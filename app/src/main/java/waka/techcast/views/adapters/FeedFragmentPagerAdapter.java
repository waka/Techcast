package waka.techcast.views.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import waka.techcast.enums.ChannelEnum;
import waka.techcast.fragments.FeedListFragment;

public class FeedFragmentPagerAdapter extends FragmentPagerAdapter {

    public FeedFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        ChannelEnum channel = ChannelEnum.valueFromId(position);
        return FeedListFragment.newInstance(channel);
    }

    @Override
    public int getCount() {
        return ChannelEnum.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ChannelEnum.valueFromId(position).getTitle();
    }
}
