package com.eighteengray.procamera.widget.dialogfragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.eighteengray.cardlibrary.widget.RecyclerLayout;
import com.eighteengray.commonutillibrary.DimenUtil;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.common.GenerateDataUtils;
import com.eighteengray.procamera.imageprocess.bean.VerticalRecyclerItem;
import com.eighteengray.procameralibrary.common.Constants;
import com.eighteengray.procameralibrary.dataevent.BitmapProcess;
import com.eighteengray.procameralibrary.dataevent.CameraConfigure;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;



public class PopupWindowFactory {
    //Flash的弹出框
    public static PopupWindow createFlashPopupWindow(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dialogfragment_flashselect, null);

        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //点击空白处消失
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));

        //点击事件
        view.findViewById(R.id.iv_flash_auto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraConfigure.Flash flash1 = new CameraConfigure.Flash();
                flash1.setFlash(Constants.FLASH_AUTO);
                EventBus.getDefault().post(flash1);
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.iv_flash_on).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraConfigure.Flash flash2 = new CameraConfigure.Flash();
                flash2.setFlash(Constants.FLASH_ON);
                EventBus.getDefault().post(flash2);
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.iv_flash_off).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraConfigure.Flash flash3 = new CameraConfigure.Flash();
                flash3.setFlash(Constants.FLASH_OFF);
                EventBus.getDefault().post(flash3);
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.iv_flash_flare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraConfigure.Flash flash4 = new CameraConfigure.Flash();
                flash4.setFlash(Constants.FLASH_FLARE);
                EventBus.getDefault().post(flash4);
                popupWindow.dismiss();
            }
        });

        return popupWindow;
    }

    //Ratio的弹出框
    public static PopupWindow createRatioPopupWindow(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dialogfragment_ratioselect, null);

        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //点击空白处消失
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));

        //点击事件
        view.findViewById(R.id.ll_ratio_normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CameraConfigure.Ratio ratio1 = new CameraConfigure.Ratio();
                ratio1.setRatio(Constants.RATIO_NORMAL);
                EventBus.getDefault().post(ratio1);
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.ll_ratio_square).setOnClickListener(new View.OnClickListener() {
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
            public void onClick(View v) {
                CameraConfigure.Ratio ratio3 = new CameraConfigure.Ratio();
                ratio3.setRatio(Constants.RATIO_4V3);
                EventBus.getDefault().post(ratio3);
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.ll_ratio_16v9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraConfigure.Ratio ratio4 = new CameraConfigure.Ratio();
                ratio4.setRatio(Constants.RATIO_16V9);
                EventBus.getDefault().post(ratio4);
                popupWindow.dismiss();
            }
        });

        return popupWindow;
    }


    // 滤镜弹出框
    public static PopupWindow createFilterPopupWindow(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.layout_common_recycler, null);
        RecyclerLayout recycler_layout = (RecyclerLayout) view.findViewById(R.id.recycler_layout);
        List<VerticalRecyclerItem> verticalRecyclerItemArrayList = GenerateDataUtils.generateFilterMenuList();
        recycler_layout.setLayoutManagerNum(2);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) recycler_layout.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = DimenUtil.dp2px(context, 120);
        recycler_layout.setLayoutParams(layoutParams);
        recycler_layout.showRecyclerView(GenerateDataUtils.generateDataBeanList(8, verticalRecyclerItemArrayList), Constants.viewModelPackage);

        PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));
        return popupWindow;
    }

    // 处理弹出框
    public static PopupWindow createProcessPopupWindow(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.layout_common_recycler, null);
        RecyclerLayout recycler_layout = (RecyclerLayout) view.findViewById(R.id.recycler_layout);
        List<VerticalRecyclerItem> verticalRecyclerItemArrayList = GenerateDataUtils.generateProcessToolsMenuList();
        recycler_layout.setLayoutManagerNum(3);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) recycler_layout.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = DimenUtil.dp2px(context, 400);
        recycler_layout.setLayoutParams(layoutParams);
        recycler_layout.showRecyclerView(GenerateDataUtils.generateDataBeanList(8, verticalRecyclerItemArrayList), Constants.viewModelPackage);

        PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));
        return popupWindow;
    }


    //图像处理中，对比度弹出框
    public static PopupWindow createContrastPopupWindow(final Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View popView = inflater.inflate(R.layout.popupwindow_strength, null);

        final SeekBar seekBarSaturation = (SeekBar) popView.findViewById(R.id.seekBarSaturation);
        final SeekBar seekBarDuibidu = (SeekBar) popView.findViewById(R.id.seekBarDuibidu);
        final SeekBar seekBarLight = (SeekBar) popView.findViewById(R.id.seekBarLight);

        TextView tv_baohedu_strength = (TextView) popView.findViewById(R.id.tv_baohedu_strength);
        TextView tv_duibidu_strength = (TextView) popView.findViewById(R.id.tv_duibidu_strength);
        TextView tv_liangdu_strength = (TextView) popView.findViewById(R.id.tv_liangdu_strength);

        final List<TextView> textViewList = new ArrayList<>();
        textViewList.add(tv_baohedu_strength);
        textViewList.add(tv_duibidu_strength);
        textViewList.add(tv_liangdu_strength);

        tv_baohedu_strength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBarSaturation.setVisibility(View.VISIBLE);
                seekBarDuibidu.setVisibility(View.GONE);
                seekBarLight.setVisibility(View.GONE);
                updateTextView(textViewList, 0, context);
            }
        });

        tv_duibidu_strength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBarSaturation.setVisibility(View.GONE);
                seekBarDuibidu.setVisibility(View.VISIBLE);
                seekBarLight.setVisibility(View.GONE);
                updateTextView(textViewList, 1, context);
            }
        });

        tv_liangdu_strength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBarSaturation.setVisibility(View.GONE);
                seekBarDuibidu.setVisibility(View.GONE);
                seekBarLight.setVisibility(View.VISIBLE);
                updateTextView(textViewList, 2, context);
            }
        });

        seekBarSaturation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                BitmapProcess.ContrastEvent contrastEvent = new BitmapProcess.ContrastEvent();
                contrastEvent.setSeekBarNum(0);
                contrastEvent.setProgress(progress);
                EventBus.getDefault().post(contrastEvent);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarDuibidu.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                BitmapProcess.ContrastEvent contrastEvent = new BitmapProcess.ContrastEvent();
                contrastEvent.setSeekBarNum(1);
                contrastEvent.setProgress(progress);
                EventBus.getDefault().post(contrastEvent);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarLight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                BitmapProcess.ContrastEvent contrastEvent = new BitmapProcess.ContrastEvent();
                contrastEvent.setSeekBarNum(2);
                contrastEvent.setProgress(progress);
                EventBus.getDefault().post(contrastEvent);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        popView.bringToFront();
        final PopupWindow popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        return popupWindow;
    }

    private static void updateTextView(List<TextView>textViewList, int current, Context context) {
        for(int i=0;i<textViewList.size();i++) {
            TextView currentTextView = textViewList.get(i);
            if(current == i) {
                currentTextView.setTextColor(context.getResources().getColor(R.color.accent));
            }
            else {
                currentTextView.setTextColor(context.getResources().getColor(R.color.text));
            }
        }
    }



}
