package waka.techcast.internal.utils;

import com.squareup.okhttp.Request;

public class RequestBuilderUtils {
    public static Request get(String url) {
        return new Request.Builder().url(url).build();
    }
}
