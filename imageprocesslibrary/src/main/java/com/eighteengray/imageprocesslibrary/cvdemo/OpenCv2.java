package com.eighteengray.imageprocesslibrary.cvdemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import com.eighteengray.imageprocesslibrary.cvdemo.ImageSelectUtils;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;



public class OpenCv2 {

    // two
    public static Bitmap bitmap2Mat() {
        Bitmap bm = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
        Mat m = new Mat();
        Utils.bitmapToMat(bm, m);
        Imgproc.circle(m, new Point(m.cols()/2, m.rows()/2), 50,
                new Scalar(255, 0, 0, 255), 2, 8, 0);
        Utils.matToBitmap(m, bm);
        return bm;
    }

    public static Bitmap mat2Bitmap(Uri fileUri) {
        Mat src = Imgcodecs.imread(fileUri.getPath());
        int width = src.cols();
        int height = src.rows();
        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Mat dst = new Mat();
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(dst, bm);
        dst.release();
        return bm;
    }

    public static Bitmap drawOnMat() {
        Mat src = Mat.zeros(500, 500, CvType.CV_8UC3);

        Imgproc.ellipse(src, new Point(250, 250), new Size(100, 50),
                360, 0, 360, new Scalar(0, 0, 255), 2, 8, 0);

        Imgproc.putText(src, "Basic Drawing Demo", new Point(20, 20),
                Core.FONT_HERSHEY_PLAIN, 1.0, new Scalar(0, 255, 0), 1);
        Rect rect = new Rect();
        rect.x = 50;
        rect.y = 50;
        rect.width = 100;
        rect.height = 100;
        Imgproc.rectangle(src, rect.tl(), rect.br(), //矩形
                new Scalar(255, 0, 0), 2, 8, 0);
        Imgproc.circle(src, new Point(400, 400), 50,
                new Scalar(0, 255, 0), 2, 8, 0);
        Imgproc.line(src, new Point(10, 10), new Point(490, 490),
                new Scalar(0, 255, 0), 2, 8, 0);
        Imgproc.line(src, new Point(10, 490), new Point(490, 10),
                new Scalar(255, 0, 0), 2, 8, 0);

        Bitmap bm = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Mat dst = new Mat();
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(dst, bm);
        return bm;
    }

    public static void getMatInfo(Uri fileUri) {
        Mat src = Imgcodecs.imread(fileUri.getPath(), Imgcodecs.IMREAD_COLOR);
        int width = src.cols();
        int height = src.rows();
        int dims = src.dims();
        int channels = src.channels();
        int depth = src.depth();
        int type = src.type();
        // 1
        Mat m1 = new Mat();
        m1.create(new Size(3, 3), CvType.CV_8UC3);
        Mat m2 = new Mat();
        m2.create(3, 3, CvType.CV_8UC3);

        Mat m3 = Mat.eye(3, 3,CvType.CV_8UC3);
        Mat m4 = Mat.eye(new Size(3, 3),CvType.CV_8UC3);
        Mat m5 = Mat.zeros(new Size(3, 3), CvType.CV_8UC3);
        Mat m6 = Mat.ones(new Size(3, 3), CvType.CV_8UC3);

        Mat m7 = new Mat(3, 3, CvType.CV_8UC3);
        m7.setTo(new Scalar(255, 255, 255));

        // 创建Mat对象并保存
        Mat image = new Mat(500, 500, CvType.CV_8UC3);
        image.setTo(new Scalar(127, 127, 127));
        ImageSelectUtils.saveImage(image);

        Mat m8 = new Mat(500, 500, CvType.CV_8UC3);
        m8.setTo(new Scalar(127, 127, 127));
        Mat result = new Mat();
        m8.copyTo(result);
    }



}
