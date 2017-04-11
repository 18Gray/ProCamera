package com.eighteengray.procamera.widget.dialogfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.eighteengray.procamera.DataEvent.ModeSelectEvent;
import com.eighteengray.procamera.R;
import org.greenrobot.eventbus.EventBus;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class ModeSelectDialogFragment extends DialogFragment
{
    View view;

    @BindView(R.id.tv_camera_mode)
    TextView tv_camera_mode;
    @BindView(R.id.tv_record_mode)
    TextView tv_record_mode;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if(view == null)
        {
            view = inflater.inflate(R.layout.dialogfragment_modeselect, null);
        }
        ButterKnife.bind(this, view);
        return view;
    }


    @OnClick({R.id.tv_camera_mode, R.id.tv_record_mode})
    public void click(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_camera_mode:
                ModeSelectEvent modeSelectEvent1 = new ModeSelectEvent();
                modeSelectEvent1.setMode(ModeSelectEvent.MODE_CAMERA);
                EventBus.getDefault().post(modeSelectEvent1);
                dismiss();
                break;

            case R.id.tv_record_mode:
                ModeSelectEvent modeSelectEvent2 = new ModeSelectEvent();
                modeSelectEvent2.setMode(ModeSelectEvent.MODE_RECORD);
                EventBus.getDefault().post(modeSelectEvent2);
                dismiss();
                break;
        }
    }



}
