package waka.techcast.network;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import javax.inject.Singleton;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Singleton
public class Client {
    private final OkHttpClient okHttpClient;

    public Client() {
        okHttpClient = new OkHttpClient();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public void changeNetworkState(boolean isConnected) {
        if (isConnected) {
            RequestQueue.start();
        } else {
            RequestQueue.stop();
        }
    }

    public void cancelAllRequests() {
        RequestQueue.cancelAll();
    }

    public Observable<String> call(Request request) {
        Call call = okHttpClient.newCall(request);
        return Observable.create(new RequestSubscriber(call))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
