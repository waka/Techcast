package waka.techcast.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import waka.techcast.R;
import waka.techcast.fragments.FeedDetailFragment;
import waka.techcast.models.Item;

public class FeedDetailActivity extends ActionBarActivity {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.toolbar_title)
    TextView toolbarTitleView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        ButterKnife.inject(this);

        Item item = (Item) getIntent().getSerializableExtra(Item.KEY);
        setupToolbar(item);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, FeedDetailFragment.newInstance(item))
                    .commit();
        }
    }

    private void setupToolbar(Item item) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("");

        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.theme_primary));
        colorDrawable.setAlpha(0);
        actionBar.setBackgroundDrawable(colorDrawable);

        toolbarTitleView.setText(item.getTitle());
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

    @Override
    public void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
