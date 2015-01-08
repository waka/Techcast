package waka.techcast.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;
import waka.techcast.internal.utils.RequestBuilderUtils;
import waka.techcast.models.Item;
import waka.techcast.network.Client;

public class DownloadService extends IntentService {
    @Inject
    Client client;

    private static final String EXTRA_KEY = "mp3_download";
    private static List<Item> downloadingList = new ArrayList<>();

    public static Intent createIntent(Context context, Item item) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(EXTRA_KEY, item);
        return intent;
    }

    public static void start(Context context, Item item) {
        Intent intent = createIntent(context, item);
        context.startService(intent);
    }

    public DownloadService() {
        super(DownloadService.class.getSimpleName());
    }

    public DownloadService(String name) {
        super(name);
    }

    public static boolean isDownloading(Item item) {
        int index = getItemIndexFromDownloadingList(item);
        return (index != -1);
    }

    public static void cancel(Item item) {
        int index = getItemIndexFromDownloadingList(item);
        if (index == -1) {
            return;
        }

        removeItemFromDownloadingList(item);
        //DownloadNotification.cancel(context, item);
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

        client.cache(getApplicationContext())
                .call(RequestBuilderUtils.get(item.getEnclosure().getUrl()))
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                    }
                });
    }
}
