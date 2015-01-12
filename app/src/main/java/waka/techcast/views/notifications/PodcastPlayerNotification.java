package waka.techcast.views.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import waka.techcast.R;
import waka.techcast.models.Item;

public class PodcastPlayerNotification {
    public static void notify(Context context, Item item, int position) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(
                NotificationIdFactory.Type.PLAYER.getCode(),
                build(context, item, position));
    }

    public static Notification build(Context context, Item item, int position) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle(context.getString(R.string.label_notification_downloading))
                .setContentText(item.getTitle());

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;

        return notification;
    }

    public static void cancel(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NotificationIdFactory.Type.PLAYER.getCode());
    }
}
