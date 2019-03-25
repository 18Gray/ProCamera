package com.eighteengray.imageprocesslibrary;


import android.os.Bundle;

import com.eighteengray.commonlibrary.BaseActivity;
import org.opencv.android.OpenCVLoader;



public class ImageProcessBaseActivity extends BaseActivity {
    static {
        System.loadLibrary("opencv_java3");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return super.getLayoutResId();
    }

    @Override
    protected void onResume() {
        super.onResume();
        OpenCVLoader.initDebug();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
