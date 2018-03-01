package com.eighteengray.procameralibrary.permission;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import com.eighteengray.procameralibrary.R;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import java.util.List;



public final class DefaultRationale implements Rationale {

    @Override
    public void showRationale(Context context, List<String> permissions, final RequestExecutor executor) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = context.getString(R.string.message_permission_rationale, TextUtils.join("\n", permissionNames));

        AlertDialog.newBuilder(context)
                .setCancelable(false)
                .setTitle("Tips")
                .setMessage(message)
                .setPositiveButton("Resume", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executor.execute();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executor.cancel();
                    }
                })
                .show();
    }
}