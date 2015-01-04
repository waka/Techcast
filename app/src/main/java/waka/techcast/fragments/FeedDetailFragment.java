package waka.techcast.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.functions.Action1;
import waka.techcast.R;
import waka.techcast.activities.FeedDetailActivity;
import waka.techcast.internal.di.Injector;
import waka.techcast.internal.utils.StringUtils;
import waka.techcast.models.Item;
import waka.techcast.view_models.FeedDetailViewModel;
import waka.techcast.views.widgets.ObservableScrollView;
import waka.techcast.views.widgets.RelevantLinksView;

public class FeedDetailFragment extends Fragment {
    @Inject
    FeedDetailViewModel viewModel;

    @InjectView(R.id.scrollview)
    ObservableScrollView scrollView;

    @InjectView(R.id.header)
    RelativeLayout headerLayout;

    @InjectView(R.id.item_title)
    TextView titleTextView;

    @InjectView(R.id.player_duration)
    TextView playerDurationTextView;

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
        setup();
    }

    private void setup() {
        Item item = viewModel.getItem();

        titleTextView.setText(item.getTitle());
        playerDurationTextView.setText(item.getDuration());

        descriptionTextView.setText(
                StringUtils.omitArticle(StringUtils.fromHtml(item.getSubTitle())));
        relevantLinksView.addLinkView(item);

        scrollView.scroll().subscribe(new Action1<ObservableScrollView.Scroller>() {
            @Override
            public void call(ObservableScrollView.Scroller scroller) {
                int height = headerLayout.getHeight();
                ((FeedDetailActivity) getActivity()).updateToolbar(height, scroller.y);
            }
        });
    }
}
