package waka.techcast.rx;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.ReplaySubject;

public class FeedListSubject {
    private static ReplaySubject<Void> subject = ReplaySubject.create();

    public static void reload() {
        subject.onNext(null);
    }

    public static Observable<Void> receiveReloaded() {
        return subject.observeOn(AndroidSchedulers.mainThread());
    }
}
