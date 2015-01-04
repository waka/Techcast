package waka.techcast.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import waka.techcast.models.Item;

public class Mp3DownloadService extends IntentService {
    private static final String EXTRA_KEY = "mp3_download";
    private static List<Item> downloadingList = new ArrayList<>();

    public static Intent createIntent(Context context, Item item) {
        Intent intent = new Intent(context, Mp3DownloadService.class);
        intent.putExtra(EXTRA_KEY, item);
        return intent;
    }

    public static void startDownload(Context context, Item item) {
        Intent intent = createIntent(context, item);
        context.startService(intent);
    }

    public Mp3DownloadService() {
        super(Mp3DownloadService.class.getSimpleName());
    }

    public Mp3DownloadService(String name) {
        super(name);
    }

    public static boolean isDownloading(Item item) {
        int index = getItemIndexFromDownloadingList(item);
        return (index != -1);
    }

    public static void cancel(Context context, Item item) {
        int index = getItemIndexFromDownloadingList(item);
        if (index == -1) {
            return;
        }

        removeItemFromDownloadingList(item);
        //Mp3DownloadNotification.cancel(context, item);
    }

    public static int getItemIndexFromDownloadingList(Item item) {
        if (item == null) {
            return -1;
        }

        int index = 0;
        while (index < downloadingList.size()) {
            Item downloadingItem = downloadingList.get(index);
            if (downloadingItem.equals(item)) {
                return index;
            }
            index++;
        }

        return -1;
    }

    public static void removeItemFromDownloadingList(Item item) {
        if (item == null) {
            return;
        }

        int index = getItemIndexFromDownloadingList(item);
        if (index == -1) {
            return;
        }

        downloadingList.remove(index);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Item item = (Item) intent.getSerializableExtra(EXTRA_KEY);
        if (item == null || isDownloading(item)) {
            return;
        }
    }
}
