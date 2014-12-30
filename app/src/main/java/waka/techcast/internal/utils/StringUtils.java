package waka.techcast.internal.utils;

import android.text.Html;

public class StringUtils {
    public static String fromHtml(String str) {
        if (str == null) return "";
        String replacedText = str.replaceAll("<(p|\n)*?>", "");
        return Html.fromHtml(replacedText).toString();
    }

    public static String omitArticle(String str) {
        String[] arr = str.split("。");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (2 > i && !arr[i].isEmpty() && !arr[i].equals(" ")) {
                builder.append(arr[i]);
                builder.append("。");
            }
        }
        return builder.toString();
    }
}
