package org.lzh.framework.updatepluginlib.creator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;

import org.lzh.framework.updatepluginlib.R;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.util.SafeDialogOper;

/**
 * @author Administrator
 */
public class DefaultNeedUpdateCreator extends DialogCreator {
    @Override
    public void create(final Update update, final Activity activity) {

        if (activity == null || activity.isFinishing()) {
            Log.e("DialogCreator--->","Activity was recycled or finished,dialog shown failed!");
        }

        String updateContent = activity.getText(R.string.update_version_name)
                + ": " + update.getVersionName() + "\n\n\n"
                + update.getUpdateContent();
        AlertDialog.Builder builder =  new AlertDialog.Builder(activity)
                .setMessage(updateContent)
                .setTitle(R.string.update_title)
                .setNeutralButton(R.string.update_immediate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendDownloadRequest(update,activity);
                        SafeDialogOper.safeDismissDialog((Dialog) dialog);
                    }
                });

        if (!update.isForced()) {
            builder.setNegativeButton(R.string.update_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendUserCancel();
                    SafeDialogOper.safeDismissDialog((Dialog) dialog);
                }
            });
        }
//        builder.create().show();
        Dialog dialog = builder.create();

        if (update.isForced()) {
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }
        dialog.show();
        SafeDialogOper.safeShowDialog(dialog);
    }
}
