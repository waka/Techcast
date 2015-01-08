package waka.techcast.internal.utils;

import android.content.Context;

import waka.techcast.models.Item;
import waka.techcast.services.DownloadService;
import waka.techcast.stores.FileStore;
import waka.techcast.views.widgets.MaterialDialog;

public class DialogUtils {
    public static MaterialDialog createDownloadDialog(final Context context, final Item item) {
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
            }

            @Override
            public void onCancelClick() {}
        });
        return dialog;
    }

    public static MaterialDialog createDownloadCancelDialog(final Context context, final Item item) {
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
            }

            @Override
            public void onCancelClick() {}
        });
        return dialog;
    }

    public static MaterialDialog createDownloadClearDialog(final Context context, final Item item) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context, item.getTitle());
        MaterialDialog dialog = builder
                .positiveText("クリア")
                .negativeText("キャンセル")
                .content(item.getSubTitle())
                .build();
        dialog.setOnClickListener(new MaterialDialog.OnClickListener() {
            @Override
            public void onConfirmClick() {
                FileStore.delete(context, item);
            }

            @Override
            public void onCancelClick() {}
        });
        return dialog;
    }
}
