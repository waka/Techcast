package waka.techcast.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action0;
import rx.functions.Action1;
import waka.techcast.internal.di.Injector;
import waka.techcast.internal.utils.RequestBuilderUtils;
import waka.techcast.models.Item;
import waka.techcast.network.Client;
import waka.techcast.rx.DownloadSubject;
import waka.techcast.stores.ItemStore;
import waka.techcast.views.notifications.DownloadedNotification;
import waka.techcast.views.notifications.DownloadingNotification;

public class DownloadService extends IntentService {
    @Inject
    Client client;

    private static final String EXTRA_KEY = "download_item";
    private static List<Item> downloadingList = new ArrayList<>();

    public static void start(Context context, Item item) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(EXTRA_KEY, item);
        context.startService(intent);
    }

    @SuppressWarnings("unused")
    public DownloadService() {
        this(DownloadService.class.getSimpleName());
    }

    public DownloadService(String name) {
        super(name);
        Injector.get().inject(this);
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
        DownloadingNotification.cancel(context, item);
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
        final Item item = (Item) intent.getSerializableExtra(EXTRA_KEY);
        if (item == null || isDownloading(item)) {
            return;
        }

        downloadingList.add(item);
        DownloadingNotification.notify(getApplicationContext(), item);

        client.callDownload(RequestBuilderUtils.get(item.getEnclosure().getUrl()))
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        DownloadingNotification.cancel(getApplicationContext(), item);
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        DownloadSubject.fail();
                    }
                })
                .subscribe(new Action1<InputStream>() {
                    @Override
                    public void call(InputStream inputStream) {
                        if (ItemStore.save(getApplicationContext(), inputStream, item)) {
                            if (isDownloading(item)) {
                                downloadingList.remove(item);
                                DownloadSubject.post(item);
                                DownloadedNotification.notify(getApplicationContext(), item);
                            } else {
                                // already cancel
                                ItemStore.delete(getApplicationContext(), item);
                            }
                        } else {
                            DownloadSubject.fail();
                        }
                    }
                });
    }
}
