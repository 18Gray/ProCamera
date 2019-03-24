package com.eighteengray.imageprocesslibrary.cvdemo.camera;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.eighteengray.imageprocesslibrary.R;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class DisplayModeActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{
    private JavaCameraView mOpenCvCameraView;
    private String TAG = "DisplayModeActivity";
    private CascadeClassifier faceDetector;
    private int option = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_mode_activity);

        //for 6.0 and 6.0 above, apply permission
        if(Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.full_screen_camera_id);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.enableFpsMeter();

        // 后置摄像头开启预览
        mOpenCvCameraView.setCameraIndex(1);
        mOpenCvCameraView.enableView();

        try {
            initFaceDetector();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void initFaceDetector() throws IOException {
        System.loadLibrary("face_detection");
        InputStream input = getResources().openRawResource(R.raw.lbpcascade_frontalface);
        File cascadeDir = this.getDir("cascade", Context.MODE_PRIVATE);
        File file = new File(cascadeDir.getAbsoluteFile(), "lbpcascade_frontalface.xml");
        FileOutputStream output = new FileOutputStream(file);
        byte[] buff = new byte[1024];
        int len = 0;
        while((len = input.read(buff)) != -1) {
            output.write(buff, 0, len);
        }
        input.close();
        output.close();
//        ImageProcessJni.initLoad(file.getAbsolutePath());
        //faceDetector = new CascadeClassifier(file.getAbsolutePath());
        file.delete();
        cascadeDir.delete();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.invert) {
            option = 1;

        } else if (id == R.id.edge) {
            option = 2;

        } else if (id == R.id.sobel) {
            option = 3;

        } else if (id == R.id.boxBlur) {
            option = 4;

        } else if (id == R.id.faceDetection) {
            option = 5;

        } else {
            option = 0;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat frame = inputFrame.rgba();
        Core.flip(frame, frame, 1);
        if(this.getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            Core.rotate(frame, frame, Core.ROTATE_90_CLOCKWISE);
        }
        process(frame);
        return frame;
    }

    private void process(Mat frame) {
        switch (option){
            case 1:
                Core.bitwise_not(frame, frame);
                break;
            case 2:
                Mat edges = new Mat();
                Imgproc.Canny(frame, edges, 100, 200, 3, false);
                Mat result = Mat.zeros(frame.size(), frame.type());
                frame.copyTo(result, edges);
                result.copyTo(frame);
                edges.release();
                result.release();
                break;
            case 3:
                Mat gradx = new Mat();
                Imgproc.Sobel(frame, gradx, CvType.CV_32F, 1, 0);
                Core.convertScaleAbs(gradx, gradx);
                gradx.copyTo(frame);
                gradx.release();
                break;
            case 4:
                Mat temp = new Mat();
                Imgproc.blur(frame, temp, new Size(15, 15));
                temp.copyTo(frame);
                temp.release();
                break;
            case 5:
                //haarFaceDetection(frame);
//               ImageProcessJni.faceDetection(frame.getNativeObjAddr());
                break;
            default:
                break;
        }
    }

    private void haarFaceDetection(Mat frame) {

        Mat gray = new Mat();
        MatOfRect faces = new MatOfRect();
        Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGRA2GRAY);
        faceDetector.detectMultiScale(gray, faces, 1.1, 1, 0, new Size(30, 30), new Size(300, 300));
        List<Rect> faceList = faces.toList();
        if(faceList.size() > 0) {
            for(Rect rect : faceList) {
                Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0,0,255), 2, 8, 0);
            }
        }
        gray.release();
        faces.release();
    }


}
