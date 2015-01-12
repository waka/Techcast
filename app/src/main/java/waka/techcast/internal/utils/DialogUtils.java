package waka.techcast.internal.utils;

import android.content.Context;

import waka.techcast.R;
import waka.techcast.models.Item;
import waka.techcast.views.widgets.MaterialDialog;

public class DialogUtils {
    public interface DialogCallbacks {
        public void onConfirm();
    }

    public static MaterialDialog createDownloadDialog(Context context, Item item, DialogCallbacks callbacks) {
        return createDialog(
                context,
                item.getTitle(),
                R.string.label_dialog_download,
                R.string.label_dialog_negative,
                item.getSubTitle(),
                callbacks);
    }

    public static MaterialDialog createDownloadCancelDialog(Context context, Item item, DialogCallbacks callbacks) {
        return createDialog(
                context,
                item.getTitle(),
                R.string.label_dialog_download_cancel,
                R.string.label_dialog_negative,
                item.getSubTitle(),
                callbacks);
    }

    public static MaterialDialog createDownloadClearDialog(Context context, Item item, DialogCallbacks callbacks) {
        return createDialog(
                context,
                item.getTitle(),
                R.string.label_dialog_download_clear,
                R.string.label_dialog_negative,
                item.getSubTitle(),
                callbacks);
    }

    public static MaterialDialog createFirstStreamingDialog(Context context, Item item, DialogCallbacks callbacks) {
        return createDialog(
                context,
                item.getTitle(),
                R.string.label_dialog_streaming,
                R.string.label_dialog_negative,
                R.string.label_dialog_streaming_description,
                callbacks);
    }

    public static MaterialDialog createDialog(Context context, String title, int positiveResId, int negativeResId, String content, final DialogCallbacks callbacks) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context, title);
        MaterialDialog dialog = builder
                .positiveText(context.getString(positiveResId))
                .negativeText(context.getString(negativeResId))
                .content(content)
                .build();
        dialog.setOnClickListener(new MaterialDialog.OnClickListener() {
            @Override
            public void onConfirmClick() {
                if (callbacks != null) {
                    callbacks.onConfirm();
                }
            }

            @Override
            public void onCancelClick() {}
        });
        return dialog;
    }

    public static MaterialDialog createDialog(Context context, String title, int positiveResId, int negativeResId, int contentResId, final DialogCallbacks callbacks) {
        return createDialog(context, title, positiveResId, negativeResId, context.getString(contentResId), callbacks);
    }
}
