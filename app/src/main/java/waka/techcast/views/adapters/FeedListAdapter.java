package waka.techcast.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import waka.techcast.R;
import waka.techcast.internal.utils.StringUtils;
import waka.techcast.models.Item;

public class FeedListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Item> items = new ArrayList<>();

    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_feed, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Item item = items.get(position);
        ((ItemViewHolder) holder).bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.item_title)
        TextView titleTextView;
        @InjectView(R.id.item_sub_title)
        TextView subTitleTextView;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void bind(final Item item) {
            titleTextView.setText(item.getTitle());

            String subTitle = StringUtils.omitArticle(StringUtils.fromHtml(item.getSubTitle()));
            subTitleTextView.setText(subTitle);
        }
    }
}
