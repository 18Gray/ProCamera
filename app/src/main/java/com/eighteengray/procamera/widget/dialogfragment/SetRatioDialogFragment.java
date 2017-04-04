package com.eighteengray.procamera.widget.dialogfragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v13.app.FragmentCompat;

import com.eighteengray.procamera.R;


public class SetRatioDialogFragment extends DialogFragment
{
    private static final String PERMISSION = "permission";
    private static final String REQUESTCODE = "requestCode";

    public static SetRatioDialogFragment newInstance(String permission, int requestCode)
    {
        SetRatioDialogFragment dialog = new SetRatioDialogFragment();
        Bundle args = new Bundle();
        args.putString(PERMISSION, permission);
        args.putInt(REQUESTCODE, requestCode);
        dialog.setArguments(args);
        return dialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Fragment parent = getParentFragment();
        final String permission = getArguments().getString(PERMISSION);
        final int requestCode = getArguments().getInt(REQUESTCODE);
        return new AlertDialog.Builder(getActivity())
                .setMessage(R.string.pgcommon_share)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        FragmentCompat.requestPermissions(parent,
                                new String[]{permission},
                                requestCode);
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
