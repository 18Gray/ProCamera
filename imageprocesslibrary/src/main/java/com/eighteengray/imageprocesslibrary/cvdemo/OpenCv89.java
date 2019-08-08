package com.eighteengray.imageprocesslibrary.cvdemo;

import android.graphics.Bitmap;
import android.net.Uri;
import com.eighteengray.imageprocesslibrary.ImageProcessJni;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;
import java.util.List;




public class OpenCv89 {

    // Eight
    public static Bitmap extractNumberROI(Bitmap input, Bitmap template) {
        Mat src = new Mat();
        Mat tpl = new Mat();
        Mat dst = new Mat();
        Mat fixSrc = new Mat();
        Utils.bitmapToMat(input, src);

        Utils.bitmapToMat(template, tpl);
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGRA2GRAY);
        Imgproc.Canny(dst, dst, 200, 400, 3, false);
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierachy = new Mat();
        Imgproc.findContours(dst, contours, hierachy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_GRAY2BGR);
        int width = input.getWidth();
        int height = input.getHeight();
        Rect roiArea = null;
        for(int i=0; i<contours.size(); i++) {
            List<Point> points = contours.get(i).toList();
            Rect rect = Imgproc.boundingRect(contours.get(i));
            if(rect.width < width && rect.width > (width / 2)) {
                if(rect.height <= (height / 4)) continue;
                roiArea = rect;
            }
        }
        // clip ROI Area
        Mat result = src.submat(roiArea);

        // fix size, in order to match template
        Size fixSize = new Size(547, 342);
        Imgproc.resize(result, fixSrc, fixSize);
        result = fixSrc;

        // detect location
        int result_cols =  result.cols() - tpl.cols() + 1;
        int result_rows = result.rows() - tpl.rows() + 1;
        Mat mr = new Mat(result_rows, result_cols, CvType.CV_32FC1);

        // template match
        Imgproc.matchTemplate(result, tpl, mr, Imgproc.TM_CCORR_NORMED);
        Core.normalize(mr, mr, 0, 1, Core.NORM_MINMAX, -1);
        Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(mr);
        Point maxLoc = minMaxLocResult.maxLoc;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types

        // find id number ROI
        Rect idNumberROI = new Rect((int)(maxLoc.x+tpl.cols()), (int)maxLoc.y, (int)(result.cols() - (maxLoc.x+tpl.cols())-40), tpl.rows()-10);
        Mat idNumberArea = result.submat(idNumberROI);

        // 返回对象
        Bitmap bmp = Bitmap.createBitmap(idNumberArea.cols(), idNumberArea.rows(), conf);
        Utils.matToBitmap(idNumberArea, bmp);

