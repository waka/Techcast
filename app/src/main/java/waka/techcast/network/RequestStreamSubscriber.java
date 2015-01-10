package waka.techcast.network;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Response;

import java.io.InputStream;

import rx.Observable;
import rx.Subscriber;

public class RequestStreamSubscriber implements Observable.OnSubscribe<InputStream> {
    private final Call call;

    public RequestStreamSubscriber(Call call) {
        this.call = call;
    }

    @Override
    public void call(final Subscriber<? super InputStream> subscriber) {
        RequestWrapper request = new RequestWrapper(call, new RequestCallbacks() {
            @Override
            public void onSuccess(Response response) {
                subscriber.onNext(response.body().byteStream());
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
