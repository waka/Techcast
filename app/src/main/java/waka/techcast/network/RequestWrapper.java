package waka.techcast.network;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class RequestWrapper {
    public interface Callbacks {
        public void onSuccess(String responseText);
        public void onFailure(Throwable error);
    }

    private final Call call;
    private final Callbacks callbacks;

    public RequestWrapper(Call call, Callbacks callbacks) {
        this.call = call;
        this.callbacks = callbacks;
    }

    public Call getCall() {
        return call;
    }

    public void execute() throws IOException {
        Response res = call.execute();
        if (res.isSuccessful()) {
            callbacks.onSuccess(res.body().string());
        } else {
            callbacks.onFailure(new Exception("failed"));
        }
    }

    public void failure(Throwable e) {
        callbacks.onFailure(e);
    }
}
