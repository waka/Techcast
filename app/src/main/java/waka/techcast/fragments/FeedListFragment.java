package waka.techcast.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.functions.Action0;
import rx.functions.Action1;
import waka.techcast.R;
import waka.techcast.activities.FeedListActivity;
import waka.techcast.enums.ChannelEnum;
import waka.techcast.internal.di.Injector;
import waka.techcast.internal.utils.DialogUtils;
import waka.techcast.internal.utils.SharedPreferenceUtils;
import waka.techcast.media.PodcastPlayer;
import waka.techcast.models.Feed;
import waka.techcast.models.Item;
import waka.techcast.rx.DownloadSubject;
import waka.techcast.services.DownloadService;
import waka.techcast.stores.ItemStore;
import waka.techcast.view_models.FeedListViewModel;
import waka.techcast.views.adapters.FeedListAdapter;
import waka.techcast.views.widgets.MaterialDialog;
import waka.techcast.views.widgets.ProgressDialog;

public class FeedListFragment extends Fragment {
    private static final String CHANNEL_KEY = "channel";
    private static final int LOGO_IMAGE_DP = 128;

    private FeedListAdapter feedListAdapter;

    @Inject
    FeedListViewModel viewModel;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    PodcastPlayer podcastPlayer;

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

        DownloadSubject.receive()
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (!isAdded()) return;
                        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .subscribe(new Action1<Item>() {
                    @Override
                    public void call(Item item) {
                        if (!isAdded()) return;
                        feedListAdapter.notifyDataSetChanged();
                    }
                });
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
        feedListView.addHeaderView(feedListAdapter.getHeaderViewHolder().getView());

        feedListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {}

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                View header = feedListAdapter.getHeaderViewHolder().getView();
                int height = (header == null) ? 0 : header.getHeight();
                int y = (header == null) ? 0 : header.getTop();
                ((FeedListActivity) getActivity()).updateToolbar(height, Math.abs(y));
            }
        });
    }

    private void fetchFeed() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        final int px = (int) (metrics.density * LOGO_IMAGE_DP);

        final ProgressDialog dialog = ProgressDialog.show(getActivity());

        viewModel.getFeedList(getActivity())
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        dialog.cancel();
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (!isAdded()) return;
                        Toast.makeText(getActivity(), R.string.error_fetch_feed, Toast.LENGTH_LONG).show();
                    }
                })
                .subscribe(new Action1<Feed>() {
                    @Override
                    public void call(final Feed feed) {
                        Picasso.with(getActivity())
                                .load(Uri.parse(feed.getImage()))
                                .resize(px, px)
                                .into(feedListAdapter.getHeaderViewHolder().getLogoImageView(), new Callback() {
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

    private void handleItemToClick(Item item) {
        ((FeedListActivity) getActivity()).moveToDetail(item);
    }

    private void handleItemToPlay(final Item item) {
        if (!ItemStore.exists(getActivity(), item) && SharedPreferenceUtils.shouldShowDialog(sharedPreferences)) {
            MaterialDialog dialog = DialogUtils.createFirstStreamingDialog(getActivity(), item, new DialogUtils.DialogCallbacks() {
                @Override
                public void onConfirm() {
                    if (!isAdded()) return;
                    SharedPreferenceUtils.doneShowDialog(sharedPreferences);
                    podcastPlayer.play(getActivity(), item);
                }
            });
            dialog.show();
        } else {
            // immediate play
            podcastPlayer.play(getActivity(), item);
        }
    }

    private void handleItemToDownload(final Item item) {
        MaterialDialog dialog;

        if (DownloadService.isDownloading(item)) {
            dialog = DialogUtils.createDownloadCancelDialog(getActivity(), item, new DialogUtils.DialogCallbacks() {
                @Override
                public void onConfirm() {
                    DownloadService.cancel(getActivity(), item);
                }
            });
        } else {
            dialog = DialogUtils.createDownloadDialog(getActivity(), item, new DialogUtils.DialogCallbacks() {
                @Override
                public void onConfirm() {
                    DownloadService.start(getActivity(), item);
                }
            });
        }
        dialog.show();
    }

    private void handleItemToClear(final Item item) {
        MaterialDialog dialog = DialogUtils.createDownloadClearDialog(getActivity(), item, new DialogUtils.DialogCallbacks() {
            @Override
            public void onConfirm() {
                if (ItemStore.delete(getActivity(), item)) {
                    feedListAdapter.notifyDataSetChanged();
                }
            }
        });
        dialog.show();
    }
}
