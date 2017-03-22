package com.eighteengray.procamera.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v13.app.FragmentCompat;
import com.eighteengray.procamera.R;




public class ConfirmationDialogFragment extends DialogFragment
{
    public static final int REQUEST_CAMERA_PERMISSION = 1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Fragment parent = getParentFragment();
        return new AlertDialog.Builder(getActivity())
                .setMessage(R.string.pgcommon_share)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        FragmentCompat.requestPermissions(parent,
                                new String[]{Manifest.permission.CAMERA},
                                REQUEST_CAMERA_PERMISSION);
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
