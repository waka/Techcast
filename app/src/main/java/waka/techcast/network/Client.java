package waka.techcast.network;

import android.content.Context;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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

    public Observable<InputStream> callDownload(Request request) {
        Call call = okHttpClient.newCall(request);
        return Observable.create(new RequestStreamSubscriber(call))
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread());
    }

    public Client cache(Context context, boolean refresh) {
        if (refresh) return this;

        File cacheDir = new File(context.getCacheDir(), "techcast_http_cache");
        Cache cache;
        try {
            cache = new Cache(cacheDir, (long) 10.0 * 1024 * 1024);
        } catch (IOException e) {
            cache = null;
        }
        okHttpClient.setCache(cache);
        return this;
    }
}
