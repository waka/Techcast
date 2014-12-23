package waka.techcast.view_models;

import java.util.List;

import waka.techcast.enums.ChannelEnum;
import waka.techcast.models.Feed;

public class FeedListViewModel {

    private ChannelEnum channel;
    private List<Feed> feedList;

    public FeedListViewModel() {}

    public void setChannel(ChannelEnum channel) {
        this.channel = channel;
    }

    public void getFeedList() {
    }
}
