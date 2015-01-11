package waka.techcast.views.widgets;

import android.app.Dialog;
import android.content.Context;

import waka.techcast.R;

public class ProgressDialog extends Dialog {
    public static ProgressDialog show(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.show();
        return dialog;
    }

    public ProgressDialog(Context context) {
        this(context, 0);
    }

    public ProgressDialog(Context context, int theme) {
        super(context, R.style.AppTheme_ProgressDialog);
        setContentView(R.layout.dialog_progress);
    }

    protected ProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
