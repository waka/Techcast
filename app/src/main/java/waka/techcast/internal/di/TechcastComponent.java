package waka.techcast.internal.di;

import javax.inject.Singleton;

import dagger.Component;
import waka.techcast.activities.FeedListActivity;
import waka.techcast.fragments.FeedDetailFragment;
import waka.techcast.fragments.FeedListFragment;
import waka.techcast.network.NetworkStateReceiver;
import waka.techcast.services.DownloadService;
import waka.techcast.view_models.FeedListViewModel;
import waka.techcast.views.widgets.MiniPodcastPlayerView;

@Singleton
@Component(modules = {AndroidModule.class, TechcastModule.class})
public interface TechcastComponent {
    void inject(FeedListActivity activity);
    void inject(FeedListFragment fragment);
    void inject(FeedListViewModel viewModel);
    void inject(FeedDetailFragment fragment);
    void inject(NetworkStateReceiver networkStateReceiver);
    void inject(DownloadService downloadService);
    void inject(MiniPodcastPlayerView miniPodcastPlayer);
}
