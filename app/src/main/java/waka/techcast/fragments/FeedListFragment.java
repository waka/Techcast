package waka.techcast.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import waka.techcast.internal.di.Injector;
import waka.techcast.R;
import waka.techcast.enums.ChannelEnum;
import waka.techcast.view_models.FeedListViewModel;
import waka.techcast.views.adapters.FeedListAdapter;

public class FeedListFragment extends Fragment {

    private static String CHANNEL_KEY = "channel";

    @Inject
    FeedListViewModel viewModel;

    @InjectView(R.id.feed_list)
    RecyclerView feedListView;

    public static FeedListFragment newInstance(ChannelEnum channel) {
        FeedListFragment fragment = new FeedListFragment();
        Bundle args = new Bundle();
        args.putSerializable(CHANNEL_KEY, channel);
        fragment.setArguments(args);
        return fragment;
    }

    public FeedListFragment() {
        // do not nothing
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.get().inject(this);

        if (getArguments() != null) {
            ChannelEnum channel = (ChannelEnum) getArguments().getSerializable(CHANNEL_KEY);
            viewModel.setChannel(channel);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_list, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupListView();
        getFeedList();
    }

    private void setupListView() {
        feedListView.setHasFixedSize(false);
        feedListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FeedListAdapter feedListAdapter = new FeedListAdapter();
        feedListView.setAdapter(feedListAdapter);
    }

    private void getFeedList() {
        viewModel.getFeedList();
    }
}
