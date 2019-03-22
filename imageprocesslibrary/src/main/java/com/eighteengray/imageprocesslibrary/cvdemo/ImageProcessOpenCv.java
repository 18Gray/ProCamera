package com.eighteengray.imageprocesslibrary.cvdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import com.eighteengray.imageprocesslibrary.R;
import org.opencv.android.Utils;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.xfeatures2d.SIFT;
import org.opencv.xfeatures2d.SURF;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class ImageProcessOpenCv {

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

    public static Bitmap mat2BitmapDemo(Uri fileUri, int index) {
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

    public static Bitmap basicDrawOnCanvas() {
        // 创建Bitmap对象
        Bitmap bm = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);

        // 创建画布与画笔风格
        Canvas canvas = new Canvas(bm);
        Paint p = new Paint();
        p.setColor(Color.BLUE);
        p.setStyle(Paint.Style.FILL_AND_STROKE);

        // 绘制直线
        canvas.drawLine(10, 10, 490, 490, p);
        canvas.drawLine(10, 490, 490, 10, p);

        // 绘制矩形
        android.graphics.Rect rect = new android.graphics.Rect();
        rect.set(50, 50, 150, 150); // 矩形左上角点，与右下角点坐标
        canvas.drawRect(rect, p);

        // 绘制圆
        p.setColor(Color.GREEN);
        canvas.drawCircle(400, 400, 50, p);

        // 绘制文本
        p.setColor(Color.RED);
        canvas.drawText("Basic Drawing on Canvas", 40, 40, p);
        return bm;
    }

    public static Bitmap basicDrawOnMat() {
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
        //Mat src = Imgcodecs.imread(fileUri.getPath());
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

    public static void getBitmapInfo(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        Bitmap.Config config = bm.getConfig();

        int a=0, r=0, g=0, b=0;
        for(int row=0; row<height; row++) {
            for(int col=0; col<width; col++) {
                // 读取像素
                int pixel = bm.getPixel(col, row);
                a = Color.alpha(pixel);
                r = Color.red(pixel);
                g = Color.green(pixel);
                b = Color.blue(pixel);
                // 修改像素
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;
                // 保存到Bitmap中
                bm.setPixel(col, row, Color.argb(a, r, g, b));
            }
        }

        int[] pixels = new int[width*height];
        bm.getPixels(pixels, 0, width, 0, 0, width, height);
    }

    public static void scanPixelsDemo(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        Bitmap.Config config = bm.getConfig();
        int[] pixels = new int[width*height];
        bm.getPixels(pixels, 0, width, 0, 0, width, height);
        int a=0, r=0, g=0, b=0;
        int index = 0;
        for(int row=0; row<height; row++) {
            for(int col=0; col<width; col++) {
                // 读取像素
                index = width*row + col;
                a=(pixels[index]>>24)&0xff;
                r=(pixels[index]>>16)&0xff;
                g=(pixels[index]>>8)&0xff;
                b=pixels[index]&0xff;
                // 修改像素
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;
                // 保存到Bitmap中
                pixels[index] = (a << 24) | (r << 16) | (g << 8) | b;
            }
        }
        bm.setPixels(pixels, 0, width, 0, 0, width, height);
    }


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

    
    // Five
    public static Bitmap analysisImage(Uri fileUri, int section) {
        // read image
        Mat src = Imgcodecs.imread(fileUri.getPath());
        if(src.empty()){
            return null;
        }
        Mat dst = new Mat();

        // 演示程序部分
        if(section == 0) {
            sobelDemo(src, dst);
        } else if(section == 1) {
            scharrDemo(src, dst);
        } else if(section == 2) {
            laplianDemo(src,dst);
        } else if(section == 3) {
            edge2Demo(src, dst);
        } else if(section == 4) {
            houghLinePDemo(src, dst);
        } else if(section == 5) {
            houghLinesDemo(src, dst);
        } else if(section == 6) {
            houghCircleDemo(src, dst);
        } else if(section == 7) {
            findContoursDemo(src, dst);
        } else if(section == 8) {
            measureContours(src, dst);
        } else if(section == 9) {
            displayHistogram(src, dst);
        } else if(section == 10) {
            equalizeHistogram(src, dst);
        } else if(section == 11) {
            compareHistogram(src, dst);
        } else if(section == 12) {
            backProjectionHistogram(src, dst, fileUri);
        } else if(section == 13) {
            matchTemplateDemo(src, dst, fileUri);
        }


        // 转换为Bitmap，显示
        Bitmap bm = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888);
        Mat result = new Mat();
        Imgproc.cvtColor(dst, result, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(result, bm);


        // release memory
        src.release();
        dst.release();
        result.release();

        return bm;
    }

    public static void matchTemplateDemo(Mat src, Mat dst, Uri fileUri) {
        String tplFilePath = fileUri.getPath().replaceAll("lena", "tmpl");
        Mat tpl = Imgcodecs.imread(tplFilePath);
        int height = src.rows() - tpl.rows() + 1;
        int width = src.cols() - tpl.cols() + 1;
        Mat result = new Mat(height, width, CvType.CV_32FC1);

        // 模板匹配
        int method = Imgproc.TM_CCOEFF_NORMED;
        Imgproc.matchTemplate(src, tpl, result, method);
        Core.MinMaxLocResult minMaxResult = Core.minMaxLoc(result);
        Point maxloc = minMaxResult.maxLoc;
        Point minloc = minMaxResult.minLoc;

        Point matchloc = null;
        if(method == Imgproc.TM_SQDIFF || method == Imgproc.TM_SQDIFF_NORMED) {
            matchloc = minloc;
        } else {
            matchloc = maxloc;
        }
        // 绘制
        src.copyTo(dst);
        Imgproc.rectangle(dst, matchloc, new Point(matchloc.x+tpl.cols(), matchloc.y + tpl.rows()), new Scalar(0, 0, 255), 2, 8, 0);

        tpl.release();
        result.release();
    }

    public static void backProjectionHistogram(Mat src, Mat dst, Uri fileUri) {
        Mat hsv = new Mat();
        String sampleFilePath = fileUri.getPath().replaceAll("target", "sample");
        Mat sample = Imgcodecs.imread(sampleFilePath);
        Imgproc.cvtColor(sample, hsv, Imgproc.COLOR_BGR2HSV);
        Mat mask = Mat.ones(sample.size(), CvType.CV_8UC1);
        Mat mHist = new Mat();
        Imgproc.calcHist(Arrays.asList(hsv), new MatOfInt(0, 1), mask, mHist, new MatOfInt(30, 32), new MatOfFloat(0, 179, 0, 255));
        System.out.println(mHist.rows());
        System.out.println(mHist.cols());

        Mat srcHSV = new Mat();
        Imgproc.cvtColor(src, srcHSV, Imgproc.COLOR_BGR2HSV);

        Imgproc.calcBackProject(Arrays.asList(srcHSV), new MatOfInt(0, 1), mHist, dst, new MatOfFloat(0, 179, 0, 255), 1);
        Core.normalize(dst, dst, 0, 255, Core.NORM_MINMAX);
        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_GRAY2BGR);
    }

    public static void compareHistogram(Mat src, Mat dst) {
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(gray, dst);

        // 直方图一
        List<Mat> images = new ArrayList<>();
        images.add(gray);
        Mat mask = Mat.ones(src.size(), CvType.CV_8UC1);
        Mat hist1 = new Mat();
        Imgproc.calcHist(images, new MatOfInt(0), mask, hist1, new MatOfInt(256), new MatOfFloat(0, 255));
        Core.normalize(hist1, hist1, 0, 255, Core.NORM_MINMAX);

        // 直方图二
        images.clear();
        images.add(dst);
        Mat hist2 = new Mat();
        Imgproc.calcHist(images, new MatOfInt(0), mask, hist2, new MatOfInt(256), new MatOfFloat(0, 255));
        Core.normalize(hist2, hist2, 0, 255, Core.NORM_MINMAX);

        // 比较直方图
        double[] distances = new double[7];
        distances[0] = Imgproc.compareHist(hist1, hist2, Imgproc.HISTCMP_CORREL);
        distances[1] = Imgproc.compareHist(hist1, hist2, Imgproc.HISTCMP_CHISQR);
        distances[2] = Imgproc.compareHist(hist1, hist2, Imgproc.HISTCMP_INTERSECT);
        distances[3] = Imgproc.compareHist(hist1, hist2, Imgproc.HISTCMP_BHATTACHARYYA);
        distances[4] = Imgproc.compareHist(hist1, hist2, Imgproc.HISTCMP_HELLINGER);
        distances[5] = Imgproc.compareHist(hist1, hist2, Imgproc.HISTCMP_CHISQR_ALT);
        distances[6] = Imgproc.compareHist(hist1, hist2, Imgproc.HISTCMP_KL_DIV);

        for(int i=0; i<distances.length; i++) {
            Log.i("Hist distance", "Distance Type : " + i + " d(H1,H2)=" + distances[i]);
        }
        src.copyTo(dst);
        gray.release();
        hist1.release();
        hist2.release();
    }

    public static void equalizeHistogram(Mat src, Mat dst) {
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(gray, dst);
        gray.release();
    }

    public static void displayHistogram(Mat src, Mat dst) {
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);

        // 计算直方图数据并归一化
        List<Mat> images = new ArrayList<>();
        images.add(gray);
        Mat mask = Mat.ones(src.size(), CvType.CV_8UC1);
        Mat hist = new Mat();
        Imgproc.calcHist(images, new MatOfInt(0), mask, hist, new MatOfInt(256), new MatOfFloat(0, 255));
        Core.normalize(hist, hist, 0, 255, Core.NORM_MINMAX);
        int height = hist.rows();

        dst.create(400, 400, src.type());
        dst.setTo(new Scalar(200, 200, 200));
        float[] histdata = new float[256];
        hist.get(0, 0, histdata);
        int offsetx = 50;
        int offsety = 350;

        // 绘制直方图
        Imgproc.line(dst, new Point(offsetx, 0), new Point(offsetx, offsety), new Scalar(0, 0, 0));
        Imgproc.line(dst, new Point(offsetx, offsety), new Point(400, offsety), new Scalar(0, 0, 0));
        for(int i=0; i<height-1; i++) {
            int y1 = (int)histdata[i];
            int y2 = (int)histdata[i+1];
            Rect rect = new Rect();
            rect.x = offsetx+i;
            rect.y = offsety-y1;
            rect.width = 1;
            rect.height = y1;
            Imgproc.rectangle(dst, rect.tl(), rect.br(), new Scalar(15, 15, 15));
        }

        // 释放内存
        gray.release();
    }

    public static void measureContours(Mat src, Mat dst) {
        Mat gray= new Mat();
        Mat binary = new Mat();

        // 二值
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(gray, binary, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);

        // 轮廓发现
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(binary, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));

        // 测量轮廓
        dst.create(src.size(), src.type());
        for(int i=0; i<contours.size(); i++) {
            Rect rect = Imgproc.boundingRect(contours.get(i));
            double w = rect.width;
            double h = rect.height;
            double rate = Math.min(w, h)/Math.max(w, h);
            Log.i("Bound Rect", "rate : " + rate);
            RotatedRect minRect = Imgproc.minAreaRect(new MatOfPoint2f(contours.get(i).toArray()));
            w = minRect.size.width;
            h = minRect.size.height;
            rate = Math.min(w, h)/Math.max(w, h);
            Log.i("Min Bound Rect", "rate : " + rate);

            double area = Imgproc.contourArea(contours.get(i), false);
            double arclen = Imgproc.arcLength(new MatOfPoint2f(contours.get(i).toArray()), true);
            Log.i("contourArea", "area : " + rate);
            Log.i("arcLength", "arcLength : " + arclen);
            Imgproc.drawContours(dst, contours, i, new Scalar(0, 0, 255), 1);
        }

        // 释放内存
        gray.release();
        binary.release();
    }

    public static void findContoursDemo(Mat src, Mat dst) {
        Mat gray= new Mat();
        Mat binary = new Mat();

        // 二值
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(gray, binary, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

        // 轮廓发现
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(binary, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));

        // 绘制轮廓
        dst.create(src.size(), src.type());
        for(int i=0; i<contours.size(); i++) {
            Imgproc.drawContours(dst, contours, i, new Scalar(0, 0, 255), 2);
        }

        // 释放内存
        gray.release();
        binary.release();
    }

    public static void houghCircleDemo(Mat src, Mat dst) {
        Mat gray = new Mat();
        Imgproc.pyrMeanShiftFiltering(src, gray, 15, 80);
        Imgproc.cvtColor(gray, gray, Imgproc.COLOR_BGR2GRAY);

        Imgproc.GaussianBlur(gray, gray, new Size(3, 3),  0);

        // detect circles
        Mat circles = new Mat();
        dst.create(src.size(), src.type());
        Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1, 20, 100, 30, 10, 200);
        for(int i=0; i<circles.cols(); i++) {
            float[] info = new float[3];
            circles.get(0, i, info);
            Imgproc.circle(dst, new Point((int)info[0], (int)info[1]), (int)info[2],
                    new Scalar(0, 255, 0), 2, 8, 0);
        }
        circles.release();
        gray.release();
    }

    public static void houghLinePDemo(Mat src, Mat dst) {
        Mat edges = new Mat();
        Imgproc.Canny(src, edges, 50, 150, 3, true);

        Mat lines = new Mat();
        Imgproc.HoughLinesP(edges, lines, 1, Math.PI/180.0, 100, 50, 10);

        Mat out = Mat.zeros(src.size(), src.type());
        for(int i=0; i<lines.rows(); i++) {
            int[] oneline = new int[4];
            lines.get(i, 0, oneline);
            Imgproc.line(out, new Point(oneline[0], oneline[1]),
                    new Point(oneline[2], oneline[3]),
                    new Scalar(0, 0, 255), 2, 8, 0);
        }
        out.copyTo(dst);

        // 释放内存
        out.release();
        edges.release();
    }

    public static void houghLinesDemo(Mat src, Mat dst) {
        Mat edges = new Mat();
        Imgproc.Canny(src, edges, 50, 150, 3, true);

        Mat lines = new Mat();
        Imgproc.HoughLines(edges, lines, 1,Math.PI/180.0, 200);
        Mat out = Mat.zeros(src.size(), src.type());
        float[] data = new float[2];
        for(int i=0; i<lines.rows(); i++) {
            lines.get(i, 0, data);
            float rho = data[0], theta = data[1];
            double a = Math.cos(theta), b = Math.sin(theta);
            double x0 = a*rho, y0 = b*rho;
            Point pt1 = new Point();
            Point pt2 = new Point();
            pt1.x = Math.round(x0 + 1000*(-b));
            pt1.y = Math.round(y0 + 1000*(a));
            pt2.x = Math.round(x0 - 1000*(-b));
            pt2.y = Math.round(y0 - 1000*(a));
            Imgproc.line(out, pt1, pt2, new Scalar(0,0,255), 3, Imgproc.LINE_AA, 0);
        }
        out.copyTo(dst);
        out.release();
        edges.release();
    }

    public static void edgeDemo(Mat src, Mat dst) {
        Mat edges = new Mat();
        Imgproc.GaussianBlur(src, src, new Size(3, 3), 0);

        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);

        Imgproc.Canny(src, edges, 50, 150, 3, true);
        Core.bitwise_and(src, src, dst, edges);
    }

    public static void edge2Demo(Mat src, Mat dst) {
        // X方向梯度
        Mat gradx = new Mat();
        Imgproc.Sobel(src, gradx, CvType.CV_16S, 1, 0);

        // Y方向梯度
        Mat grady = new Mat();
        Imgproc.Sobel(src, grady, CvType.CV_16S, 0, 1);

        // 边缘检测
        Mat edges = new Mat();
        Imgproc.Canny(gradx, grady, edges, 50, 150);
        Core.bitwise_and(src, src, dst, edges);

        // 释放内存
        edges.release();
        gradx.release();
        grady.release();
    }

    public static void sobelDemo(Mat src, Mat dst) {
        // X方向梯度
        Mat gradx = new Mat();
        Imgproc.Sobel(src, gradx, CvType.CV_32F, 1, 0);
        Core.convertScaleAbs(gradx, gradx);
        Log.i("OpenCV", "XGradient....");
        // Y方向梯度
        Mat grady = new Mat();
        Imgproc.Sobel(src, grady, CvType.CV_32F, 0, 1);
        Core.convertScaleAbs(grady, grady);
        Log.i("OpenCV", "YGradient....");

        Core.addWeighted(gradx,0.5, grady, 0.5, 0, dst);
        gradx.release();
        grady.release();
        Log.i("OpenCV", "Gradient.....");
    }

    public static void scharrDemo(Mat src, Mat dst) {
        // X方向梯度
        Mat gradx = new Mat();
        Imgproc.Scharr(src, gradx, CvType.CV_32F, 1, 0);
        Core.convertScaleAbs(gradx, gradx);

        // Y方向梯度
        Mat grady = new Mat();
        Imgproc.Scharr(src, grady, CvType.CV_32F, 0, 1);
        Core.convertScaleAbs(grady, grady);

        Core.addWeighted(gradx,0.5, grady, 0.5, 0, dst);
    }

    public static void laplianDemo(Mat src, Mat dst) {
        Imgproc.Laplacian(src, dst, CvType.CV_32F, 3, 1.0, 0);
        Core.convertScaleAbs(dst, dst);
    }



    // Six 
    public static void initFaceDetector(Context context, InputStream input) throws IOException {
        File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
        File file = new File(cascadeDir.getAbsoluteFile(), "lbpcascade_frontalface.xml");
        FileOutputStream output = new FileOutputStream(file);
        byte[] buff = new byte[1024];
        int len = 0;
        while((len = input.read(buff)) != -1) {
            output.write(buff, 0, len);
        }
        input.close();
        output.close();
        CascadeClassifier faceDetector = new CascadeClassifier(file.getAbsolutePath());
        file.delete();
        cascadeDir.delete();
    }

    public static Bitmap extractFeatureImage(Uri fileUri, int section) {
        Mat src = Imgcodecs.imread(fileUri.getPath());
        if(src.empty()){
            return null;
        }
        Mat dst = new Mat();
        if(section == 0) {
            harrisCornerDemo(src, dst);
        } else if(section == 1) {
            shiTomasicornerDemo(src, dst);
        } else if(section == 2) {
            surfDemo(src, dst, fileUri);
        } else if(section == 3){
            siftDemo(src, dst, fileUri);
        } else if(section == 4) {
            detectorDemo(src, dst, 1);
        } else if(section == 5) {
            descriptorDemo(src, dst, fileUri);
        } else if(section == 6) {
            findKnownObject(src, dst, fileUri);
        } else if(section == 7) {
            faceDetectionDemo(src, dst, fileUri.getPath());
        }

        // 转换为Bitmap，显示
        Bitmap bm = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888);
        Mat result = new Mat();
        Imgproc.cvtColor(dst, result, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(result, bm);

        // release memory
        src.release();
        dst.release();
        result.release();
        return bm;
    }

    public static void faceDetectionDemo(Mat src, Mat dst, String filePath) {
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGRA2GRAY);
        MatOfRect faces = new MatOfRect();
        CascadeClassifier faceDetector = new CascadeClassifier(filePath);
        faceDetector.detectMultiScale(gray, faces, 1.1, 3, 0, new Size(50, 50), new Size());
        List<Rect> faceList = faces.toList();
        src.copyTo(dst);
        if(faceList.size() > 0) {
            for(Rect rect : faceList) {
                Imgproc.rectangle(dst, rect.tl(), rect.br(), new Scalar(0,0,255), 2, 8, 0);
            }
        }
        gray.release();
    }

    public static void findKnownObject(Mat src, Mat dst, Uri fileUri) {
        String boxFile = fileUri.getPath().replaceAll("box_in_scene", "box");
        Mat boxImage = Imgcodecs.imread(boxFile, Imgcodecs.IMREAD_GRAYSCALE);
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);


        SURF surf_detector = SURF.create(400, 4, 3, false, false);
        MatOfKeyPoint keyPoints_box = new MatOfKeyPoint();
        MatOfKeyPoint keyPoints_scene = new MatOfKeyPoint();

        // 特征检测-关键点
        surf_detector.detect(boxImage, keyPoints_box);
        surf_detector.detect(gray, keyPoints_scene);

        // 获取描述子
        Mat descriptor_box = new Mat();
        Mat descriptor_scene = new Mat();
        surf_detector.compute(boxImage, keyPoints_box, descriptor_box);
        surf_detector.compute(gray, keyPoints_scene, descriptor_scene);

        // 匹配
        MatOfDMatch matches = new MatOfDMatch();
        DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
        descriptorMatcher.match(descriptor_box, descriptor_scene, matches);

        // find min max distance
        DMatch[] dm_arrays = matches.toArray();
        double max_dist = 0; double min_dist = 100;
        for(int i=0; i<descriptor_box.rows(); i++) {
            double dist = dm_arrays[i].distance;
            max_dist = Math.max(dist, max_dist);
            min_dist = Math.min(dist, min_dist);
        }
        Log.i("Find Known Object", "max distance : " + max_dist);
        Log.i("Find Known Object", "min distance : " + min_dist);

        ArrayList<DMatch> goodMatches = new ArrayList<DMatch>();
        double t = 3.0*min_dist;
        for(int i=0; i<descriptor_box.rows(); i++) {
            if(dm_arrays[i].distance <= t) {
                goodMatches.add(dm_arrays[i]);
            }
        }
        Features2d.drawMatches(boxImage, keyPoints_box, gray, keyPoints_scene, new MatOfDMatch(goodMatches.toArray(new DMatch[0])),
                dst, Scalar.all(-1), Scalar.all(-1), new MatOfByte(),Features2d.NOT_DRAW_SINGLE_POINTS);

        // 得到匹配程度高的关键点对
        Point[] boxes = new Point[goodMatches.size()];
        Point[] scenes = new Point[goodMatches.size()];
        KeyPoint[] kp_boxes = keyPoints_box.toArray();
        KeyPoint[] kp_scenes = keyPoints_scene.toArray();
        for(int i=0; i<goodMatches.size(); i++) {
            boxes[i] = (kp_boxes[goodMatches.get(i).queryIdx].pt);
            scenes[i] = (kp_scenes[goodMatches.get(i).trainIdx].pt);
        }

        // 寻找位置
        Mat H = Calib3d.findHomography(new MatOfPoint2f(boxes), new MatOfPoint2f(scenes), Calib3d.RANSAC, 3);
        Mat obj_corners = new Mat(4,1,CvType.CV_32FC2);
        Mat scene_corners = new Mat(4,1,CvType.CV_32FC2);
        obj_corners.put(0, 0, new double[] {0,0});
        obj_corners.put(1, 0, new double[] {boxImage.cols(),0});
        obj_corners.put(2, 0, new double[] {boxImage.cols(),boxImage.rows()});
        obj_corners.put(3, 0, new double[] {0,boxImage.rows()});
        Core.perspectiveTransform(obj_corners, scene_corners, H);

        // 绘制直线，矩形外接框
        Imgproc.line(dst, new Point(scene_corners.get(0,0)[0]+boxImage.cols(), scene_corners.get(0,0)[1]),
                new Point(scene_corners.get(1,0)[0] + boxImage.cols(), scene_corners.get(1,0)[1]),
                new Scalar(0, 255, 0),4);

        Imgproc.line(dst, new Point(scene_corners.get(1,0)[0]+boxImage.cols(), scene_corners.get(1,0)[1]),
                new Point(scene_corners.get(2,0)[0]+boxImage.cols(), scene_corners.get(2,0)[1]),
                new Scalar(0, 255, 0),4);

        Imgproc.line(dst, new Point(scene_corners.get(2,0)[0]+boxImage.cols(), scene_corners.get(2,0)[1]),
                new Point(scene_corners.get(3,0)[0]+boxImage.cols(), scene_corners.get(3,0)[1]),
                new Scalar(0, 255, 0),4);

        Imgproc.line(dst, new Point(scene_corners.get(3,0)[0]+boxImage.cols(), scene_corners.get(3,0)[1]),
                new Point(scene_corners.get(0,0)[0]+boxImage.cols(), scene_corners.get(0,0)[1]),
                new Scalar(0, 255, 0),4);


        // 释放内存
        keyPoints_box.release();
        keyPoints_scene.release();

        descriptor_box.release();
        descriptor_scene.release();
        matches.release();
    }

    public static void descriptorDemo(Mat src, Mat dst, Uri fileUri) {
        String boxFile = fileUri.getPath().replaceAll("box_in_scene", "box");
        Mat boxImage = Imgcodecs.imread(boxFile);
        FeatureDetector detector = FeatureDetector.create(FeatureDetector.AKAZE);
        DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.AKAZE);

        // 关键点检测
        MatOfKeyPoint keyPoints_box = new MatOfKeyPoint();
        MatOfKeyPoint keyPoints_scene = new MatOfKeyPoint();
        detector.detect(boxImage, keyPoints_box);
        detector.detect(src, keyPoints_scene);

        // 描述子生成
        Mat descriptor_box = new Mat();
        Mat descriptor_scene = new Mat();
        descriptorExtractor.compute(boxImage, keyPoints_box, descriptor_box);
        descriptorExtractor.compute(src, keyPoints_scene, descriptor_scene);

        // 特征匹配
        MatOfDMatch matches = new MatOfDMatch();
        DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
        descriptorMatcher.match(descriptor_box, descriptor_scene, matches);
        Features2d.drawMatches(boxImage, keyPoints_box, src, keyPoints_scene, matches, dst);

        // 释放内存
        keyPoints_box.release();
        keyPoints_scene.release();

        descriptor_box.release();
        descriptor_scene.release();
        matches.release();
    }

    public static void detectorDemo(Mat src, Mat dst, int type) {
        FeatureDetector detector = null;
        if(type == 1) {
            detector = FeatureDetector.create(FeatureDetector.ORB);
        } else if(type == 2) {
            detector = FeatureDetector.create(FeatureDetector.BRISK);
        } else if(type == 3) {
            detector = FeatureDetector.create(FeatureDetector.FAST);
        } else if(type == 4){
            detector = FeatureDetector.create(FeatureDetector.AKAZE);
        } else {
            detector = FeatureDetector.create(FeatureDetector.HARRIS);
        }
        MatOfKeyPoint keyPoints = new MatOfKeyPoint();
        detector.detect(src, keyPoints);
        Features2d.drawKeypoints(src, keyPoints, dst);
    }

    public static void siftDemo(Mat src, Mat dst, Uri fileUri) {
        String boxFile = fileUri.getPath().replaceAll("box_in_scene", "box");
        Mat boxImage = Imgcodecs.imread(boxFile);

        SIFT sift_detector = SIFT.create();
        MatOfKeyPoint keyPoints_box = new MatOfKeyPoint();
        MatOfKeyPoint keyPoints_scene = new MatOfKeyPoint();

        // 特征检测-关键点
        sift_detector.detect(boxImage, keyPoints_box);
        sift_detector.detect(src, keyPoints_scene);

        // 获取描述子
        Mat descriptor_txt = new Mat();
        Mat descriptor_scene = new Mat();
        sift_detector.compute(boxImage, keyPoints_box, descriptor_txt);
        sift_detector.compute(src, keyPoints_scene, descriptor_scene);

        // 匹配
        MatOfDMatch matches = new MatOfDMatch();
        DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_SL2);
        descriptorMatcher.match(descriptor_txt, descriptor_scene, matches);
        Features2d.drawMatches(boxImage, keyPoints_box, src, keyPoints_scene, matches, dst);

        // 释放内存
        keyPoints_box.release();
        keyPoints_scene.release();

        descriptor_txt.release();
        descriptor_scene.release();
        matches.release();
    }

    public static void surfDemo(Mat src, Mat dst, Uri fileUri) {
        String textFile = fileUri.getPath().replaceAll("box_in_scene", "box");
        Mat textImage = Imgcodecs.imread(textFile);

        SURF surf_detector = SURF.create(100, 4, 3, false, false);
        MatOfKeyPoint keyPoints_txt = new MatOfKeyPoint();
        MatOfKeyPoint keyPoints_scene = new MatOfKeyPoint();

        // 特征检测-关键点
        surf_detector.detect(textImage, keyPoints_txt);
        surf_detector.detect(src, keyPoints_scene);

        // 获取描述子
        Mat descriptor_txt = new Mat();
        Mat descriptor_scene = new Mat();
        surf_detector.compute(textImage, keyPoints_txt, descriptor_txt);
        surf_detector.compute(src, keyPoints_scene, descriptor_scene);

        // 匹配
        MatOfDMatch matches = new MatOfDMatch();
        DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_SL2);
        descriptorMatcher.match(descriptor_txt, descriptor_scene, matches);
        Features2d.drawMatches(textImage, keyPoints_txt, src, keyPoints_scene, matches, dst);

        // 释放内存
        keyPoints_txt.release();
        keyPoints_scene.release();

        descriptor_txt.release();
        descriptor_scene.release();
        matches.release();
    }

    public static void shiTomasicornerDemo(Mat src, Mat dst) {
        // 变量定义
        double k = 0.04;
        int blockSize = 3;
        double qualityLevel= 0.01;
        boolean useHarrisCorner = false;

        // 角点检测
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        MatOfPoint corners = new MatOfPoint();
        Imgproc.goodFeaturesToTrack(gray, corners, 100, qualityLevel, 10, new Mat(), blockSize, useHarrisCorner, k);

        // 绘制角点
        dst.create(src.size(), src.type());
        src.copyTo(dst);
        Point[] points = corners.toArray();
        for(int i=0; i<points.length; i++) {
            Imgproc.circle(dst, points[i], 5,  new Scalar(0, 0, 255), 2, 8, 0);
        }
        gray.release();
    }

    public static void harrisCornerDemo(Mat src, Mat dst) {
        // 定义阈值T
        int threshold = 100;
        Mat gray = new Mat();
        Mat response = new Mat();
        Mat response_norm = new Mat();

        // 角点检测
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cornerHarris(gray, response, 2, 3, 0.04);
        Core.normalize(response, response_norm, 0, 255, Core.NORM_MINMAX, CvType.CV_32F);

        // 绘制角点
        dst.create(src.size(), src.type());
        src.copyTo(dst);
        float[] data = new float[1];
        for(int j=0; j<response_norm.rows(); j++ )
        {
            for(int i=0; i<response_norm.cols(); i++ )
            {
                response_norm.get(j, i, data);
                if((int)data[0] > 100)
                {
                    Imgproc.circle(dst, new Point(i, j), 5,  new Scalar(0, 0, 255), 2, 8, 0);
                    Log.i("Harris Corner", "find corner point...");
                }
            }
        }
        gray.release();
        response.release();
    }


}
