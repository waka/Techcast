package waka.techcast.models;

import android.net.Uri;

public class Enclosure {
    private Uri url;
    private String type;
    private int length;

    public Enclosure(Uri url, String type, int length) {
        this.url = url;
        this.type = type;
        this.length = length;
    }

    public Uri getUrl() {
        return url;
    }

    public void setUrl(Uri url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
