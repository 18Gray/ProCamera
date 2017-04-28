package com.eighteengray.procamera.widget.dialogfragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import com.eighteengray.procamera.dataevent.CameraConfigure;
import com.eighteengray.procamera.R;
import com.eighteengray.procameralibrary.common.Constants;

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


    //Hdr的弹出框
    public static PopupWindow createHdrPopupWindow(Context context)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dialogfragment_hdrselect, null);

        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //点击空白处消失
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));

        //点击事件
        view.findViewById(R.id.iv_hdr_on).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CameraConfigure.Hdr hdr1 = new CameraConfigure.Hdr();
                hdr1.setHdr(Constants.HDR_ON);
                EventBus.getDefault().post(hdr1);
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.iv_hdr_off).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CameraConfigure.Hdr hdr2 = new CameraConfigure.Hdr();
                hdr2.setHdr(Constants.HDR_OFF);
                EventBus.getDefault().post(hdr2);
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

}
