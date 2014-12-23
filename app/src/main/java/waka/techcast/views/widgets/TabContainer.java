package waka.techcast.views.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import waka.techcast.R;

public class TabContainer extends TabHost implements ViewPager.OnPageChangeListener {

    private final LayoutInflater inflater;
    private TabWidget tabWidget;
    private View indicator;
    private int scrollingState = ViewPager.SCROLL_STATE_IDLE;

    public TabContainer(Context context) {
        this(context, null);
    }

    public TabContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void setup() {
        super.setup();

        tabWidget = (TabWidget) findViewById(android.R.id.tabs);
        tabWidget.setStripEnabled(false);
        tabWidget.setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);

        indicator = findViewById(R.id.indicator);

        float elevation = 4 * getResources().getDisplayMetrics().density;
        setElevation(elevation);
    }

    public void addTab(CharSequence title) {
        TextView tv = (TextView) inflater.inflate(R.layout.view_tab_text, tabWidget, false);
        tv.setText(title);

        TabSpec tabSpec = newTabSpec(String.valueOf(tabWidget.getTabCount()))
                .setIndicator(tv)
                .setContent(android.R.id.tabcontent);
        addTab(tabSpec);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        updateIndicatorPosition(position, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
        // Ignore when scrolling
        if (scrollingState == ViewPager.SCROLL_STATE_IDLE) {
            updateIndicatorPosition(position, 0);
        }
        tabWidget.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        scrollingState = state;
    }

    private void updateIndicatorPosition(int position, float positionOffset) {
        View tabView = tabWidget.getChildTabViewAt(position);
        int indicatorWidth = tabView.getWidth();
        int indicatorLeft = (int) ((position + positionOffset) * indicatorWidth);

        final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) indicator.getLayoutParams();
        layoutParams.width = indicatorWidth;
        layoutParams.setMargins(indicatorLeft, 0, 0, 0);
        indicator.setLayoutParams(layoutParams);
    }
}
