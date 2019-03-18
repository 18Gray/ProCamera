package com.eighteengray.imageprocesslibrary.cvdemo.camera;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.eighteengray.imageprocesslibrary.R;
import com.eighteengray.imageprocesslibrary.cvdemo.FaceExtraLayer;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;



public class EyeRenderActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private int option;
    private JavaCameraView mOpenCvCameraView;
    private String TAG = "EyeRenderActivity";
//    private FaceExtraLayer mNativeDetector;
    private float mRelativeFaceSize   = 0.2f;
    private static final Scalar FACE_RECT_COLOR = new Scalar(255, 0, 0);
    private static final Scalar EYE_RECT_COLOR = new Scalar(0, 0, 255);
    private static final Scalar EYE_COLOR = new Scalar(0, 255, 255);
    private Mat k1;
    private Mat k2;
    private int mAbsoluteFaceSize   = 0;
    private CascadeClassifier eyeDetector;
    Mat leftEye_template;
    Mat rightEye_template;
    private Mat gray = new Mat();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eye_render);
        //for 6.0 and 6.0 above, apply permission
        if(Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.ten_chapter_camera_id);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.enableFpsMeter();

        // 前置摄像头开启预览
        mOpenCvCameraView.setCameraIndex(1);
        mOpenCvCameraView.enableView();
        
        // 初始化人脸跟踪器
        try {
            initDetectBasedTracker();
            initEyesDetector();
            // 缓存眼睛模板
            leftEye_template = new Mat();
            rightEye_template = new Mat();
            // 初始化结构元素
            k1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3), new Point(-1, -1));
            k2 = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(10, 10), new Point(-1, -1));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void initEyesDetector()throws IOException {
        InputStream input = getResources().openRawResource(R.raw.haarcascade_eye_tree_eyeglasses);
        File cascadeDir = this.getDir("cascade", Context.MODE_PRIVATE);
        File file = new File(cascadeDir.getAbsoluteFile(), "haarcascade_eye_tree_eyeglasses.xml");
        FileOutputStream output = new FileOutputStream(file);
        byte[] buff = new byte[1024];
        int len = 0;
        while((len = input.read(buff)) != -1) {
            output.write(buff, 0, len);
        }
        input.close();
        output.close();
        eyeDetector = new CascadeClassifier(file.getAbsolutePath());
        file.delete();
        cascadeDir.delete();
    }

    private void initDetectBasedTracker() throws IOException {
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
//        mNativeDetector = new FaceExtraLayer(file.getAbsolutePath(), 0);
        file.delete();
        cascadeDir.delete();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_charpter_ten, menu);
        return true;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

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

    public void process(Mat frame){
        if (mAbsoluteFaceSize == 0) {
            int height = frame.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
//            mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
//            mNativeDetector.start();
        }
        Imgproc.cvtColor(frame, gray, Imgproc.COLOR_RGBA2GRAY);
        Imgproc.equalizeHist(gray, gray);
        MatOfRect faces = new MatOfRect();
//        mNativeDetector.detect(gray, faces);
        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i++) {
            Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 2);
            selectEyesArea(facesArray[i], frame);
        }
    }

    private void selectEyesArea(Rect faceROI, Mat frame) {
        if(option < 2) return;
        int offy = (int)(faceROI.height * 0.35f);
        int offx = (int)(faceROI.width * 0.15f);
        int sh = (int)(faceROI.height * 0.18f);
        int sw = (int)(faceROI.width * 0.32f);
        int gap = (int)(faceROI.width * 0.025f);
        Point lp_eye = new Point(faceROI.tl().x+offx, faceROI.tl().y+offy);
        Point lp_end = new Point(lp_eye.x+sw-gap, lp_eye.y+sh);
        Imgproc.rectangle(frame, lp_eye, lp_end, EYE_RECT_COLOR, 2);

        int right_offx = (int)(faceROI.width * 0.095f);
        int rew = (int)(sw *0.81f);
        Point rp_eye = new Point(faceROI.x+faceROI.width/2+right_offx, faceROI.tl().y+offy);
        Point rp_end = new Point(rp_eye.x+rew, rp_eye.y+sh);
        Imgproc.rectangle(frame, rp_eye, rp_end, EYE_RECT_COLOR, 2);

        // 使用级联分类器检测眼睛
        if(option < 3) return;
        MatOfRect eyes = new MatOfRect();

        Rect left_eye_roi = new Rect();
        left_eye_roi.x = (int)lp_eye.x;
        left_eye_roi.y = (int)lp_eye.y;
        left_eye_roi.width = (int)(lp_end.x - lp_eye.x);
        left_eye_roi.height = (int)(lp_end.y - lp_eye.y);

        Rect right_eye_roi = new Rect();
        right_eye_roi.x = (int)rp_eye.x;
        right_eye_roi.y = (int)rp_eye.y;
        right_eye_roi.width = (int)(rp_end.x - rp_eye.x);
        right_eye_roi.height = (int)(rp_end.y - rp_eye.y);


        // 级联分类器
        Mat leftEye = frame.submat(left_eye_roi);
        Mat rightEye = frame.submat(right_eye_roi);

        eyeDetector.detectMultiScale(gray.submat(left_eye_roi), eyes, 1.15, 2, 0, new Size(30,30), new Size());
        Rect[] eyesArray = eyes.toArray();
        for(int i=0; i<eyesArray.length; i++) {
            Log.i("EYE_DETECTION", "Found Left Eyes...");
            leftEye.submat(eyesArray[i]).copyTo(leftEye_template);
            detectPupil(leftEye.submat(eyesArray[i]));
            Imgproc.rectangle(leftEye, eyesArray[i].tl(), eyesArray[i].br(), EYE_COLOR, 2);
        }
        if(eyesArray.length == 0) {
            Rect left_roi = matchEyeTemplate(leftEye, true);
            if(left_roi != null) {
                detectPupil(leftEye.submat(left_roi));
                Imgproc.rectangle(leftEye, left_roi.tl(), left_roi.br(), EYE_COLOR, 2);
            } else {
                detectPupil(leftEye);
            }
        }

        eyes.release();
        eyes = new MatOfRect();
        eyeDetector.detectMultiScale(gray.submat(right_eye_roi), eyes, 1.15, 2, 0, new Size(30,30), new Size());
        eyesArray = eyes.toArray();
        for(int i=0; i<eyesArray.length; i++) {
            Log.i("EYE_DETECTION", "Found Right Eyes...");
            rightEye.submat(eyesArray[i]).copyTo(rightEye_template);
            detectPupil(rightEye.submat(eyesArray[i]));
            Imgproc.rectangle(rightEye, eyesArray[i].tl(), eyesArray[i].br(), EYE_COLOR, 2);
        }
        if(eyesArray.length == 0) {
            Rect right_roi = matchEyeTemplate(rightEye, false);
            if(right_roi != null) {
                detectPupil(rightEye.submat(right_roi));
                Imgproc.rectangle(rightEye, right_roi.tl(), right_roi.br(), EYE_COLOR, 2);
            }
            else {
                detectPupil(rightEye);
            }
        }

        //if(option>4) {
        //    detectPupil(leftEye);
        //    detectPupil(rightEye);
        //}

    }

    private void detectPupil(Mat eyeImage) {
        if(option < 4) return;
        Mat gray = new Mat();
        Mat binary = new Mat();

        Imgproc.cvtColor(eyeImage, gray, Imgproc.COLOR_RGBA2GRAY);
        Imgproc.threshold(gray, binary, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);

        Imgproc.morphologyEx(binary, binary, Imgproc.MORPH_CLOSE, k1);
        Imgproc.morphologyEx(binary, binary, Imgproc.MORPH_OPEN, k2);

        //
        if(option > 4) {
            renderEye(eyeImage, binary);
        } else {
            // 轮廓发现
            List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(binary, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));

            // 绘制轮廓
            for (int i = 0; i < contours.size(); i++) {
                Imgproc.drawContours(eyeImage, contours, i, new Scalar(0, 255, 0), -1);
            }
            hierarchy.release();
            contours.clear();
        }
        gray.release();
        binary.release();
    }

    private void renderEye(Mat eyeImage, Mat mask) {
        //Core.add(eyeImage, new Scalar(100, 30, 10), eyeImage, mask);
        Mat blur_mask = new Mat();
        Mat blur_mask_f = new Mat();

        // 高斯模糊
        Imgproc.GaussianBlur(mask, blur_mask, new Size(3, 3), 0.0);
        blur_mask.convertTo(blur_mask_f, CvType.CV_32F);
        Core.normalize(blur_mask_f, blur_mask_f, 1.0, 0, Core.NORM_MINMAX);

        // 获取数据
        int w = eyeImage.cols();
        int h = eyeImage.rows();
        int ch = eyeImage.channels();
        byte[] data1 = new byte[w*h*ch];
        byte[] data2 = new byte[w*h*ch];
        float[] mdata = new float[w*h];
        blur_mask_f.get(0, 0, mdata);
        eyeImage.get(0, 0, data1);

        // 高斯权重混合
        for(int row=0; row<h; row++) {
            for(int col=0; col<w; col++) {
                int r1 = data1[row*ch*w + col*ch]&0xff;
                int g1 = data1[row*ch*w + col*ch+1]&0xff;
                int b1 = data1[row*ch*w + col*ch+2]&0xff;

                int r2 = (data1[row*ch*w + col*ch]&0xff) + 50;
                int g2 = (data1[row*ch*w + col*ch+1]&0xff) + 20;
                int b2 = (data1[row*ch*w + col*ch+2]&0xff) + 10;

                float w2 = mdata[row*w + col];
                float w1 = 1.0f - w2;

                r2 = (int)(r2*w2 + w1*r1);
                g2 = (int)(g2*w2 + w1*g1);
                b2 = (int)(b2*w2 + w1*b1);

                r2 = r2 > 255 ? 255 : r2;
                g2 = g2 > 255 ? 255 : g2;
                b2 = b2 > 255 ? 255 : b2;

                data2[row*ch*w + col*ch]=(byte)r2;
                data2[row*ch*w + col*ch+1]=(byte)g2;
                data2[row*ch*w + col*ch+2]=(byte)b2;
            }
        }
        eyeImage.put(0, 0, data2);

        // 释放内存
        blur_mask.release();
        blur_mask_f.release();
        data1 = null;
        data2 = null;
        mdata = null;
    }

    /**
     * save images as debug info
     * @param image
     */
    private void saveDebugImage(Mat image) {
        Bitmap bitmap = Bitmap.createBitmap(image.cols(),image.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(image, bitmap);
        File filedir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "myOcrImages");
        String name = String.valueOf(System.currentTimeMillis()) + "_eye.jpg";
        File tempFile = new File(filedir.getAbsolutePath() + File.separator, name);
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        }catch (IOException ioe) {
            Log.i("DEBUGIMG", ioe.getMessage());
        }finally {
            try {
                output.flush();
                output.close();
            }
            catch (IOException e) {
                Log.i("CLOSEOUTPUT", e.getMessage());
            }
        }
    }

    private Rect matchEyeTemplate(Mat src, Boolean left) {
        Mat tpl = left ? leftEye_template : rightEye_template;
        if(tpl.cols() == 0 || tpl.rows() == 0) {
            return null;
        }
        int height = src.rows() - tpl.rows() + 1;
        int width = src.cols() - tpl.cols() + 1;
        if(height < 1 || width < 1) {
            return null;
        }
        Mat result = new Mat(height, width, CvType.CV_32FC1);

        // 模板匹配
        int method = Imgproc.TM_CCOEFF_NORMED;
        Imgproc.matchTemplate(src, tpl, result, method);
        Core.MinMaxLocResult minMaxResult = Core.minMaxLoc(result);
        Point maxloc = minMaxResult.maxLoc;

        // ROI
        Rect rect = new Rect();
        rect.x = (int)(maxloc.x);
        rect.y = (int)(maxloc.y);
        rect.width = tpl.cols();
        rect.height = tpl.rows();

        result.release();
        return rect;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.face_trace_menu_id) {
            option = 1;

        } else if (id == R.id.eye_roi_menu_id) {
            option = 2;

        } else if (id == R.id.eye_location_menu_id) {
            option = 3;

        } else if (id == R.id.location_ball_menu_id) {
            option = 4;

        } else if (id == R.id.render_ball_menu_id) {
            option = 5;

        } else {
            option = 0;

        }
        return super.onOptionsItemSelected(item);
    }

}
