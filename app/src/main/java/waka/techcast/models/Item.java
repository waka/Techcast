package waka.techcast.models;

import java.io.Serializable;

public class Item implements Serializable {
    public static final String KEY = "ITEM";

    private String title;
    private String description;
    private String pubDate;
    private String link;
    private String subTitle;
    private String duration;
    private Enclosure enclosure;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Enclosure getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(Enclosure enclosure) {
        this.enclosure = enclosure;
    }

    public boolean equals(Item item) {
        return super.equals(item);
    }

    public String getFileName() {
        if (enclosure == null) return null;

        int point = enclosure.getUrl().lastIndexOf("/");
        if (point == -1) {
            return null;
        }
        return enclosure.getUrl().substring(point + 1);
    }
}
