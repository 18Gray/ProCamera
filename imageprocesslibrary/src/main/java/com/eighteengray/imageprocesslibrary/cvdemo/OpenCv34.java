package com.eighteengray.imageprocesslibrary.cvdemo;


import android.graphics.Bitmap;
import android.net.Uri;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class OpenCv34 {

    // Three
    public static Bitmap blendMat(Uri fileUri, double alpha, double gamma) {
        // 加载图像
        Mat src = Imgcodecs.imread(fileUri.getPath());
        if(src.empty()){
            return null;
        }

        // create black image
        Mat black = Mat.zeros(src.size(), src.type());
        Mat dst = new Mat();

        // 像素混合 - 基于权重
        Core.addWeighted(src, alpha, black, 1.0-alpha, gamma, dst);

        // 转换为Bitmap，显示
        Bitmap bm = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Mat result = new Mat();
        Imgproc.cvtColor(dst, result, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(result, bm);
        return bm;
    }

    public static Bitmap meanAndDev(Uri fileUri) {
        // 加载图像
        Mat src = Imgcodecs.imread(fileUri.getPath());
        if(src.empty()){
            return null;
        }
        // 转为灰度图像
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);

        // 计算均值与标准方差
        MatOfDouble means = new MatOfDouble();
        MatOfDouble stddevs = new MatOfDouble();
        Core.meanStdDev(gray, means, stddevs);

        // 显示均值与标准方差
        double[] mean = means.toArray();
        double[] stddev = stddevs.toArray();

        // 读取像素数组
        int width = gray.cols();
        int height = gray.rows();
        byte[] data = new byte[width*height];
        gray.get(0, 0, data);
        int pv = 0;

        // 根据均值，二值分割
        int t = (int)mean[0];
        for(int i=0; i<data.length; i++) {
            pv = data[i]&0xff;
            if(pv > t) {
                data[i] = (byte)255;
            } else {
                data[i] = (byte)0;
            }
        }
        gray.put(0, 0, data);

        Bitmap bm = Bitmap.createBitmap(gray.cols(), gray.rows(), Bitmap.Config.ARGB_8888);
        Mat dst = new Mat();
        Imgproc.cvtColor(gray, dst, Imgproc.COLOR_GRAY2RGBA);
        Utils.matToBitmap(dst, bm);
        dst.release();
        gray.release();
        src.release();
        return bm;
    }

   public static Bitmap normAndAbs() {
        // 创建随机浮点数图像
        Mat src = Mat.zeros(400, 400, CvType.CV_32FC3);
        float[] data = new float[400*400*3];
        Random random = new Random();
        for(int i=0; i<data.length; i++) {
            data[i] = (float)random.nextGaussian();
        }
        src.put(0, 0, data);

        // 归一化值到0～255之间
        Mat dst = new Mat();
        Core.normalize(src, dst, 0, 255, Core.NORM_MINMAX, -1, new Mat());


        // 类型转换
        Mat dst8u = new Mat();
        dst.convertTo(dst8u, CvType.CV_8UC3);

        // 转换为Bitmap，显示
        Bitmap bm = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888);
        Mat result = new Mat();
        Imgproc.cvtColor(dst8u, result, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(result, bm);

        return bm;
    }

   public static Bitmap logicOperator() {
        Mat src1 = Mat.zeros(400, 400, CvType.CV_8UC3);
        Mat src2 = new Mat(400, 400, CvType.CV_8UC3);
        src2.setTo(new Scalar(255, 255, 255));

        Rect rect = new Rect();
        rect.x=100;
        rect.y=100;
        rect.width = 200;
        rect.height = 200;
        Imgproc.rectangle(src1, rect.tl(), rect.br(), new Scalar(0, 255, 0), -1);

        rect.x=10;
        rect.y=10;
        Imgproc.rectangle(src2, rect.tl(), rect.br(), new Scalar(255, 255, 0), -1);

        Mat dst1 = new Mat();
        Mat dst2 = new Mat();
        Mat dst3 = new Mat();
        Core.bitwise_and(src1, src2, dst1);
        Core.bitwise_or(src1, src2, dst2);
        Core.bitwise_xor(src1, src2, dst3);

        Mat dst = Mat.zeros(400, 1200, CvType.CV_8UC3);
        rect.x=0;
        rect.y=0;
        rect.width=400;
        rect.height=400;
        dst1.copyTo(dst.submat(rect));
        rect.x=400;
        dst2.copyTo(dst.submat(rect));
        rect.x=800;
        dst3.copyTo(dst.submat(rect));
        dst1.release();
        dst2.release();
        dst3.release();

        // 转换为Bitmap，显示
        Bitmap bm = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888);
        Mat result = new Mat();
        Imgproc.cvtColor(dst, result, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(result, bm);

        return bm;
    }


   public static Bitmap adjustBrightAndContrast(Uri fileUri, int b, float c) {
        // 输入图像src1
        Mat src = Imgcodecs.imread(fileUri.getPath());
        if(src.empty()){
            return null;
        }

        // 调整亮度
        Mat dst1 = new Mat();
        Core.add(src, new Scalar(b, b, b), dst1);

        // 调整对比度
        Mat dst2 = new Mat();
        Core.multiply(dst1, new Scalar(c, c, c), dst2);

        // 转换为Bitmap，显示
        Bitmap bm = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Mat result = new Mat();
        Imgproc.cvtColor(dst2, result, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(result, bm);

       return bm;
    }

   public static Bitmap matArithmeticDemo(Uri fileUri) {
        // 输入图像src1
        Mat src = Imgcodecs.imread(fileUri.getPath());
        if(src.empty()){
            return null;
        }
        // 输入图像src2
        Mat moon = Mat.zeros(src.rows(), src.cols(), src.type());
        int cx = src.cols() - 60;
        int cy = 60;
        Imgproc.circle(moon, new Point(cx, cy), 50, new Scalar(90,95,234), -1, 8, 0);

        // 加法运算
        Mat dst = new Mat();
        Core.add(src, moon, dst);

        Bitmap bm = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Mat result = new Mat();
        Imgproc.cvtColor(dst, result, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(result, bm);
       return bm;
    }

   public static Bitmap channelsAndPixels(Uri fileUri) {
        Mat src = Imgcodecs.imread(fileUri.getPath());
        if(src.empty()){
            return null;
        }

        List<Mat> mv = new ArrayList<>();
        Core.split(src, mv);
        for(Mat m : mv) {
            int pv = 0;
            int channels = m.channels();
            int width = m.cols();
            int height = m.rows();
            byte[] data = new byte[channels*width*height];
            m.get(0, 0, data);
            for(int i=0; i<data.length; i++) {
                pv = data[i]&0xff;
                pv = 255-pv;
                data[i] = (byte)pv;
            }
            m.put(0, 0, data);
        }
        Core.merge(mv, src);

        Bitmap bm = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Mat dst = new Mat();
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(dst, bm);
        dst.release();
        src.release();

       return bm;
    }

   public static Bitmap readAndWritePixels(Uri fileUri) {
        Mat src = Imgcodecs.imread(fileUri.getPath());
        if(src.empty()){
            return null;
        }
        int channels = src.channels();
        int width = src.cols();
        int height = src.rows();

//        // each row data
//        byte[] data = new byte[channels*width];
//        // loop
//        int b=0, g=0, r=0;
//        int pv = 0;
//        for(int row=0; row<height; row++) {
//            src.get(row, 0, data);
//            for(int col=0; col<data.length; col++) {
//                // 读取
//                pv = data[col]&0xff;
//                // 修改
//                pv = 255 - pv;
//                data[col] = (byte)pv;
//            }
//            // 写入
//            src.put(row, 0, data);
//        }

        // all pixels
        int pv = 0;
        byte[] data = new byte[channels*width*height];
        src.get(0, 0, data);
        for(int i=0; i<data.length; i++) {
            pv = data[i]&0xff;
            pv = 255-pv;
            data[i] = (byte)pv;
        }
        src.put(0, 0, data);

        Bitmap bm = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Mat dst = new Mat();
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(dst, bm);
       return bm;
    }




    // Four
    public static Bitmap blurImage(Uri fileUri, int type) {
        // read image
        Mat src = Imgcodecs.imread(fileUri.getPath());
        if(src.empty()){
            return null;
        }
        Mat dst = new Mat();

        if(type == 0) {
            Imgproc.blur(src, dst, new Size(5, 5), new Point(-1, -1), Core.BORDER_DEFAULT);
        } else if(type == 1) {
            Imgproc.GaussianBlur(src, dst, new Size(0, 0), 15);
        }else if(type == 2) {
            Imgproc.medianBlur(src, dst, 5);
        } else if(type == 3) {
            Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
            Imgproc.dilate(src, dst, kernel);
        }else if(type == 4) {
            Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
            Imgproc.erode(src, dst, kernel);
        }else if(type == 5) {
            Imgproc.bilateralFilter(src, dst, 0, 150, 15);
        }else if(type == 6) {
            Imgproc.pyrMeanShiftFiltering(src, dst, 10, 50);
        } else if(type == 7) {
            customFilter(src, dst, 0);
        } else if(type == 8) {
            morphologyDemo(src, dst, 0);
        } else if(type == 9) {
            thresholdDemo(src, dst);
        }else if(type == 10) {
            adpThresholdDemo(src, dst);
        }

        // 转换为Bitmap，显示
        Bitmap bm = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Mat result = new Mat();
        Imgproc.cvtColor(dst, result, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(result, bm);


        // release memory
        src.release();
        dst.release();
        result.release();

        return bm;
    }

    public static void thresholdDemo(Mat src, Mat dst) {
        int t = 127;
        int maxValue = 255;
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        // Imgproc.threshold(gray, dst, t, maxValue, Imgproc.THRESH_BINARY | Imgproc.THRESH_TRIANGLE);
        Imgproc.threshold(gray, dst, t, maxValue, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        gray.release();
    }

    public static void adpThresholdDemo(Mat src, Mat dst) {
        int t = 127;
        int maxValue = 255;
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.adaptiveThreshold(src, dst, 255,
                Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 15, 10);
        gray.release();
    }

    public static void morphologyDemo(Mat src, Mat dst, int option) {

        // 创建结构元素
        Mat k = Imgproc.getStructuringElement(
                Imgproc.MORPH_RECT, new Size(15, 15), new Point(-1, -1));

        // 形态学操作
        switch (option) {
            case 0: // 膨胀
                Imgproc.morphologyEx(src, dst, Imgproc.MORPH_DILATE, k);
                break;
            case 1: // 腐蚀
                Imgproc.morphologyEx(src, dst, Imgproc.MORPH_ERODE, k);
                break;
            case 2: // 开操作
                Imgproc.morphologyEx(src, dst, Imgproc.MORPH_OPEN, k);
                break;
            case 3: // 闭操作
                Imgproc.morphologyEx(src, dst, Imgproc.MORPH_CLOSE, k);
                break;
            case 4: // 黑帽
                Imgproc.morphologyEx(src, dst, Imgproc.MORPH_BLACKHAT, k);
                break;
            case 5: // 顶帽
                Imgproc.morphologyEx(src, dst, Imgproc.MORPH_TOPHAT, k);
                break;
            case 6: // 基本梯度
                Imgproc.morphologyEx(src, dst, Imgproc.MORPH_GRADIENT, k);
                break;
            default:
                break;
        }
    }

    public static void customFilter(Mat src, Mat dst, int type) {
        if(type == 1) {
            Mat k = new Mat(3, 3, CvType.CV_32FC1);
            float[] data = new float[]{1.0f/9.0f,1.0f/9.0f,1.0f/9.0f,
                    1.0f/9.0f, 1.0f/9.0f, 1.0f/9.0f,
                    1.0f/9.0f, 1.0f/9.0f, 1.0f/9.0f};
            k.put(0, 0, data);
        } else if(type == 2) {
            Mat k = new Mat(3, 3, CvType.CV_32FC1);
            float[] data = new float[]{0,1.0f/8.0f,0,
                    1.0f/8.0f, 0.5f, 1.0f/8.0f,
                    0, 1.0f/8.0f, 0};
            k.put(0, 0, data);
        } else if(type == 3) {
            Mat kx = new Mat(3, 3, CvType.CV_32FC1);
            Mat ky = new Mat(3, 3, CvType.CV_32FC1);

            float[] robert_x = new float[]{-1,0,0,1};
            kx.put(0, 0, robert_x);

            float[] robert_y = new float[]{0,1,-1,0};
            ky.put(0, 0, robert_y);

            Imgproc.filter2D(src, dst, -1, kx);
            Imgproc.filter2D(src, dst, -1, ky);
        }
    }



}
