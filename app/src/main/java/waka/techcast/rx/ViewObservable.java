package waka.techcast.rx;

import android.view.View;

import rx.Observable;
import rx.subjects.PublishSubject;

public class ViewObservable {
    public static Observable<Void> click(View view) {
        final PublishSubject<Void> subject = PublishSubject.create();
        view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                subject.onNext(null);
            }
        });
        return subject;
    }
}
