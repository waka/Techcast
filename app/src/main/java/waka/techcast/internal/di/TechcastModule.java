package waka.techcast.internal.di;

import dagger.Module;
import dagger.Provides;
import waka.techcast.view_models.FeedListViewModel;

@Module
public class TechcastModule {

    @Provides
    public FeedListViewModel provideFeedListViewModel() {
        return new FeedListViewModel();
    }
}
