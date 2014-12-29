package waka.techcast.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;
import waka.techcast.R;
import waka.techcast.views.adapters.FeedFragmentPagerAdapter;
import waka.techcast.views.widgets.TabContainer;


public class FeedListActivity extends ActionBarActivity {
    @InjectView(R.id.tabs)
    TabContainer tabContainer;

    @InjectView(R.id.pager)
    ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_list);

        ButterKnife.inject(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }

        setupViewPager();
    }

    @Override
    public void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }

    private void setupViewPager() {
        tabContainer.setup();

        FeedFragmentPagerAdapter fragmentPagerAdapter = new FeedFragmentPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < fragmentPagerAdapter.getCount(); i++) {
            tabContainer.addTab(fragmentPagerAdapter.getPageTitle(i));
        }

        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOnPageChangeListener(tabContainer);

        tabContainer.setOnTabChangedListener(new TabContainer.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                viewPager.setCurrentItem(Integer.valueOf(tabId));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
