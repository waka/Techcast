package waka.techcast.network;

import android.support.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

@Singleton
public class RequestQueue {
    private static final Internal internal;

    static {
        internal = new Internal();
    }

    RequestQueue() {}

    public static void start() {
        internal.start();
    }

    public static void stop() {
        internal.stop();
    }

    public static void add(RequestJob job) {
        internal.add(job);
    }

    public static void cancelAll() {
        internal.cancelAll();
    }

    public static void finished(RequestJob job) {
        internal.finished(job);
    }

    private static final class Internal {
        private boolean isRunning;
        private final int maxRequests = 16;
        private ExecutorService executorService;
        private final Deque<RequestJob> readyQueue = new ArrayDeque<>();
        private final Deque<RequestJob> runningQueue = new ArrayDeque<>();

        Internal() {
            isRunning = true;
        }

        private synchronized ExecutorService getExecutorService() {
            if (executorService == null) {
                executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>(), createThreadFactory());
            }
            return executorService;
        }

        private ThreadFactory createThreadFactory() {
            return new ThreadFactory() {
                @Override public Thread newThread(@NonNull Runnable runnable) {
                    Thread result = new Thread(runnable);
                    result.setDaemon(false);
                    return result;
                }
            };
        }

        public void add(RequestJob job) {
            if (isRunning && runningQueue.size() < maxRequests) {
                runningQueue.add(job);
                getExecutorService().execute(job);
            } else {
                readyQueue.add(job);
            }
        }

        public void cancelAll() {
            for (Iterator<RequestJob> i = readyQueue.iterator(); i.hasNext(); ) {
                i.remove();
            }
            readyQueue.clear();

            for (RequestJob job : runningQueue) {
                job.cancel();
            }
            runningQueue.clear();
        }

        public void start() {
            isRunning = true;
            conveyorToRunning();
        }

        public void stop() {
            isRunning = false;
        }

        /** Used by {@code RequestJob#run} to signal completion. */
        synchronized void finished(RequestJob job) {
            if (!runningQueue.remove(job)) {
                throw new AssertionError("RequestJob wasn't running!");
            }
            conveyorToRunning();
        }

        private void conveyorToRunning() {
            // Already running max capacity or No ready jobs to conveyor.
            if (runningQueue.size() >= maxRequests || readyQueue.isEmpty()) {
                return;
            }

            for (Iterator<RequestJob> i = readyQueue.iterator(); i.hasNext(); ) {
                RequestJob job = i.next();

                i.remove();
                runningQueue.add(job);
                getExecutorService().execute(job);

                // Reached max capacity.
                if (runningQueue.size() >= maxRequests) return;
            }
        }
    }
}