        // 释放内存
        idNumberArea.release();
        idNumberArea.release();
        result.release();
        fixSrc.release();
        src.release();
        dst.release();
        return bmp;
    }

    public static void deSkewText(Mat textImage, Mat dst) {
        // 二值化图像
        Mat gray = new Mat();
        Mat binary = new Mat();
        Imgproc.cvtColor(textImage, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(gray, binary, 0, 255,Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);

        // 寻找文本区域最新外接矩形
        int w = binary.cols();
        int h = binary.rows();
        List<Point> points = new ArrayList<>();
        int p = 0;
        byte[] data = new byte[w*h];
        binary.get(0, 0, data);
        int index = 0;
        for(int row=0; row<h; row++) {
            for(int col=0; col<w; col++) {
                index = row*w + col;
                p = data[index]&0xff;
                if(p == 255) {
                    points.add(new Point(col, row));
                }
            }
        }
        RotatedRect box = Imgproc.minAreaRect(new MatOfPoint2f(points.toArray(new Point[0])));
        double angle = box.angle;
        if (angle < -45.)
            angle += 90.;

        Point[] vertices = new Point[4];
        box.points(vertices);
        // de-skew 偏斜校正
        Mat rot_mat = Imgproc.getRotationMatrix2D(box.center, angle, 1);
        Imgproc.warpAffine(binary, dst, rot_mat, binary.size(), Imgproc.INTER_CUBIC);
        Core.bitwise_not(dst, dst);

        gray.release();
        binary.release();
        rot_mat.release();
    }

    public static Bitmap deSkewTextImage(Uri fileUri) {
        Mat src = Imgcodecs.imread(fileUri.getPath());
        if(src.empty()){
            return null;
        }
        Mat dst = new Mat();
        deSkewText(src, dst);

        // 转换为Bitmap，显示
        Bitmap bm = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, bm);

        // 释放内存
        dst.release();
        src.release();
        return bm;
    }



    // Nine
    public static Bitmap Integral_Image_Demo(Uri fileUri, int option) {
        float sigma = 30.0f;
        Mat src = Imgcodecs.imread(fileUri.getPath());
        if(src.empty()){
            return null;
        }
        Mat dst = new Mat(src.size(), src.type());
        Mat mask = new Mat(src.size(), CvType.CV_8UC1);
        Mat sum = new Mat();
        Mat sqsum = new Mat();
        int w = src.cols();
        int h = src.rows();
        int ch = src.channels();
        int[] data1 = new int[(w+1)*(h+1)*ch];
        float[] data2 = new float[(w+1)*(h+1)*ch];
        Imgproc.integral2(src, sum, sqsum, CvType.CV_32S, CvType.CV_32F);
        sum.get(0, 0, data1);
        sqsum.get(0, 0, data2);
        if(option == 1) {
            blur_demo(src, sum, dst);
        } else if(option == 2) {
            FastEPFilter(src, data1, data2, dst);
        } else if(option == 3) {
            generateMask(src, mask);
            Core.bitwise_and(src, src, dst, mask);
        } else if(option == 4) {
//            ImageProcessJni.beautySkinFilter(src.getNativeObjAddr(), dst.getNativeObjAddr(),sigma, false);
            //generateMask(src, mask);
            //FastEPFilter(src, data1, data2, dst);
            //blendImage(src, dst, mask);
            //enhanceEdge(src, dst, mask);
        }

        // 转换为Bitmap，显示
        Bitmap bm = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Mat result = new Mat();
        Imgproc.cvtColor(dst, result, Imgproc.COLOR_BGR2RGBA);
        Utils.matToBitmap(result, bm);

        // release memory
        src.release();
        dst.release();
        sum.release();
        sqsum.release();
        data1 = null;
        data2 = null;
        mask.release();
        result.release();
        return bm;
    }

    public static void enhanceEdge(Mat src, Mat dst, Mat mask) {
        Imgproc.Canny(src, mask, 150, 300, 3, true);
        Core.bitwise_and(src, src, dst, mask);
        Imgproc.GaussianBlur(dst, dst, new Size(3, 3), 0.0);
    }

    public static void blendImage(Mat src, Mat dst, Mat mask) {
        Mat blur_mask = new Mat();
        Mat blur_mask_f = new Mat();

        // 高斯模糊
        Imgproc.GaussianBlur(mask, blur_mask, new Size(3, 3), 0.0);
        blur_mask.convertTo(blur_mask_f, CvType.CV_32F);
        Core.normalize(blur_mask_f, blur_mask_f, 1.0, 0, Core.NORM_MINMAX);

        // 获取数据
        int w = src.cols();
        int h = src.rows();
        int ch = src.channels();
        byte[] data1 = new byte[w*h*ch];
        byte[] data2 = new byte[w*h*ch];
        float[] mdata = new float[w*h];
        blur_mask_f.get(0, 0, mdata);
        src.get(0, 0, data1);
        dst.get(0, 0, data2);

        // 高斯权重混合
        for(int row=0; row<h; row++) {
            for(int col=0; col<w; col++) {
                int b1 = data1[row*ch*w + col*ch]&0xff;
                int g1 = data1[row*ch*w + col*ch+1]&0xff;
                int r1 = data1[row*ch*w + col*ch+2]&0xff;

                int b2 = data2[row*ch*w + col*ch]&0xff;
                int g2 = data2[row*ch*w + col*ch+1]&0xff;
                int r2 = data2[row*ch*w + col*ch+2]&0xff;

                float w2 = mdata[row*w + col];
                float w1 = 1.0f - w2;

                b2 = (int)(b2*w2 + w1*b1);
                g2 = (int)(g2*w2 + w1*g1);
                r2 = (int)(r2*w2 + w1*r1);

                data2[row*ch*w + col*ch]=(byte)b2;
                data2[row*ch*w + col*ch+1]=(byte)g2;
                data2[row*ch*w + col*ch+2]=(byte)r2;
            }
        }
        dst.put(0, 0, data2);

        // 释放内存
        blur_mask.release();
        blur_mask_f.release();
        data1 = null;
        data2 = null;
        mdata = null;
    }

    public static void generateMask(Mat src, Mat mask) {
        int w = src.cols();
        int h = src.rows();
        byte[] data = new byte[3];
        Mat ycrcb = new Mat();
        DefaultSkinFinder skinFinder = new DefaultSkinFinder();
        Imgproc.cvtColor(src, ycrcb, Imgproc.COLOR_BGR2YCrCb);
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                ycrcb.get(row, col, data);
                int y = data[0]&0xff;
                int cr = data[1]&0xff;
                int cb = data[2]&0xff;
                //if ((y > 80) && (85 < cb && cb < 135) && (135 < cr && cr < 180)) {
                if(skinFinder.yCrCbSkin(y, cr, cb)) {
                    mask.put(row, col, new byte[]{(byte) 255});
                }
                //}
            }
        }
        ycrcb.release();
    }

    public static void FastEPFilter(Mat src, int[] sum, float[] sqsum, Mat dst) {
        float sigma = 30.0f;
        int w = src.cols();
        int h = src.rows();
        int x2 = 0, y2 = 0;
        int x1 = 0, y1 = 0;
        int ksize = 15;
        int radius = ksize / 2;
        int ch = src.channels();
        byte[] data = new byte[ch*w*h];
        src.get(0, 0, data);
        int cx = 0, cy = 0;
        float sigma2 = sigma*sigma;
        for (int row = radius; row < h + radius; row++) {
            y2 = (row + 1)>h ? h : (row + 1);
            y1 = (row - ksize) < 0 ? 0 : (row - ksize);
            for (int col = 0; col < w + radius; col++) {
                x2 = (col + 1)>w ? w : (col + 1);
                x1 = (col - ksize) < 0 ? 0 : (col - ksize);
                cx = (col - radius) < 0 ? 0 : col - radius;
                cy = (row - radius) < 0 ? 0 : row - radius;
                int num = (x2 - x1)*(y2 - y1);
                for (int i = 0; i < ch; i++) {
                    int s = getblockMean(sum, x1, y1, x2, y2, i, w+1);
                    float var = getblockSqrt(sqsum, x1, y1, x2, y2, i, w+1);

                    // 计算系数K
                    float dr = (var - (s*s) / num) / num;
                    float mean = s / num;
                    float kr = dr / (dr + sigma2);

                    // 得到滤波后的像素值
                    int r = data[cy*ch*w + cx*ch+i]&0xff;
                    r = (int)((1 - kr)*mean + kr*r);
                    data[cy*ch*w + cx*ch+i] = (byte)r;
                }
            }
        }
        dst.put(0, 0, data);
    }

    public static int getblockMean(int[] sum, int x1, int y1, int x2, int y2, int i, int w) {
        int tl = sum[y1*3*w + x1*3+i];
        int tr = sum[y2*3*w + x1*3+i];
        int bl = sum[y1*3*w + x2*3+i];
        int br = sum[y2*3*w + x2*3+i];
        int s = (br - bl - tr + tl);
        return s;
    }

    public static float getblockSqrt(float[] sum, int x1, int y1, int x2, int y2, int i, int w) {
        float tl = sum[y1*3*w + x1*3+i];
        float tr = sum[y2*3*w + x1*3+i];
        float bl = sum[y1*3*w + x2*3+i];
        float br = sum[y2*3*w + x2*3+i];
        float var = (br - bl - tr + tl);
        return var;
    }

    public static void blur_demo(Mat src, Mat sum, Mat dst) {
        int w = src.cols();
        int h = src.rows();
        int x2 = 0, y2 = 0;
        int x1 = 0, y1 = 0;
        int ksize = 15;
        int radius = ksize / 2;
        int ch = src.channels();
        byte[] data = new byte[ch*w*h];
        int[] tl = new int[3];
        int[] tr = new int[3];
        int[] bl = new int[3];
        int[] br = new int[3];
        int cx = 0;
        int cy = 0;
        for (int row = 0; row < h+radius; row++) {
            y2 = (row+1)>h?h:(row+1);
            y1 = (row - ksize) < 0 ? 0 : (row - ksize);
            for (int col = 0; col < w+radius; col++) {
                x2 = (col+1)>w?w:(col+1);
                x1 = (col - ksize) < 0 ? 0 : (col - ksize);
                sum.get(y1, x1,tl);
                sum.get(y2, x1,tr);
                sum.get(y1, x2,bl);
                sum.get(y2, x2,br);
                cx = (col - radius) < 0 ? 0 : col - radius;
                cy = (row - radius) < 0 ? 0 : row - radius;
                for (int i = 0; i < ch; i++) {
                    int num = (x2 - x1)*(y2 - y1);
                    int x = (br[i] - bl[i] - tr[i] + tl[i]) / num;
                    data[cy*ch*w + cx*ch+i] = (byte)x;
                }
            }
        }
        dst.put(0, 0, data);
    }



}
