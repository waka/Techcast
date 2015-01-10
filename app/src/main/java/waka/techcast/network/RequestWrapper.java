package waka.techcast.network;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class RequestWrapper {
    private final Call call;
    private final RequestCallbacks callbacks;

    public RequestWrapper(Call call, RequestCallbacks callbacks) {
        this.call = call;
        this.callbacks = callbacks;
    }

    public Call getCall() {
        return call;
    }

    public void execute() throws IOException {
        Response res = call.execute();
        if (res.isSuccessful()) {
            callbacks.onSuccess(res);
        } else {
            callbacks.onFailure(new Exception("failed"));
        }
    }

    public void failure(Throwable e) {
        callbacks.onFailure(e);
    }
}
