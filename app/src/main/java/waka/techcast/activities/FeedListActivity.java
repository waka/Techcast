package waka.techcast.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import rx.android.observables.AndroidObservable;
import rx.functions.Action1;
import waka.techcast.R;
import waka.techcast.enums.ChannelEnum;
import waka.techcast.fragments.FeedListFragment;
import waka.techcast.internal.di.Injector;
import waka.techcast.media.PodcastPlayer;
import waka.techcast.models.Item;
import waka.techcast.rx.FeedListSubject;
import waka.techcast.rx.PodcastPlayerSubject;
import waka.techcast.views.adapters.DrawerListAdapter;
import waka.techcast.views.widgets.DrawerToggle;
import waka.techcast.views.widgets.MiniPodcastPlayerView;

public class FeedListActivity extends ActionBarActivity {
    private DrawerToggle drawerToggle;

    @Inject
    PodcastPlayer podcastPlayer;

    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @InjectView(R.id.drawer)
    LinearLayout drawer;

    @InjectView(R.id.drawer_list)
    ListView drawerListView;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.toolbar_title)
    TextView toolbarTitleView;

    @InjectView(R.id.mini_player)
    MiniPodcastPlayerView miniPodcastPlayerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_list);

        Injector.get().inject(this);
        ButterKnife.inject(this);

        setupToolbar();
        setupMiniPodcastPlayer();
        setupDrawerLayout();
        setupSubjects();

        if (savedInstanceState == null) {
           getFragmentManager().beginTransaction()
                    .add(R.id.container, FeedListFragment.newInstance(ChannelEnum.REBUILD))
                    .commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (podcastPlayer.isPlaying() || podcastPlayer.isPaused()) {
            miniPodcastPlayerView.show();
        } else {
            miniPodcastPlayerView.hide();
        }
    }

    @Override
    public void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_reload:
                FeedListSubject.reload();
                break;
        }

        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);

        actionBar.setTitle("");

        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.theme_primary));
        colorDrawable.setAlpha(0);
        actionBar.setBackgroundDrawable(colorDrawable);

        toolbarTitleView.setText(ChannelEnum.REBUILD.getTitle());
        toolbarTitleView.setAlpha(0);
    }

    private void setupMiniPodcastPlayer() {
        miniPodcastPlayerView.setCallback(new MiniPodcastPlayerView.Callback() {
            @Override
            public void onClick(Item item) {
                moveToDetail(item);
            }
        });
    }

    private void setupSubjects() {
        AndroidObservable.bindActivity(this, PodcastPlayerSubject.receivePlayed())
                .subscribe(new Action1<Item>() {
                    @Override
                    public void call(Item item) {
                        miniPodcastPlayerView.setPlayAndPauseCheckbox(true);
                        miniPodcastPlayerView.show();
                    }
                });
        AndroidObservable.bindActivity(this, PodcastPlayerSubject.receivePaused())
                .subscribe(new Action1<Item>() {
                    @Override
                    public void call(Item item) {
                        miniPodcastPlayerView.setPlayAndPauseCheckbox(false);
                    }
                });
        AndroidObservable.bindActivity(this, PodcastPlayerSubject.receiveStopped())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void empty) {
                        miniPodcastPlayerView.hide();
                    }
                });
    }

    public void updateToolbar(int height, int y) {
        ActionBar actionBar = getSupportActionBar();
        int alpha;

        if (0 >= y) {
            alpha = 0;
        } else if (y > height) {
            alpha = 255;
        } else {
            alpha = (int) ((y * 1.0 / height) * 255);
        }
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.theme_primary));
        colorDrawable.setAlpha(alpha);
        actionBar.setBackgroundDrawable(colorDrawable);

        toolbarTitleView.setAlpha(alpha / 255F);
    }

    private void setupDrawerLayout() {
        drawerListView.setAdapter(new DrawerListAdapter(this, ChannelEnum.toList()));
        drawerToggle = new DrawerToggle(this, drawerLayout, toolbar);
        drawerToggle.setDrawerIndicatorEnabled(true);
    }

    @OnItemClick(R.id.drawer_list)
    public void selectDrawerItem(int position) {
        ChannelEnum channel = ChannelEnum.toList().get(position);
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, FeedListFragment.newInstance(channel))
                .commit();
        toolbarTitleView.setText(channel.getTitle());

        drawerListView.setItemChecked(position, true);
        drawerLayout.closeDrawer(drawer);
    }

    public void moveToDetail(Item item) {
        Intent intent = new Intent(this, FeedDetailActivity.class);
        intent.putExtra(Item.KEY, item);
        startActivity(intent);
    }
}
