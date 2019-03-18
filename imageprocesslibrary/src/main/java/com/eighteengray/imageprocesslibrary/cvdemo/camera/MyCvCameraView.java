package com.eighteengray.imageprocesslibrary.cvdemo.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;

import org.opencv.android.JavaCameraView;

import java.io.FileOutputStream;

/**
 * Created by zhigang on 2017/12/3.
 */

public class MyCvCameraView extends JavaCameraView implements Camera.PictureCallback {
    private String TAG = "MyCvCameraView";
    private String imageFileName;
    public MyCvCameraView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void takePicture(final String fileName) {
        Log.i(TAG, "Taking picture");
        this.imageFileName = fileName;
        System.gc(); // bug fix
        mCamera.setPreviewCallback(null);
        mCamera.takePicture(null, null, this);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.i(TAG, "Saving a bitmap to file");
        mCamera.startPreview();
        mCamera.setPreviewCallback(this);

        // Write the image in a file (in jpeg format)
        try {
            FileOutputStream fos = new FileOutputStream(imageFileName);
            fos.write(data);
            fos.close();

        } catch (java.io.IOException e) {
            Log.e(TAG, "Exception in photoCallback", e);
        }
    }
}
