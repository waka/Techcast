package waka.techcast.view_models;

import waka.techcast.models.Item;

public class FeedDetailViewModel {
    private Item item;

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
