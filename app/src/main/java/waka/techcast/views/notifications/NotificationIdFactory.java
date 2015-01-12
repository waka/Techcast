package waka.techcast.views.notifications;

import waka.techcast.models.Item;

public class NotificationIdFactory {
    public enum Type {
        DOWNLOADING(1000),
        DOWNLOADED(2000),
        PLAYER(3000);

        private final int code;

        private Type(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public static int get(Item item, Type type) {
        return item.getId() + type.getCode();
    }
}
