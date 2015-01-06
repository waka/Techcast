package waka.techcast.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import waka.techcast.R;
import waka.techcast.internal.utils.StringUtils;
import waka.techcast.models.Item;

public class FeedListAdapter extends ArrayAdapter<Item> {
    public interface OnClickListener {
        public void onContentsClick(Item item);
        public void onDownloadClick(Item item);
        public void onPlayClick(Item item);
        public void onClearClick(Item item);
    }

    private final LayoutInflater inflater;
    private final OnClickListener listener;

    public FeedListAdapter(Context context, List<Item> items, OnClickListener listener) {
        super(context, 0, items);
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    public void setItems(List<Item> items) {
        this.addAll(items);
        notifyDataSetChanged();
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    @Override
    public final View getView(int position, View view, ViewGroup container) {
        if (view == null) {
            view = newView(inflater, container);
            if (view == null) {
                throw new IllegalStateException("newView result must not be null.");
            }
        }
        bindView(getItem(position), view);
        return view;
    }

    public View newView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.list_item_feed, container, false);

        ItemViewHolder holder = new ItemViewHolder(view, listener);
        view.setTag(holder);
        return view;
    }

    public void bindView(Item item, View view) {
        ItemViewHolder holder = (ItemViewHolder) view.getTag();
        holder.bind(item);
    }

    public static class HeaderItemViewHolder {
        private View view;

        @InjectView(R.id.logo_image)
        ImageView logoImageView;

        public HeaderItemViewHolder(View view) {
            ButterKnife.inject(this, view);
            this.view = view;
        }

        public View getView() {
            return view;
        }

        public ImageView getLogoImageView() {
            return logoImageView;
        }
    }

    static class ItemViewHolder {
        @InjectView(R.id.contents)
        LinearLayout contents;

        @InjectView(R.id.item_title)
        TextView titleTextView;
        @InjectView(R.id.item_sub_title)
        TextView subTitleTextView;

        @InjectView(R.id.play_button)
        LinearLayout playView;
        @InjectView(R.id.clear_button)
        LinearLayout clearView;
        @InjectView(R.id.download_button)
        LinearLayout downloadView;

        private final OnClickListener listener;

        public ItemViewHolder(View view, OnClickListener listener) {
            ButterKnife.inject(this, view);
            this.listener = listener;
        }

        public void bind(final Item item) {
            titleTextView.setText(item.getTitle());
            subTitleTextView.setText(StringUtils.omitArticle(StringUtils.fromHtml(item.getSubTitle())));

            contents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onContentsClick(item);
                }
            });

            playView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onPlayClick(item);
                }
            });
            clearView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClearClick(item);
                }
            });
            downloadView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onDownloadClick(item);
                }
            });
        }
    }
}
