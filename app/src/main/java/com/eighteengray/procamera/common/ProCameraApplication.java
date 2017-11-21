package com.eighteengray.procamera.common;

import android.app.Application;
import com.squareup.leakcanary.LeakCanary;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ProCameraApplication extends Application
{
    private static ProCameraApplication context;
    public static ExecutorService executorService;

    @Override
    public void onCreate()
    {
        super.onCreate();
        if(LeakCanary.isInAnalyzerProcess(this))
        {
            return;
        }
        LeakCanary.install(this);
        context = this;
        executorService = Executors.newSingleThreadExecutor();

        //异常上报
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(context);
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
