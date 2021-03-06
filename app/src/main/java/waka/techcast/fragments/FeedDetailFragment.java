package waka.techcast.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.observables.AndroidObservable;
import rx.functions.Action1;
import waka.techcast.R;
import waka.techcast.activities.FeedDetailActivity;
import waka.techcast.internal.di.Injector;
import waka.techcast.internal.utils.StringUtils;
import waka.techcast.media.PodcastPlayer;
import waka.techcast.models.Item;
import waka.techcast.rx.PodcastPlayerSubject;
import waka.techcast.rx.ViewObservable;
import waka.techcast.view_models.FeedDetailViewModel;
import waka.techcast.views.widgets.ObservableScrollView;
import waka.techcast.views.widgets.RelevantLinksView;

public class FeedDetailFragment extends Fragment {
    @Inject
    FeedDetailViewModel viewModel;

    @Inject
    PodcastPlayer podcastPlayer;

    @InjectView(R.id.scrollview)
    ObservableScrollView scrollView;

    @InjectView(R.id.header)
    RelativeLayout headerLayout;

    @InjectView(R.id.item_title)
    TextView titleTextView;

    @InjectView(R.id.player_seekbar)
    SeekBar playerSeekBar;

    @InjectView(R.id.player_elapsed_time)
    TextView playerElapsedTimeTextView;

    @InjectView(R.id.player_duration)
    TextView playerDurationTextView;

    @InjectView(R.id.player_action)
    FrameLayout playerActionView;

    @InjectView(R.id.play_button)
    ImageView playButton;

    @InjectView(R.id.pause_button)
    ImageView pauseButton;

    @InjectView(R.id.item_description)
    TextView descriptionTextView;

    @InjectView(R.id.show_notes)
    RelevantLinksView relevantLinksView;

    public static FeedDetailFragment newInstance(Item item) {
        FeedDetailFragment fragment = new FeedDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(Item.KEY, item);
        fragment.setArguments(args);
        return fragment;
    }

    public FeedDetailFragment() {
        // do not nothing
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.get().inject(this);

        Item item = (Item) getArguments().getSerializable(Item.KEY);
        viewModel.setItem(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_detail, container, false);
        ButterKnife.inject(this, view);

        setupViews();
        setupSubjects();

        return view;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        setupPlayer();
    }

    private void setupViews() {
        Item currentItem = viewModel.getItem();

        titleTextView.setText(currentItem.getTitle());
        descriptionTextView.setText(
                StringUtils.omitArticle(StringUtils.fromHtml(currentItem.getSubTitle())));
        relevantLinksView.addLinkView(currentItem);
    }

    private void setupSubjects() {
        final Item currentItem = viewModel.getItem();

        AndroidObservable.bindFragment(this, ViewObservable.click(playerActionView))
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void empty) {
                        if (podcastPlayer.isPlayingItem(currentItem)) {
                            if (podcastPlayer.isPlaying()) {
                                podcastPlayer.pause();
                            } else {
                                podcastPlayer.start();
                            }
                        } else {
                            podcastPlayer.play(getActivity(), currentItem);
                        }
                    }
                });

        AndroidObservable.bindFragment(this, scrollView.scroll())
                .subscribe(new Action1<ObservableScrollView.Scroller>() {
                    @Override
                    public void call(ObservableScrollView.Scroller scroller) {
                        int height = headerLayout.getHeight();
                        ((FeedDetailActivity) getActivity()).updateToolbar(height, scroller.y);
                    }
                });

        AndroidObservable.bindFragment(this, PodcastPlayerSubject.receivePlayed())
                .subscribe(new Action1<Item>() {
                    @Override
                    public void call(Item item) {
                        if (item.equals(currentItem)) {
                            showPause();
                        }
                    }
                });
        AndroidObservable.bindFragment(this, PodcastPlayerSubject.receivePaused())
                .subscribe(new Action1<Item>() {
                    @Override
                    public void call(Item item) {
                        if (isAdded() && item.equals(currentItem)) {
                            showPlay();
                        }
                    }
                });
        AndroidObservable.bindFragment(this, PodcastPlayerSubject.receiveStopped())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (isAdded()) {
                            showPlay();
                        }
                    }
                });
        AndroidObservable.bindFragment(this, PodcastPlayerSubject.receiveTicked())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer position) {
                        updateElapsedTime(position);
                    }
                });
    }

    private void setupPlayer() {
        final Item currentItem = viewModel.getItem();

        if (podcastPlayer.isPlaying() && podcastPlayer.isPlayingItem(currentItem)) {
            // TODO sync seekbar and duration
            showPause();
        } else {
            showPlay();
        }

        updateElapsedTime(0);
        playerDurationTextView.setText(currentItem.getDuration());
    }

    private void showPlay() {
        pauseButton.setVisibility(View.GONE);
        playButton.setVisibility(View.VISIBLE);
    }

    private void showPause() {
        playButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
    }

    private void updateElapsedTime(int position) {
        playerElapsedTimeTextView.setText(StringUtils.seekPositionToString(position));
        playerSeekBar.setProgress(position);
    }
}
