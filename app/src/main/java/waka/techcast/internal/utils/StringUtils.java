package waka.techcast.internal.utils;

import android.text.Html;

import java.util.Formatter;
import java.util.Locale;

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

    public static String replaceSigns(String str) {
        return str.replaceAll(" ", "_").replaceAll("　", "_").replaceAll("/", "_");
    }

    public static String seekPositionToString(int position) {
        return DurationFormatter.format(position);
    }

    private static final class DurationFormatter {

        public static String format(int source) {
            StringBuilder stringBuilder = new StringBuilder();
            Formatter formatter = new Formatter(stringBuilder, Locale.getDefault());

            int totalSeconds = source / 1000;

            int seconds = totalSeconds % 60;
            int minutes = (totalSeconds / 60) % 60;
            int hours = totalSeconds / 3600;

            stringBuilder.setLength(0);
            if (hours > 0) {
                return formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
            } else {
                return formatter.format("%02d:%02d", minutes, seconds).toString();
            }
        }

        private DurationFormatter() {}
    }
}
