package waka.techcast.enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum ChannelEnum {
    REBUILD(0, "Rebuild", "http://feeds.rebuild.fm/rebuildfm"),
    MOSAICFM(1, "mosaic.fm", "http://feeds.feedburner.com/mozaicfm"),
    ANDROID_DEVELOPER_BACKSTAGE(2, "Android Developer Backstage", "http://feeds.feedburner.com/blogspot/AndroidDevelopersBackstage");

    private final int id;
    private final String title;
    private final String url;

    private ChannelEnum(int id, String title, String url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public static ChannelEnum valueFromId(Integer id) {
        if (id == null) {
            return null;
        }
        for (ChannelEnum item : values()) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public static List<ChannelEnum> toList() {
        List<ChannelEnum> items = new ArrayList<>();
        Collections.addAll(items, values());
        return items;
    }
}
