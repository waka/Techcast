package waka.techcast.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class ObservableScrollView extends ScrollView {
    final BehaviorSubject<Scroller> subject = BehaviorSubject.create();

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        subject.onNext(new Scroller(x, y, oldX, oldY));
    }

    public Observable<Scroller> scroll() {
        return subject;
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    public static class Scroller {
        public final int x;
        public final int y;
        public final int oldX;
        public final int oldY;

        public Scroller(int x, int y, int oldX, int oldY) {
            this.x = x;
            this.y = y;
            this.oldX = oldX;
            this.oldY = oldY;
        }
    }
}
