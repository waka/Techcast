package waka.techcast.network;

import com.squareup.okhttp.Response;

public interface RequestCallbacks {
    public void onSuccess(Response response);
    public void onFailure(Throwable error);
}
