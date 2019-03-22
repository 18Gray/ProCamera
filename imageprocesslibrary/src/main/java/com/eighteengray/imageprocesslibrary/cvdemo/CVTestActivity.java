package com.eighteengray.imageprocesslibrary.cvdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.eighteengray.imageprocesslibrary.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

public class CVTestActivity extends Activity implements  Handler.Callback {
    ImageView image_view;
    Bitmap bitmap;
    Handler handler = new Handler();
    Mat testMat;

    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("opencv_java");
    }


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            bitmap = ImageProcessOpenCv.bitmap2Mat();
                            handler.sendEmptyMessage(101);
                        }
                    });
                    thread.start();

                } break;

                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cvtest);
        image_view = (ImageView) findViewById(R.id.image_view);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case 101:
                image_view.setImageBitmap(bitmap);
                break;
        }
        return false;
    }
}
