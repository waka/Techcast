package waka.techcast.internal.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import waka.techcast.media.PodcastPlayer;
import waka.techcast.network.Client;
import waka.techcast.view_models.FeedDetailViewModel;
import waka.techcast.view_models.FeedListViewModel;

@Module
public class TechcastModule {
    @Provides @Singleton
    Client provideClient() {
        return new Client();
    }

    @Provides @Singleton
    PodcastPlayer providePodcastPlayer() {
        return new PodcastPlayer();
    }

    @Provides
    FeedListViewModel provideFeedListViewModel() {
        return new FeedListViewModel();
    }

    @Provides
    FeedDetailViewModel provideFeedDetailViewModel() {
        return new FeedDetailViewModel();
    }
}
