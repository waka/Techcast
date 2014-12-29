package waka.techcast.internal.di;

import javax.inject.Singleton;

import dagger.Component;
import waka.techcast.fragments.FeedListFragment;
import waka.techcast.view_models.FeedListViewModel;

@Singleton
@Component(modules = {AndroidModule.class, TechcastModule.class})
public interface TechcastComponent {
    void inject(FeedListFragment fragment);
    void inject(FeedListViewModel viewModel);
}
