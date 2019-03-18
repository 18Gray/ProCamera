package com.eighteengray.imageprocesslibrary.cvdemo.camera;

import android.Manifest;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.Toast;

import com.eighteengray.imageprocesslibrary.R;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.io.File;


public class CameraViewActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2, View.OnClickListener, View.OnTouchListener {
    private MyCvCameraView mOpenCvCameraView;
    private OrientationEventListener mOrientationListener;
    private static int cameraIndex=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_view_activity);

        //for 6.0 and 6.0 above, apply permission
        if(Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mOpenCvCameraView = (MyCvCameraView) findViewById(R.id.cv_camera_id);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setOnTouchListener(this);

        RadioButton backOption = (RadioButton)this.findViewById(R.id.backCameraBtn);
        RadioButton frontOption = (RadioButton)this.findViewById(R.id.frontCameraBtn);
        backOption.setSelected(true);

        backOption.setOnClickListener(this);
        frontOption.setOnClickListener(this);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        Log.i("CVCamera", "start camera view now...");
    }

    @Override
    public void onCameraViewStopped() {
        Log.i("CVCamera", "stop camera view...");
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT) {
            Log.i("CVCamera", "竖屏显示...");
        }
        Mat frame = inputFrame.rgba();
        if(cameraIndex == 0) {
            return frame;
        } else {
            Core.flip(frame, frame, 1);
            return frame;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.frontCameraBtn) {
            cameraIndex = 1;
        } else if(id == R.id.backCameraBtn) {
            cameraIndex = 0;
        }
        mOpenCvCameraView.setCameraIndex(cameraIndex);
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
        mOpenCvCameraView.enableView();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onResume() {
        super.onResume();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.setCameraIndex(cameraIndex);
            mOpenCvCameraView.enableFpsMeter();
            mOpenCvCameraView.enableView();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.i("TAKE_PICTURE","onTouch event");
        File filedir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "myOcrImages");
        String name = String.valueOf(System.currentTimeMillis()) + "_ocr.jpg";
        File tempFile = new File(filedir.getAbsoluteFile()+File.separator, name);
        String fileName = tempFile.getAbsolutePath();
        Log.i("TAKE_PICTURE",fileName);
        mOpenCvCameraView.takePicture(fileName);
        Toast.makeText(this, fileName + " saved", Toast.LENGTH_SHORT).show();
        return false;
    }
}
