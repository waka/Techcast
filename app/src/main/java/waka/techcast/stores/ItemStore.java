package waka.techcast.stores;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import waka.techcast.models.Item;

public class ItemStore {
    private static final int BUFFER_SIZE = 10 * 1024;

    public static boolean save(Context context, InputStream inputStream, Item item) {
        BufferedInputStream bufferedInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            File file = getExternalStorageFile(context, item);
            bufferedInputStream = new BufferedInputStream(inputStream, BUFFER_SIZE);
            fileOutputStream = new FileOutputStream(file);

            int seek;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((seek = bufferedInputStream.read(buffer, 0, BUFFER_SIZE)) > 0) {
                fileOutputStream.write(buffer, 0, seek);
            }

            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (fileOutputStream != null) fileOutputStream.close();
                if (bufferedInputStream != null) bufferedInputStream.close();
            } catch (IOException e) {
                // nothing to do
            }
        }
    }

    public static boolean delete(Context context, Item item) {
        if (!exists(context, item)) {
            return false;
        }
        File file = getExternalStorageFile(context, item);
        return file.delete();
    }

    public static File getExternalStorageFile(Context context, Item item) {
        return new File(context.getExternalFilesDir(null), item.getFileName());
    }

    public static boolean exists(Context context, Item item) {
        if (TextUtils.isEmpty(item.getFileName())) {
            return false;
        }
        File file = getExternalStorageFile(context, item);
        return file.exists();
    }
}
