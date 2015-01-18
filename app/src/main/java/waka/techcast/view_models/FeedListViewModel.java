package waka.techcast.view_models;

import android.content.Context;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import waka.techcast.enums.ChannelEnum;
import waka.techcast.internal.di.Injector;
import waka.techcast.internal.rss.FeedConverter;
import waka.techcast.internal.utils.RequestBuilderUtils;
import waka.techcast.models.Feed;
import waka.techcast.network.Client;

public class FeedListViewModel {
    @Inject
    Client client;

    private ChannelEnum channel;
    private Feed feed;

    public FeedListViewModel() {
        Injector.get().inject(this);
    }

    public void setChannel(ChannelEnum channel) {
        this.channel = channel;
    }

    public ChannelEnum getChannel() {
        return channel;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public Feed getFeed() {
        return feed;
    }

    public Observable<Feed> getFeedList(Context context, boolean refresh) {
        return client
                .cache(context, refresh)
                .call(RequestBuilderUtils.get(channel.getUrl()))
                .map(new FeedListFunc());
    }

    private class FeedListFunc implements Func1<String, Feed> {
        @Override
        public Feed call(String responseText) {
            feed = FeedConverter.convert(responseText);
            return feed;
        }
    }
}
