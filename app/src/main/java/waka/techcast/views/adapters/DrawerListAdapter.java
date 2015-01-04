package waka.techcast.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import waka.techcast.R;
import waka.techcast.enums.ChannelEnum;

public class DrawerListAdapter extends ArrayAdapter<ChannelEnum> {
    private final LayoutInflater inflater;

    public DrawerListAdapter(Context context, List<ChannelEnum> channels) {
        super(context, 0, channels);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public final View getView(int position, View view, ViewGroup container) {
        if (view == null) {
            view = newView(inflater, position, container);
            if (view == null) {
                throw new IllegalStateException("newView result must not be null.");
            }
        }
        bindView(getItem(position), view);
        return view;
    }

    public View newView(LayoutInflater inflater, int position, ViewGroup container) {
        View view = inflater.inflate(R.layout.list_item_drawer, container, false);
        ChannelViewHolder holder = new ChannelViewHolder(view);
        view.setTag(holder);
        return view;
    }

    public void bindView(ChannelEnum channel, View view) {
        ChannelViewHolder holder = (ChannelViewHolder)view.getTag();
        holder.bind(channel);
    }

    static class ChannelViewHolder {
        @InjectView(R.id.item_title)
        TextView titleTextView;

        public ChannelViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

        public void bind(final ChannelEnum channel) {
            titleTextView.setText(channel.getTitle());
        }
    }
}
