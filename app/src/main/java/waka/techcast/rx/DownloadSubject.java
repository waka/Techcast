package waka.techcast.rx;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.ReplaySubject;
import waka.techcast.models.Item;

public class DownloadSubject {
    private static ReplaySubject<Item> subject = ReplaySubject.create();

    public static void post(Item item) {
        subject.onNext(item);
    }

    public static Observable<Item> receive() {
        return subject.observeOn(AndroidSchedulers.mainThread());
    }
}
