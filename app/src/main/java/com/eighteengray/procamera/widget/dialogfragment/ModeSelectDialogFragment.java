package com.eighteengray.procamera.widget.dialogfragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import com.eighteengray.procamera.dataevent.ModeSelectEvent;
import com.eighteengray.procamera.R;
import com.eighteengray.procameralibrary.common.Constants;

import org.greenrobot.eventbus.EventBus;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class ModeSelectDialogFragment extends DialogFragment
{
    View view;

    @BindView(R.id.ll_camera_modeselect)
    LinearLayout ll_camera_modeselect;
    @BindView(R.id.ll_video_modeselect)
    LinearLayout ll_video_modeselect;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //全屏显示
        Window window = getDialog().getWindow();
        view = inflater.inflate(R.layout.dialogfragment_modeselect,  ((ViewGroup) window.findViewById(android.R.id.content)), false);//需要用android.R.id.content这个view
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(-1, -2);
        ButterKnife.bind(this, view);

        return view;
    }


    @OnClick({R.id.ll_camera_modeselect, R.id.ll_video_modeselect})
    public void click(View view)
    {
        switch (view.getId())
        {
            case R.id.ll_camera_modeselect:
                ModeSelectEvent modeSelectEvent1 = new ModeSelectEvent();
                modeSelectEvent1.setMode(Constants.MODE_CAMERA);
                EventBus.getDefault().post(modeSelectEvent1);
                dismiss();
                break;

            case R.id.ll_video_modeselect:
                ModeSelectEvent modeSelectEvent2 = new ModeSelectEvent();
                modeSelectEvent2.setMode(Constants.MODE_RECORD);
                EventBus.getDefault().post(modeSelectEvent2);
                dismiss();
                break;
        }
    }



}
