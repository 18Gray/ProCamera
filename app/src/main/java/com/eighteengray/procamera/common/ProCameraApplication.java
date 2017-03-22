package com.eighteengray.procamera.common;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.eighteengray.procamera.widget.AllDialog;


public class ProCameraApplication extends Application
{
    private static ProCameraApplication context;


    @Override
    public void onCreate()
    {
        super.onCreate();
        context = this;
    }


    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        System.gc();
    }


    public static synchronized ProCameraApplication getInstance()
    {
        return context;
    }


}
