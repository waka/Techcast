package waka.techcast.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Singleton;

import waka.techcast.models.Item;
import waka.techcast.rx.PodcastPlayerSubject;
import waka.techcast.stores.ItemStore;

@Singleton
public class PodcastPlayer extends MediaPlayer implements MediaPlayer.OnPreparedListener {
    public enum Status {
        PLAYING,
        PAUSED,
        STOPPED
    }

    private Status status = Status.STOPPED;
    private Item playingItem;
    private TickTimer tickTimer;

    public Item getPlayingItem() {
        return playingItem;
    }

    @Override
    public boolean isPlaying() {
        return status == Status.PLAYING;
    }

    public boolean isPaused() {
        return status == Status.PAUSED;
    }

    public boolean isStopped() {
        return status == Status.STOPPED;
    }

    public boolean isPlayingItem(Item item) {
        return playingItem != null && item.equals(playingItem);
    }

    public void play(Context context, Item item) {
        if (item.getEnclosure() == null) return;
        playingItem = item;

        reset();

        try {
            if (ItemStore.exists(context, item)) {
                setDataSource(ItemStore.getExternalStorageFile(context, item).getAbsolutePath());
            } else {
                setDataSource(context.getApplicationContext(), Uri.parse(item.getEnclosure().getUrl()));
            }
            prepareAsync();
            setOnPreparedListener(this);
        } catch (IOException e) {
            // do nothing
        }
    }

    @Override
    public void start() {
        super.start();
        status = Status.PLAYING;

        if (tickTimer == null) {
            tickTimer = new TickTimer();
        }
        tickTimer.start();
        PodcastPlayerSubject.play(playingItem);
    }

    @Override
    public void pause() {
        super.pause();
        status = Status.PAUSED;

        tickTimer.stop();
        PodcastPlayerSubject.pause(playingItem);
    }

    @Override
    public void stop() {
        super.pause();
        super.seekTo(0);
        status = Status.STOPPED;

        playingItem = null;
        tickTimer.cancel();
        PodcastPlayerSubject.stop();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        start();
        status = Status.PLAYING;
    }

    private static class TickTimer extends Timer {
        private int count = 0;
        private boolean stopped;
        private boolean isRunning = false;

        public void start() {
            stopped = false;
            if (isRunning) return;

            scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (!stopped) {
                        count++;
                        PodcastPlayerSubject.tick(count * 1000);
                    }
                }
            }, 1000, 1000);

            isRunning = true;
        }

        public void stop() {
            stopped = true;
        }
    }
}
