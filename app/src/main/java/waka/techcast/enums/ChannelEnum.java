package waka.techcast.enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum ChannelEnum {
    REBUILD(0, "Rebuild", ""),
    MOSAICFM(1, "MosaicFM", "");

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
