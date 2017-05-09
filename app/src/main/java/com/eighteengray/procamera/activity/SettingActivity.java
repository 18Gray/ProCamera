package com.eighteengray.procamera.activity;

import android.app.Activity;
import android.os.Bundle;
import com.eighteengray.procamera.contract.ISettingContract;


/**
 * 功能：
 * 1.图像、视频录制质量、格式调节
 * 2.九宫格、平衡器、防手抖、静音、
 * 3.实时直方图
 * 4.签名，包括日期、位置、版权、字号、颜色
 */
public class SettingActivity extends Activity implements ISettingContract.IView
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


    }
}
