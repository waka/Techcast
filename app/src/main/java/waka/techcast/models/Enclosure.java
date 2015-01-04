package waka.techcast.models;

import java.io.Serializable;

public class Enclosure implements Serializable {
    private String url;
    private String type;
    private int length;

    public Enclosure(String url, String type, int length) {
        this.url = url;
        this.type = type;
        this.length = length;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
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
