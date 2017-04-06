package com.eighteengray.procamera.widget.dialogfragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;


public class ExplationDialogFragment extends DialogFragment
{
    private static final String PERMISSIONS = "permissions";
    private static final String REQUESTCODE = "requestCode";


    public static ExplationDialogFragment newInstance(String[] permissions, int requestCode)
    {
        ExplationDialogFragment dialog = new ExplationDialogFragment();
        Bundle args = new Bundle();
        args.putStringArray(PERMISSIONS, permissions);
        args.putInt(REQUESTCODE, requestCode);
        dialog.setArguments(args);
        return dialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Fragment parent = getParentFragment();
        final String[] permissions = getArguments().getStringArray(PERMISSIONS);
        final int requestCode = getArguments().getInt(REQUESTCODE);
        return new AlertDialog.Builder(getActivity())
                .setMessage("请求权限")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        ActivityCompat.requestPermissions(parent.getActivity(), permissions, requestCode);
                    }
                })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Activity activity = parent.getActivity();
                                if (activity != null)
                                {
                                    activity.finish();
                                }
                            }
                        })
                .create();
    }
}
