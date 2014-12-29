package waka.techcast.network;

import com.squareup.okhttp.Call;

import rx.Observable;
import rx.Subscriber;

public class RequestSubscriber implements Observable.OnSubscribe<String> {
    private final Call call;

    public RequestSubscriber(Call call) {
        this.call = call;
    }

    @Override
    public void call(final Subscriber<? super String> subscriber) {
        RequestWrapper request = new RequestWrapper(call, new RequestWrapper.Callbacks() {
            @Override
            public void onSuccess(String responseText) {
                subscriber.onNext(responseText);
                subscriber.onCompleted();
            }

            @Override
            public void onFailure(Throwable error) {
                subscriber.onError(error);
            }
        });
        RequestQueue.add(new RequestJob(request));
    }
}
