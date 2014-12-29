package waka.techcast.network;

import java.io.IOException;

public class RequestJob implements Runnable {
    private final RequestWrapper request;

    public RequestJob(RequestWrapper request) {
        this.request = request;
    }

    @Override
    public void run() {
        try {
            request.execute();
        } catch (IOException e) {
            request.failure(e);
        }
        RequestQueue.finished(this);
    }

    public void cancel() {
        request.getCall().cancel();
    }
}
