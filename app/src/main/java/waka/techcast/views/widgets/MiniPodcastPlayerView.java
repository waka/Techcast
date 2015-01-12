package waka.techcast.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import waka.techcast.R;
import waka.techcast.internal.di.Injector;
import waka.techcast.media.PodcastPlayer;
import waka.techcast.models.Item;

public class MiniPodcastPlayerView extends FrameLayout {
    public interface Callback {
        public void onClick(Item item);
    }

    private Callback callback;

    @Inject
    PodcastPlayer podcastPlayer;

    @InjectView(R.id.player_play_and_pause)
    CheckBox playAndPauseCheckbox;

    @InjectView(R.id.player_stop)
    ImageView stopButton;

    @InjectView(R.id.player_title)
    TextView titleTextView;

    public MiniPodcastPlayerView(Context context, AttributeSet attributes) {
        super(context, attributes);
        setup();
        setupViews();
    }

    private void setup() {
        View view = View.inflate(getContext(), R.layout.view_mini_podcast_player, null);
        addView(view);

        Injector.get().inject(this);
        ButterKnife.inject(this);
    }

    private void setupViews() {
        getRootView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.onClick(podcastPlayer.getPlayingItem());
                }
            }
        });

        if (podcastPlayer.isPlaying()) {
            playAndPauseCheckbox.setChecked(true);
        } else {
            playAndPauseCheckbox.setChecked(false);
        }
        playAndPauseCheckbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playAndPauseCheckbox.isChecked()) {
                    play();
                } else {
                    pause();
                }
            }
        });

        stopButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                stop();
            }
        });
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private void play() {
        if (!podcastPlayer.isPlaying()) {
            podcastPlayer.start();
        }
    }

    private void pause() {
        if (podcastPlayer.isPlaying()) {
            podcastPlayer.pause();
        }
    }

    private void stop() {
        if (podcastPlayer.isPlaying()) {
            podcastPlayer.stop();
        }
        hide();
    }

    public void show() {
        setVisibility(View.VISIBLE);
        titleTextView.setText(podcastPlayer.getPlayingItem().getTitle());
        titleTextView.setSelected(true);
        titleTextView.requestFocus();
    }

    public void hide() {
        setVisibility(View.GONE);
    }

    public void setPlayAndPauseCheckbox(boolean isPlay) {
        playAndPauseCheckbox.setChecked(isPlay);
    }
}
