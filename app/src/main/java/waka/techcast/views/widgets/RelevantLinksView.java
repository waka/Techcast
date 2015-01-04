package waka.techcast.views.widgets;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import waka.techcast.R;
import waka.techcast.models.Item;
import waka.techcast.models.Link;

public class RelevantLinksView extends LinearLayout {
    public RelevantLinksView(Context context) {
        super(context);
        init();
    }

    public RelevantLinksView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RelevantLinksView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
    }

    public void addLinkView(Item item) {
        List<Link> linkList = Link.parseToLinkList(item.getDescription());
        View row;
        TextView linkTitleView;

        for (final Link link : linkList) {
            row = View.inflate(getContext(), R.layout.list_item_relevant_link, null);

            linkTitleView = (TextView) row.findViewById(R.id.link_title);
            linkTitleView.setText(link.getTitle());
            linkTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(
                            "android.intent.action.VIEW",
                            Uri.parse(link.getUrl()));
                    getContext().startActivity(intent);
                }
            });

            addView(row);
        }
    }
}
