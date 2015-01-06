package waka.techcast.fragments;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.functions.Action1;
import waka.techcast.R;
import waka.techcast.activities.FeedListActivity;
import waka.techcast.enums.ChannelEnum;
import waka.techcast.internal.di.Injector;
import waka.techcast.models.Feed;
import waka.techcast.models.Item;
import waka.techcast.services.DownloadService;
import waka.techcast.view_models.FeedListViewModel;
import waka.techcast.views.adapters.FeedListAdapter;

public class FeedListFragment extends Fragment {
    private static final String CHANNEL_KEY = "channel";
    private static final int LOGO_IMAGE_DP = 128;

    private FeedListAdapter feedListAdapter;
    private FeedListAdapter.HeaderItemViewHolder headerItemViewHolder;

    @Inject
    FeedListViewModel viewModel;

    @InjectView(R.id.feed_list)
    ListView feedListView;

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

        ChannelEnum channel = (ChannelEnum) getArguments().getSerializable(CHANNEL_KEY);
        viewModel.setChannel(channel);
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
        fetchFeed();
    }

    private void setupListView() {
        List<Item> items = new ArrayList<>();
        feedListAdapter = new FeedListAdapter(getActivity(), items, new FeedListAdapter.OnClickListener() {
            @Override
            public void onContentsClick(Item item) {
                handleItemToClick(item);
            }

            @Override
            public void onPlayClick(Item item) {
                handleItemToPlay(item);
            }

            @Override
            public void onDownloadClick(Item item) {
                handleItemToDownload(item);
            }

            @Override
            public void onClearClick(Item item) {
                handleItemToClear(item);
            }
        });
        feedListView.setAdapter(feedListAdapter);

        View view = feedListAdapter.getInflater().inflate(R.layout.list_item_feed_header, null, false);
        headerItemViewHolder = new FeedListAdapter.HeaderItemViewHolder(view);
        feedListView.addHeaderView(headerItemViewHolder.getView());

        feedListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {}

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                View header = headerItemViewHolder.getView();
                int height = (header == null) ? 0 : header.getHeight();
                int y = (header == null) ? 0 : header.getTop();
                ((FeedListActivity) getActivity()).updateToolbar(height, Math.abs(y));
            }
        });
    }

    private void fetchFeed() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        final int px = (int) (metrics.density * LOGO_IMAGE_DP);

        viewModel.getFeedList(getActivity()).subscribe(new Action1<Feed>() {
            @Override
            public void call(final Feed feed) {
                Picasso.with(getActivity())
                        .load(Uri.parse(feed.getImage()))
                        .resize(px, px)
                        .into(headerItemViewHolder.getLogoImageView(), new Callback() {
                            @Override
                            public void onSuccess() {
                                displayItems(feed.getItems());
                            }

                            @Override
                            public void onError() {
                                displayItems(feed.getItems());
                            }
                        });
            }
        });
    }

    private void displayItems(List<Item> items) {
        feedListAdapter.setItems(items);
    }

    private void handleItemToClick(final Item item) {
        ((FeedListActivity) getActivity()).moveToDetail(item);
    }

    private void handleItemToPlay(final Item item) {
        if (item.isDownloaded()) {
            // play from cache
        } else {
            // play from streaming
        }
    }

    private void handleItemToDownload(final Item item) {
        if (DownloadService.isDownloading(item)) {
            DownloadService.cancel(item);
        } else {
            DownloadService.start(getActivity(), item);
        }
    }

    private void handleItemToClear(final Item item) {
        DownloadService.clear(item);
    }
}
