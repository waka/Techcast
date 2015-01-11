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

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import waka.techcast.R;
import waka.techcast.enums.ChannelEnum;
import waka.techcast.fragments.FeedListFragment;
import waka.techcast.models.Item;
import waka.techcast.views.adapters.DrawerListAdapter;
import waka.techcast.views.widgets.DrawerToggle;

public class FeedListActivity extends ActionBarActivity {
    private DrawerToggle drawerToggle;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_list);

        ButterKnife.inject(this);

        setupToolbar();
        setupDrawerLayout();

        if (savedInstanceState == null) {
           getFragmentManager().beginTransaction()
                    .add(R.id.container, FeedListFragment.newInstance(ChannelEnum.REBUILD))
                    .commit();
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
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
