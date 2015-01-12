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
public class DownloadedNotification {
    public static void notify(Context context, Item item) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(
                NotificationIdFactory.get(item, NotificationIdFactory.Type.DOWNLOADED),
                build(context, item));
    }

    public static Notification build(Context context, Item item) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle(context.getString(R.string.label_notification_downloaded))
                .setContentText(item.getTitle());

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        return notification;
    }

    public static void cancel(Context context, Item item) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NotificationIdFactory.get(item, NotificationIdFactory.Type.DOWNLOADED));
    }
}
