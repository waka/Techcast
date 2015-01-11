package waka.techcast.views.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import waka.techcast.R;
import waka.techcast.models.Item;

/**
 * 複数ある場合はまとめるべき通知
 */
public class DownloadingNotification {
    private static int DOWNLOADING_NOTIFICATION_ID = 1000;

    public static void notify(Context context, Item item) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(DOWNLOADING_NOTIFICATION_ID, build(context, item));
    }

    public static Notification build(Context context, Item item) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle(context.getString(R.string.label_notification_downloading))
                .setContentText(item.getTitle());

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;

        return notification;
    }
}
