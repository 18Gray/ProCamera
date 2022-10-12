package com.eighteengray.procamera.widget.dialogfragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.eighteengray.procamera.databinding.DialogfragmentModeselectBinding;
import com.eighteengray.procameralibrary.common.Constants;
import com.eighteengray.procameralibrary.dataevent.ModeSelectEvent;
import org.greenrobot.eventbus.EventBus;

import androidx.fragment.app.DialogFragment;


public class ModeSelectDialogFragment extends DialogFragment {
    DialogfragmentModeselectBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //全屏显示
        Window window = getDialog().getWindow();
        binding = DialogfragmentModeselectBinding.inflate(inflater);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(-1, -2);

        binding.llCameraModeselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModeSelectEvent modeSelectEvent1 = new ModeSelectEvent();
                modeSelectEvent1.setMode(Constants.MODE_CAMERA);
                EventBus.getDefault().post(modeSelectEvent1);
                dismiss();
            }
        });

        binding.llVideoModeselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModeSelectEvent modeSelectEvent2 = new ModeSelectEvent();
                modeSelectEvent2.setMode(Constants.MODE_RECORD);
                EventBus.getDefault().post(modeSelectEvent2);
                dismiss();
            }
        });

        return binding.getRoot();
    }


}
