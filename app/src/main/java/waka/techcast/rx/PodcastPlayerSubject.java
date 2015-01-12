package waka.techcast.rx;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.ReplaySubject;
import waka.techcast.models.Item;

public class PodcastPlayerSubject {
    private static ReplaySubject<Item> playSubject  = ReplaySubject.create();
    private static ReplaySubject<Item> pauseSubject = ReplaySubject.create();
    private static ReplaySubject<Void> stopSubject  = ReplaySubject.create();

    public static void play(Item item) {
        playSubject.onNext(item);
    }

    public static void pause(Item item) {
        pauseSubject.onNext(item);
    }

    public static void stop() {
        stopSubject.onNext(null);
    }

    public static Observable<Item> receivePlayed() {
        return playSubject.observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Item> receivePaused() {
        return pauseSubject.observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Void> receiveStopped() {
        return stopSubject.observeOn(AndroidSchedulers.mainThread());
    }
}
