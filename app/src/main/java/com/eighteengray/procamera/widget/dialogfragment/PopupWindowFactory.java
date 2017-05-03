package com.eighteengray.procamera.widget.dialogfragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import com.eighteengray.procamera.R;
import com.eighteengray.procameralibrary.common.Constants;
import com.eighteengray.procameralibrary.dataevent.CameraConfigure;
import org.greenrobot.eventbus.EventBus;



public class PopupWindowFactory
{
    //Flash的弹出框
    public static PopupWindow createFlashPopupWindow(Context context)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dialogfragment_flashselect, null);

        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //点击空白处消失
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));

        //点击事件
        view.findViewById(R.id.iv_flash_auto).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CameraConfigure.Flash flash1 = new CameraConfigure.Flash();
                flash1.setFlash(Constants.FLASH_AUTO);
                EventBus.getDefault().post(flash1);
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.iv_flash_on).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CameraConfigure.Flash flash2 = new CameraConfigure.Flash();
                flash2.setFlash(Constants.FLASH_ON);
                EventBus.getDefault().post(flash2);
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.iv_flash_off).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CameraConfigure.Flash flash3 = new CameraConfigure.Flash();
                flash3.setFlash(Constants.FLASH_OFF);
                EventBus.getDefault().post(flash3);
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.iv_flash_flare).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CameraConfigure.Flash flash4 = new CameraConfigure.Flash();
                flash4.setFlash(Constants.FLASH_FLARE);
                EventBus.getDefault().post(flash4);
                popupWindow.dismiss();
            }
        });

        return popupWindow;
    }



    //Ratio的弹出框
    public static PopupWindow createRatioPopupWindow(Context context)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dialogfragment_ratioselect, null);

        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //点击空白处消失
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));

        //点击事件
        view.findViewById(R.id.ll_ratio_normal).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CameraConfigure.Ratio ratio1 = new CameraConfigure.Ratio();
                ratio1.setRatio(Constants.RATIO_NORMAL);
                EventBus.getDefault().post(ratio1);
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.ll_ratio_square).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CameraConfigure.Ratio ratio2 = new CameraConfigure.Ratio();
                ratio2.setRatio(Constants.RATIO_SQUARE);
                EventBus.getDefault().post(ratio2);
                popupWindow. dismiss();
            }
        });
        view.findViewById(R.id.ll_ratio_4v3).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CameraConfigure.Ratio ratio3 = new CameraConfigure.Ratio();
                ratio3.setRatio(Constants.RATIO_4V3);
                EventBus.getDefault().post(ratio3);
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.ll_ratio_16v9).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CameraConfigure.Ratio ratio4 = new CameraConfigure.Ratio();
                ratio4.setRatio(Constants.RATIO_16V9);
                EventBus.getDefault().post(ratio4);
                popupWindow.dismiss();
            }
        });

        return popupWindow;
    }



    //图像处理中，对比度弹出框
    public static PopupWindow createContrastPopupWindow(Context context)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View popView = inflater.inflate(R.layout.popupwindow_strength, null);
        /*IncreaseProcessImage increaseProcessImage = new IncreaseProcessImage(currentBitmap);
        final SeekBar seekBarSaturation = (SeekBar) popView.findViewById(R.id.seekBarSaturation);
        final SeekBar seekBarDuibidu = (SeekBar) popView.findViewById(R.id.seekBarDuibidu);
        final SeekBar seekBarLight = (SeekBar) popView.findViewById(R.id.seekBarLight);
        seekBarSaturation.setOnSeekBarChangeListener(this);
        seekBarDuibidu.setOnSeekBarChangeListener(this);
        seekBarLight.setOnSeekBarChangeListener(this);

        TextView tv_baohedu_strength = (TextView) popView.findViewById(R.id.tv_baohedu_strength);
        TextView tv_duibidu_strength = (TextView) popView.findViewById(R.id.tv_duibidu_strength);
        TextView tv_liangdu_strength = (TextView) popView.findViewById(R.id.tv_liangdu_strength);

        tv_baohedu_strength.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                seekBarSaturation.setVisibility(View.VISIBLE);
                seekBarDuibidu.setVisibility(View.GONE);
                seekBarLight.setVisibility(View.GONE);
            }
        });

        tv_duibidu_strength.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                seekBarSaturation.setVisibility(View.GONE);
                seekBarDuibidu.setVisibility(View.VISIBLE);
                seekBarLight.setVisibility(View.GONE);
            }
        });

        tv_liangdu_strength.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                seekBarSaturation.setVisibility(View.GONE);
                seekBarDuibidu.setVisibility(View.GONE);
                seekBarLight.setVisibility(View.VISIBLE);
            }
        });*/

        popView.bringToFront();
        final PopupWindow popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setTouchInterceptor(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE)
                {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        return popupWindow;
    }

}
