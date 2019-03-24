package com.eighteengray.imageprocesslibrary;


import com.eighteengray.commonlibrary.BaseActivity;
import org.opencv.android.OpenCVLoader;



public abstract class ImageProcessBaseActivity extends BaseActivity {
    static {
        System.loadLibrary("opencv_java3");
    }


    @Override
    protected void onResume() {
        super.onResume();
        OpenCVLoader.initDebug();
    }

}
