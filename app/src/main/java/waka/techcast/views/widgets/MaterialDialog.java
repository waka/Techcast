package waka.techcast.views.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import waka.techcast.R;

public class MaterialDialog extends AlertDialog {
    public interface OnClickListener {
        public void onConfirmClick();
        public void onCancelClick();
    }

    private OnClickListener listener;
    private Context context;
    private String title;
    private String content;
    private String positiveText;
    private String negativeText;
    private Typeface typeface;

    private View rootView;
    private View customView;
    private TextView titleView;
    private TextView contentView;
    private TextView confirmView;
    private TextView cancelView;

    private MaterialDialog(Builder builder) {
        super(new ContextThemeWrapper(builder.context, R.style.MaterialDialog));

        this.context = builder.context;
        this.title   = builder.title;
        this.content = builder.content;
        this.positiveText = builder.positiveText;
        this.negativeText = builder.negativeText;
        this.typeface = builder.typeface;

        setup();
    }

    private void setup() {
        rootView = LayoutInflater.from(context).inflate(R.layout.dialog, null);
        titleView   = rootView.findViewById(R.id.dialog_title);
        contentView = rootView.findViewById(R.id.dialog_content);
        confirmView = rootView.findViewById(R.id.dialog_confirm);
        cancelView  = rootView.findViewById(R.id.dialog_cancel);


    }


    public static class Builder {
        private final Context context;
        private final String title;
        private final String positiveText;
        private String negativeText = "";
        private String content = "";
        private Typeface typeface;

        public Builder(Context context, String title, String positiveText) {
            this.context = context;
            this.title = title;
            this.positiveText = positiveText;

            // Load typeface from assets to be used.
            typeface = Typeface.createFromAsset(this.context.getResources().getAssets(), "Roboto-Medium.ttf");
        }

        public Builder(Context context, int titleResId, int positiveTextResId) {
            this(context, context.getString(titleResId), context.getString(positiveTextResId));
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder negativeText(String negativeText) {
            this.negativeText = negativeText;
            return this;
        }

        public Builder negativeText(int negativeTextResId) {
            this.negativeText = context.getString(negativeTextResId);
            return this;
        }

        public MaterialDialog build() {
            return new MaterialDialog(this);
        }
    }
}
