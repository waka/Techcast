package waka.techcast.views.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

    private Button confirmView;
    private Button cancelView;

    private MaterialDialog(Builder builder) {
        super(new ContextThemeWrapper(builder.context, R.style.AppTheme_Dialog));

        this.context = builder.context;
        this.title   = builder.title;
        this.content = builder.content;
        this.positiveText = builder.positiveText;
        this.negativeText = builder.negativeText;
        this.typeface = builder.typeface;

        setupViews();
        setListeners();
    }

    private void setupViews() {
        View rootView = LayoutInflater.from(context).inflate(R.layout.dialog, null);

        TextView titleView = (TextView) rootView.findViewById(R.id.dialog_title);
        titleView.setTypeface(typeface);
        titleView.setText(title);

        TextView contentView = (TextView) rootView.findViewById(R.id.dialog_content);
        contentView.setTypeface(typeface);
        contentView.setText(content);

        confirmView = (Button) rootView.findViewById(R.id.dialog_confirm);
        confirmView.setTypeface(typeface);
        confirmView.setText(positiveText);

        cancelView  = (Button) rootView.findViewById(R.id.dialog_cancel);
        cancelView.setTypeface(typeface);
        cancelView.setText(negativeText);

        super.setView(rootView);
    }

    private void setListeners() {
        // Set the listener that handles positive button clicks.
        confirmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onConfirmClick();
                }
                dismiss();
            }
        });

        // Set the listener that handles negative button clicks.
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onCancelClick();
                }
                dismiss();
            }
        });
    }

    public MaterialDialog setOnClickListener(OnClickListener listener) {
        this.listener = listener;
        return this;
    }


    public static class Builder {
        private final Context context;
        private final String title;
        private String positiveText = "OK";
        private String negativeText = "Cancel";
        private String content = "";
        private Typeface typeface;

        public Builder(Context context, String title) {
            this.context = context;
            this.title = title;

            // Load typeface from assets to be used.
            typeface = Typeface.createFromAsset(this.context.getResources().getAssets(), "Roboto-Medium.ttf");
        }

        public Builder(Context context, int titleResId) {
            this(context, context.getString(titleResId));
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder positiveText(String positiveText) {
            this.positiveText = positiveText;
            return this;
        }

        public Builder positiveText(int positiveTextResId) {
            this.positiveText = context.getString(positiveTextResId);
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
