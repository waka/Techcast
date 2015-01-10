package waka.techcast.internal.utils;

import android.content.Context;

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
                .positiveText("ダウンロード")
                .negativeText("キャンセル")
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
                .positiveText("ダウンロード中止")
                .negativeText("キャンセル")
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
                .positiveText("キャッシュ削除")
                .negativeText("キャンセル")
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
}
