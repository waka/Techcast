package waka.techcast.network;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Response;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;

public class RequestSubscriber implements Observable.OnSubscribe<String> {
    private final Call call;

    public RequestSubscriber(Call call) {
        this.call = call;
    }

    @Override
    public void call(final Subscriber<? super String> subscriber) {
        RequestWrapper request = new RequestWrapper(call, new RequestCallbacks() {
            @Override
            public void onSuccess(Response response) {
                try {
                    subscriber.onNext(response.body().string());
                } catch (IOException e) {
                    subscriber.onNext(null);
                }
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
