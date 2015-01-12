package waka.techcast.internal.utils;

import android.content.SharedPreferences;

public class SharedPreferenceUtils {
    private static String FIRST_STREAMING_KEY = "first_streaming";

    public static boolean shouldShowDialog(SharedPreferences sharedPreferences) {
        return !sharedPreferences.getBoolean(FIRST_STREAMING_KEY, false);
    }

    public static void doneShowDialog(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FIRST_STREAMING_KEY, true);
        editor.apply();
    }
}
