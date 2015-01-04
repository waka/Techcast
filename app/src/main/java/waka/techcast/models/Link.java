package waka.techcast.models;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Link implements Serializable {
    private String title;
    private String url;

    public Link(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public static List<Link> parseToLinkList(String source) {
        List<Link> linkList = new ArrayList<>();
        if (TextUtils.isEmpty(source)) {
            return linkList;
        }
        String html = substringDescription(source);
        Pattern pattern = Pattern.compile("<a.*?href=\"(.*?)\".*?>(.*?)</a>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(html);

        while (matcher.find()) {
            String href = matcher.group(1).replaceAll("¥¥s", "");        // (2)
            String text = matcher.group(2).replaceAll("¥¥s", "");
            linkList.add(new Link(text, href));
        }

        return linkList;
    }

    public static String substringDescription(String source) {
        if (TextUtils.isEmpty(source)) return "";
        int startIndex = source.indexOf("Show Notes");
        if (0 > startIndex) {
            startIndex = source.indexOf("Show Note");
            if (0 > startIndex) {
                startIndex = source.indexOf("Relevant Links:");
                if (0 > startIndex) return "";
            }
        }

        return source.substring(startIndex, source.length());
    }
}
