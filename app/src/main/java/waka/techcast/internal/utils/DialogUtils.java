package waka.techcast.internal.utils;

import android.content.Context;

import waka.techcast.R;
import waka.techcast.models.Item;
import waka.techcast.services.DownloadService;
import waka.techcast.stores.ItemStore;
import waka.techcast.views.widgets.MaterialDialog;

public class DialogUtils {
    public interface DialogCallbacks {
        public void onConfirm();
    }

    public static MaterialDialog createDownloadDialog(final Context context, final Item item, final DialogCallbacks callbacks) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context, item.getTitle());
        MaterialDialog dialog = builder
                .positiveText(R.string.label_dialog_download)
                .negativeText(R.string.label_dialog_negative)
                        .content(item.getSubTitle())
                        .build();
        dialog.setOnClickListener(new MaterialDialog.OnClickListener() {
            @Override
            public void onConfirmClick() {
                DownloadService.start(context, item);
                if (callbacks != null) {
                    callbacks.onConfirm();
                }
            }

            @Override
            public void onCancelClick() {}
        });
        return dialog;
    }

    public static MaterialDialog createDownloadCancelDialog(final Context context, final Item item, final DialogCallbacks callbacks) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context, item.getTitle());
        MaterialDialog dialog = builder
                .positiveText(R.string.label_dialog_download_cancel)
                .negativeText(R.string.label_dialog_negative)
                .content(item.getSubTitle())
                .build();
        dialog.setOnClickListener(new MaterialDialog.OnClickListener() {
            @Override
            public void onConfirmClick() {
                DownloadService.cancel(item);
                if (callbacks != null) {
                    callbacks.onConfirm();
                }
            }

            @Override
            public void onCancelClick() {}
        });
        return dialog;
    }

    public static MaterialDialog createDownloadClearDialog(final Context context, final Item item, final DialogCallbacks callbacks) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context, item.getTitle());
        MaterialDialog dialog = builder
                .positiveText(R.string.label_dialog_download_clear)
                .negativeText(R.string.label_dialog_negative)
                .content(item.getSubTitle())
                .build();
        dialog.setOnClickListener(new MaterialDialog.OnClickListener() {
            @Override
            public void onConfirmClick() {
                ItemStore.delete(context, item);
                if (callbacks != null) {
                    callbacks.onConfirm();
                }
            }

            @Override
            public void onCancelClick() {}
        });
        return dialog;
    }

    public static MaterialDialog createFirstStreamingDialog(final Context context, final Item item, final DialogCallbacks callbacks) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context, item.getTitle());
        MaterialDialog dialog = builder
                .positiveText(R.string.label_dialog_streaming)
                .negativeText(R.string.label_dialog_negative)
                .content(context.getString(R.string.label_dialog_streaming_description))
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
}
