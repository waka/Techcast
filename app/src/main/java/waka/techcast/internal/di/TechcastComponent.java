package waka.techcast.internal.di;

import javax.inject.Singleton;

import dagger.Component;
import waka.techcast.fragments.FeedListFragment;

@Singleton
@Component(modules = {AndroidModule.class, TechcastModule.class})
public interface TechcastComponent {
    void inject(FeedListFragment fragment);
}
