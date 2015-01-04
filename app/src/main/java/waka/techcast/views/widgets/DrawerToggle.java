package waka.techcast.views.widgets;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class DrawerToggle extends ActionBarDrawerToggle {
    public DrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar) {
        super(activity, drawerLayout, toolbar, 0, 0);
    }

    @Override
    public void onDrawerClosed(View view) {
    }

    @Override
    public void onDrawerOpened(View drawerView) {
    }
}
