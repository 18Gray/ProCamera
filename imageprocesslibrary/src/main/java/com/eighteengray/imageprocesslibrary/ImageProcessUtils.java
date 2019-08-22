package com.eighteengray.imageprocesslibrary;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;

import java.util.Random;


/**
 * 图像处理相关工具类
 */
public class ImageProcessUtils
{

    /**
     * 图像旋转
     * @param bitmap 原始图像
     * @param rotateAngle  旋转角度
     * @return
     */
    public static Bitmap bitmapRotate(Bitmap bitmap, int rotateAngle)
    {
        Matrix matrix = new Matrix();
        matrix.setRotate(rotateAngle, (float) bitmap.getWidth(), (float) bitmap.getHeight());
        float targetX, targetY;
        if (rotateAngle == 90)
        {
            targetX = bitmap.getHeight();
            targetY = 0;
        }
        else
        {
            targetX = bitmap.getHeight();
            targetY = bitmap.getWidth();
        }
        final float[] values = new float[9];
        matrix.getValues(values);
        float x1 = values[Matrix.MTRANS_X];
        float y1 = values[Matrix.MTRANS_Y];
        matrix.postTranslate(targetX - x1, targetY - y1);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getWidth(), Config.ARGB_8888);

        Paint paint = new Paint();
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(bitmap, matrix, paint);
        return newBitmap;
    }


    /**
     * 圆角图片
     * @param bitmap 原始图像
     * @param pixels 圆角大小
     * @return
     */
    public static Bitmap bitmapRoundCorner(Bitmap bitmap, int pixels)
    {
        Bitmap roundCornerBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(roundCornerBitmap);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); //两个绘制取交集就可以画出圆角图像
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return roundCornerBitmap;
    }


    /**
     * 转变为灰度图
     * @param bitmap
     * @return
     */
    public static Bitmap grayBitmap(Bitmap bitmap)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap grayBitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
        Canvas c = new Canvas(grayBitmap);
        Paint paint = new Paint();

        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(cf);  //这里用paint的ColorFilter做颜色过滤
        c.drawBitmap(bitmap, 0, 0, paint);
        return grayBitmap;
    }


    /**
     * 图片线性灰度处理
     * @param image
     * */
    public Bitmap lineGrey(Bitmap image) {
        // 得到图像的宽度和长度
        int width = image.getWidth();
        int height = image.getHeight();
        // 创建线性拉升灰度图像
        Bitmap linegray = null;
        linegray = image.copy(Config.ARGB_8888, true);
        // 依次循环对图像的像素进行处理
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // 得到每点的像素值
                int col = image.getPixel(i, j);
                int alpha = col & 0xFF000000;
                int red = (col & 0x00FF0000) >> 16;
                int green = (col & 0x0000FF00) >> 8;
                int blue = (col & 0x000000FF);
                // 增加了图像的亮度
                red = (int) (1.1 * red + 30);
                green = (int) (1.1 * green + 30);
                blue = (int) (1.1 * blue + 30);
                // 对图像像素越界进行处理
                if (red >= 255) {
                    red = 255;
                }

                if (green >= 255) {
                    green = 255;
                }

                if (blue >= 255) {
                    blue = 255;
                }
                // 新的ARGB
                int newColor = alpha | (red << 16) | (green << 8) | blue;
                // 设置新图像的RGB值
                linegray.setPixel(i, j, newColor);
            }
        }
        return linegray;
    }

    /**
     * 图像二值化处理
     * @param graymap
     * */
    public Bitmap gray2Binary(Bitmap graymap) {
        // 得到图形的宽度和长度
        int width = graymap.getWidth();
        int height = graymap.getHeight();
        // 创建二值化图像
        Bitmap binarymap = null;
        binarymap = graymap.copy(Config.ARGB_8888, true);
        // 依次循环，对图像的像素进行处理
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // 得到当前像素的值
                int col = binarymap.getPixel(i, j);
                // 得到alpha通道的值
                int alpha = col & 0xFF000000;
                // 得到图像的像素RGB的值
                int red = (col & 0x00FF0000) >> 16;
                int green = (col & 0x0000FF00) >> 8;
                int blue = (col & 0x000000FF);
                // 用公式X = 0.3×R+0.59×G+0.11×B计算出X代替原来的RGB
                int gray = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                // 对图像进行二值化处理
                if (gray <= 95) {
                    gray = 0;
                } else {
                    gray = 255;
                }
                // 新的ARGB
                int newColor = alpha | (gray << 16) | (gray << 8) | gray;
                // 设置新图像的当前像素值
                binarymap.setPixel(i, j, newColor);
            }
        }
        return binarymap;
    }

    /**
     * 高斯模糊
     * @param bmp
     * @return
     */
    public static Bitmap convertToBlur(Bitmap bmp) {
        // 高斯矩阵
        int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap newBmp = Bitmap.createBitmap(width, height,
                Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int delta = 16; // 值越小图片会越亮，越大则越暗
        int idx = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);
                        newR = newR + pixR * gauss[idx];
                        newG = newG + pixG * gauss[idx];
                        newB = newB + pixB * gauss[idx];
                        idx++;
                    }
                }
                newR /= delta;
                newG /= delta;
                newB /= delta;
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBmp;
    }

    /**
     * 素描效果
     * @param bmp
     * @return
     */
    public static Bitmap convertToSketch(Bitmap bmp) {
        int pos, row, col, clr;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixSrc = new int[width * height];
        int[] pixNvt = new int[width * height];
        // 先对图象的像素处理成灰度颜色后再取反
        bmp.getPixels(pixSrc, 0, width, 0, 0, width, height);
        for (row = 0; row < height; row++) {
            for (col = 0; col < width; col++) {
                pos = row * width + col;
                pixSrc[pos] = (Color.red(pixSrc[pos])
                        + Color.green(pixSrc[pos]) + Color.blue(pixSrc[pos])) / 3;
                pixNvt[pos] = 255 - pixSrc[pos];
            }
        }
        // 对取反的像素进行高斯模糊, 强度可以设置，暂定为5.0
        gaussGray(pixNvt, 5.0, 5.0, width, height);
        // 灰度颜色和模糊后像素进行差值运算
        for (row = 0; row < height; row++) {
            for (col = 0; col < width; col++) {
                pos = row * width + col;
                clr = pixSrc[pos] << 8;
                clr /= 256 - pixNvt[pos];
                clr = Math.min(clr, 255);
                pixSrc[pos] = Color.rgb(clr, clr, clr);
            }
        }
        bmp.setPixels(pixSrc, 0, width, 0, 0, width, height);
        return bmp;
    }

    private static int gaussGray(int[] psrc, double horz, double vert, int width, int height) {
        int[] dst, src;
        double[] n_p, n_m, d_p, d_m, bd_p, bd_m;
        double[] val_p, val_m;
        int i, j, t, k, row, col, terms;
        int[] initial_p, initial_m;
        double std_dev;
        int row_stride = width;
        int max_len = Math.max(width, height);
        int sp_p_idx, sp_m_idx, vp_idx, vm_idx;
        val_p = new double[max_len];
        val_m = new double[max_len];
        n_p = new double[5];
        n_m = new double[5];
        d_p = new double[5];
        d_m = new double[5];
        bd_p = new double[5];
        bd_m = new double[5];
        src = new int[max_len];
        dst = new int[max_len];
        initial_p = new int[4];
        initial_m = new int[4];
        // 垂直方向
        if (vert > 0.0) {
            vert = Math.abs(vert) + 1.0;
            std_dev = Math.sqrt(-(vert * vert) / (2 * Math.log(1.0 / 255.0)));
            // 初试化常量
            findConstants(n_p, n_m, d_p, d_m, bd_p, bd_m, std_dev);
            for (col = 0; col < width; col++) {
                for (k = 0; k < max_len; k++) {
                    val_m[k] = val_p[k] = 0;
                }
                for (t = 0; t < height; t++) {
                    src[t] = psrc[t * row_stride + col];
                }
                sp_p_idx = 0;
                sp_m_idx = height - 1;
                vp_idx = 0;
                vm_idx = height - 1;
                initial_p[0] = src[0];
                initial_m[0] = src[height - 1];
                for (row = 0; row < height; row++) {
                    terms = (row < 4) ? row : 4;
                    for (i = 0; i <= terms; i++) {
                        val_p[vp_idx] += n_p[i] * src[sp_p_idx - i] - d_p[i]
                                * val_p[vp_idx - i];
                        val_m[vm_idx] += n_m[i] * src[sp_m_idx + i] - d_m[i]
                                * val_m[vm_idx + i];
                    }
                    for (j = i; j <= 4; j++) {
                        val_p[vp_idx] += (n_p[j] - bd_p[j]) * initial_p[0];
                        val_m[vm_idx] += (n_m[j] - bd_m[j]) * initial_m[0];
                    }
                    sp_p_idx++;
                    sp_m_idx--;
                    vp_idx++;
                    vm_idx--;
                }
                transferGaussPixels(val_p, val_m, dst, 1, height);
                for (t = 0; t < height; t++) {
                    psrc[t * row_stride + col] = dst[t];
                }
            }
        }
        // 水平方向
        if (horz > 0.0) {
            horz = Math.abs(horz) + 1.0;
            if (horz != vert) {
                std_dev = Math.sqrt(-(horz * horz)
                        / (2 * Math.log(1.0 / 255.0)));
                // 初试化常量
                findConstants(n_p, n_m, d_p, d_m, bd_p, bd_m, std_dev);
            }
            for (row = 0; row < height; row++) {
                for (k = 0; k < max_len; k++) {
                    val_m[k] = val_p[k] = 0;
                }
                for (t = 0; t < width; t++) {
                    src[t] = psrc[row * row_stride + t];
                }
                sp_p_idx = 0;
                sp_m_idx = width - 1;
                vp_idx = 0;
                vm_idx = width - 1;
                initial_p[0] = src[0];
                initial_m[0] = src[width - 1];
                for (col = 0; col < width; col++) {
                    terms = (col < 4) ? col : 4;
                    for (i = 0; i <= terms; i++) {
                        val_p[vp_idx] += n_p[i] * src[sp_p_idx - i] - d_p[i]
                                * val_p[vp_idx - i];
                        val_m[vm_idx] += n_m[i] * src[sp_m_idx + i] - d_m[i]
                                * val_m[vm_idx + i];
                    }
                    for (j = i; j <= 4; j++) {
                        val_p[vp_idx] += (n_p[j] - bd_p[j]) * initial_p[0];
                        val_m[vm_idx] += (n_m[j] - bd_m[j]) * initial_m[0];
                    }
                    sp_p_idx++;
                    sp_m_idx--;
                    vp_idx++;
                    vm_idx--;
                }
                transferGaussPixels(val_p, val_m, dst, 1, width);
                for (t = 0; t < width; t++) {
                    psrc[row * row_stride + t] = dst[t];
                }
            }
        }
        return 0;
    }

    private static void transferGaussPixels(double[] src1, double[] src2,
                                            int[] dest, int bytes, int width) {
        int i, j, k, b;
        int bend = bytes * width;
        double sum;
        i = j = k = 0;
        for (b = 0; b < bend; b++) {
            sum = src1[i++] + src2[j++];
            if (sum > 255)
                sum = 255;
            else if (sum < 0)
                sum = 0;
            dest[k++] = (int) sum;
        }
    }

    private static void findConstants(double[] n_p, double[] n_m, double[] d_p,
                                      double[] d_m, double[] bd_p, double[] bd_m, double std_dev) {
        double div = Math.sqrt(2 * 3.141593) * std_dev;
        double x0 = -1.783 / std_dev;
        double x1 = -1.723 / std_dev;
        double x2 = 0.6318 / std_dev;
        double x3 = 1.997 / std_dev;
        double x4 = 1.6803 / div;
        double x5 = 3.735 / div;
        double x6 = -0.6803 / div;
        double x7 = -0.2598 / div;
        int i;
        n_p[0] = x4 + x6;
        n_p[1] = (Math.exp(x1)
                * (x7 * Math.sin(x3) - (x6 + 2 * x4) * Math.cos(x3)) + Math
                .exp(x0) * (x5 * Math.sin(x2) - (2 * x6 + x4) * Math.cos(x2)));
        n_p[2] = (2
                * Math.exp(x0 + x1)
                * ((x4 + x6) * Math.cos(x3) * Math.cos(x2) - x5 * Math.cos(x3)
                * Math.sin(x2) - x7 * Math.cos(x2) * Math.sin(x3)) + x6
                * Math.exp(2 * x0) + x4 * Math.exp(2 * x1));
        n_p[3] = (Math.exp(x1 + 2 * x0)
                * (x7 * Math.sin(x3) - x6 * Math.cos(x3)) + Math.exp(x0 + 2
                * x1)
                * (x5 * Math.sin(x2) - x4 * Math.cos(x2)));
        n_p[4] = 0.0;
        d_p[0] = 0.0;
        d_p[1] = -2 * Math.exp(x1) * Math.cos(x3) - 2 * Math.exp(x0)
                * Math.cos(x2);
        d_p[2] = 4 * Math.cos(x3) * Math.cos(x2) * Math.exp(x0 + x1)
                + Math.exp(2 * x1) + Math.exp(2 * x0);
        d_p[3] = -2 * Math.cos(x2) * Math.exp(x0 + 2 * x1) - 2 * Math.cos(x3)
                * Math.exp(x1 + 2 * x0);
        d_p[4] = Math.exp(2 * x0 + 2 * x1);
        for (i = 0; i <= 4; i++) {
            d_m[i] = d_p[i];
        }
        n_m[0] = 0.0;
        for (i = 1; i <= 4; i++) {
            n_m[i] = n_p[i] - d_p[i] * n_p[0];
        }
        double sum_n_p, sum_n_m, sum_d;
        double a, b;
        sum_n_p = 0.0;
        sum_n_m = 0.0;
        sum_d = 0.0;
        for (i = 0; i <= 4; i++) {
            sum_n_p += n_p[i];
            sum_n_m += n_m[i];
            sum_d += d_p[i];
        }
        a = sum_n_p / (1.0 + sum_d);
        b = sum_n_m / (1.0 + sum_d);
        for (i = 0; i <= 4; i++) {
            bd_p[i] = d_p[i] * a;
            bd_m[i] = d_m[i] * b;
        }
    }


    /**
     * 图片锐化（拉普拉斯变换）
     * @param bmp
     * @return
     */
    public static Bitmap sharpenImageAmeliorate(Bitmap bmp) {
        // 拉普拉斯矩阵
        int[] laplacian = new int[] { -1, -1, -1, -1, 9, -1, -1, -1, -1 };
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int idx = 0;
        float alpha = 0.3F;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + n) * width + k + m];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);
                        newR = newR + (int) (pixR * laplacian[idx] * alpha);
                        newG = newG + (int) (pixG * laplacian[idx] * alpha);
                        newB = newB + (int) (pixB * laplacian[idx] * alpha);
                        idx++;
                    }
                }
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 图片复古
     * @param bmp
     * @return
     */
    private Bitmap OldRemeberImage(Bitmap bmp)
    {
        /*
         * 怀旧处理算法即设置新的RGB
         * R=0.393r+0.769g+0.189b
         * G=0.349r+0.686g+0.168b
         * B=0.272r+0.534g+0.131b
         */
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++)
        {
            for (int k = 0; k < width; k++)
            {
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
                newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
                newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
                int newColor = Color.argb(255, newR > 255 ? 255 : newR, newG > 255 ? 255 : newG, newB > 255 ? 255 : newB);
                pixels[width * i + k] = newColor;
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 图片浮雕
     * 将当前像素点的RGB值分别与255之差后的值作为当前点的RGB
     * 灰度图像:通常使用的方法是gray=0.3*pixR+0.59*pixG+0.11*pixB
     * @param bmp
     * @return
     */
    private Bitmap ReliefImage(Bitmap bmp)
    {
        /*
         * 算法原理：(前一个像素点RGB-当前像素点RGB+127)作为当前像素点RGB值
         * 在ABC中计算B点浮雕效果(RGB值在0~255)
         * B.r = C.r - B.r + 127
         * B.g = C.g - B.g + 127
         * B.b = C.b - B.b + 127
         */
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1; i < height-1; i++)
        {
            for (int k = 1; k < width-1; k++)
            {
                //获取前一个像素颜色
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                //获取当前像素
                pixColor = pixels[(width * i + k) + 1];
                newR = Color.red(pixColor) - pixR +127;
                newG = Color.green(pixColor) - pixG +127;
                newB = Color.blue(pixColor) - pixB +127;
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[width * i + k] = Color.argb(255, newR, newG, newB);
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }


    /**
     * 图片光照效果
     * @param bmp
     * @return
     */
    private Bitmap SunshineImage(Bitmap bmp)
    {
        /*
         * 算法原理：(前一个像素点RGB-当前像素点RGB+127)作为当前像素点RGB值
         * 在ABC中计算B点浮雕效果(RGB值在0~255)
         * B.r = C.r - B.r + 127
         * B.g = C.g - B.g + 127
         * B.b = C.b - B.b + 127
         * 光照中心取长宽较小值为半径,也可以自定义从左上角射过来
         */
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        //围绕圆形光照
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = Math.min(centerX, centerY);
        float strength = 150F;  //光照强度100-150
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1; i < height-1; i++)
        {
            for (int k = 1; k < width-1; k++)
            {
                //获取前一个像素颜色
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = pixR;
                newG = pixG;
                newB = pixB;
                //计算当前点到光照中心的距离,平面坐标系中两点之间的距离
                int distance = (int) (Math.pow((centerY-i), 2) + Math.pow((centerX-k), 2));
                if(distance < radius*radius)
                {
                    //按照距离大小计算增强的光照值
                    int result = (int)(strength*( 1.0-Math.sqrt(distance) / radius ));
                    newR = pixR + result;
                    newG = newG + result;
                    newB = pixB + result;
                }
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[width * i + k] = Color.argb(255, newR, newG, newB);
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 图片冰冻效果
     * @param bmp
     * @return
     */
    private Bitmap IceImage(Bitmap bmp)
    {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newColor = 0;
        int newR = 0;
        int newG = 0;
        int newB =0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++)
        {
            for (int k = 0; k < width; k++)
            {
                //获取前一个像素颜色
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                //红色
                newColor = pixR - pixG - pixB;
                newColor = newColor * 3 / 2;
                if(newColor < 0) {
                    newColor = -newColor;
                }
                if(newColor >255) {
                    newColor = 255;
                }
                newR = newColor;
                //绿色
                newColor = pixG - pixB - pixR;
                newColor = newColor * 3 / 2;
                if(newColor < 0) {
                    newColor = -newColor;
                }
                if(newColor >255) {
                    newColor = 255;
                }
                newG = newColor;
                //蓝色
                newColor = pixB - pixG - pixR;
                newColor = newColor * 3 / 2;
                if(newColor < 0) {
                    newColor = -newColor;
                }
                if(newColor >255) {
                    newColor = 255;
                }
                newB = newColor;
                pixels[width * i + k] = Color.argb(255, newR, newG, newB);
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 倒影图片
     * @param mBitmapSrc 图片源
     * @return Bitmap
     */
    public Bitmap toReflectedImage(Bitmap mBitmapSrc) {
        final int reflectionGap = 4;
        int width = mBitmapSrc.getWidth();
        int height = mBitmapSrc.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        Bitmap reflectionImage = Bitmap.createBitmap(mBitmapSrc, 0,
                height / 2, width, height / 2, matrix, false);
        Bitmap bitmap = Bitmap.createBitmap(width,
                (height + height / 2), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(mBitmapSrc, 0, 0, null);
        Paint defaultPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0,
                mBitmapSrc.getHeight(), 0, bitmap.getHeight()
                + reflectionGap, 0x70FFFFFF, 0x00FFFFFF,
                Shader.TileMode.MIRROR);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawRect(0, height, width, bitmap.getHeight()
                + reflectionGap, paint);
        return bitmap;
    }

    /**
     * 黑白照片
     * @param mBitmapSrc 图片源
     * @return Bitmap
     */
    public Bitmap toBlackAndWhite(Bitmap mBitmapSrc) {
        int mBitmapWidth;
        int mBitmapHeight;

        mBitmapWidth = mBitmapSrc.getWidth();
        mBitmapHeight = mBitmapSrc.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight,
                Config.ARGB_8888);
        int iPixel;
        for (int i = 0; i < mBitmapWidth; i++) {
            for (int j = 0; j < mBitmapHeight; j++) {
                int curr_color = mBitmapSrc.getPixel(i, j);

                int avg = (Color.red(curr_color) + Color.green(curr_color) + Color
                        .blue(curr_color)) / 3;
                if (avg >= 100) {
                    iPixel = 255;
                } else {
                    iPixel = 0;
                }
                int modify_color = Color.argb(255, iPixel, iPixel, iPixel);

                bitmap.setPixel(i, j, modify_color);
            }
        }
        return bitmap;
    }

    /**
     * 底片效果
     * @param mBitmapSrc 图片源
     * @return Bitmap
     */
    private Bitmap negativeFilm(Bitmap mBitmapSrc) {
        // RGBA的最大值
        final int MAX_VALUE = 255;
        int width = mBitmapSrc.getWidth();
        int height = mBitmapSrc.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.RGB_565);

        int pixR;
        int pixG;
        int pixB;

        int pixColor;

        int newR;
        int newG;
        int newB;

        int[] pixels = new int[width * height];
        mBitmapSrc.getPixels(pixels, 0, width, 0, 0, width, height);
        int pos = 0;
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                pos = i * width + k;
                pixColor = pixels[pos];

                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);

                newR = MAX_VALUE - pixR;
                newG = MAX_VALUE - pixG;
                newB = MAX_VALUE - pixB;

                newR = Math.min(MAX_VALUE, Math.max(0, newR));
                newG = Math.min(MAX_VALUE, Math.max(0, newG));
                newB = Math.min(MAX_VALUE, Math.max(0, newB));

                pixels[pos] = Color.argb(MAX_VALUE, newR, newG, newB);
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 油画效果
     * @param mBitmapSrc 图片源
     * @return Bitmap
     */
    public Bitmap oilPainting(Bitmap mBitmapSrc) {
        Bitmap bmpReturn = Bitmap.createBitmap(mBitmapSrc.getWidth(),
                mBitmapSrc.getHeight(), Config.RGB_565);
        int color = 0;
        int Radio = 0;
        int width = mBitmapSrc.getWidth();
        int height = mBitmapSrc.getHeight();

        Random rnd = new Random();
        int iModel = 10;
        int i = width - iModel;
        while (i > 1) {
            int j = height - iModel;
            while (j > 1) {
                int iPos = rnd.nextInt(100000) % iModel;
                color = mBitmapSrc.getPixel(i + iPos, j + iPos);
                bmpReturn.setPixel(i, j, color);
                j = j - 1;
            }
            i = i - 1;
        }
        return bmpReturn;
    }


    /**
     * 图片合成
     * @param position   组合位置： -1 ：左   1 ：右   2 ：上   -2 ：下
     * @param mBitmapSrcs 图片源
     * @return Bitmap
     */
    public Bitmap photoMix(Position position, Bitmap... mBitmapSrcs) {
        if (mBitmapSrcs.length <= 0) {
            return null;
        }
        if (mBitmapSrcs.length == 1) {
            return mBitmapSrcs[0];
        }
        Bitmap newBitmap = mBitmapSrcs[0];

        for (int i = 1; i < mBitmapSrcs.length; i++) {
            newBitmap = createBitmapForPhotoMix(newBitmap, mBitmapSrcs[i], position);
        }
        return newBitmap;
    }

    private Bitmap createBitmapForPhotoMix(Bitmap first, Bitmap second, Position position) {
        if (first == null) {
            return null;
        }
        if (second == null) {
            return first;
        }
        int fw = first.getWidth();
        int fh = first.getHeight();
        int sw = second.getWidth();
        int sh = second.getHeight();
        Bitmap newBitmap = null;
        if (position == Position.LEFT) {
            newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh, Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, sw, 0, null);
            canvas.drawBitmap(second, 0, 0, null);
        } else if (position == Position.RIGHT) {
            newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh, Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, 0, null);
            canvas.drawBitmap(second, fw, 0, null);
        } else if (position == Position.TOP) {
            newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh, Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, sh, null);
            canvas.drawBitmap(second, 0, 0, null);
        } else if (position ==  Position.BOTTOM) {
            newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh, Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, 0, null);
            canvas.drawBitmap(second, 0, fh, null);
        } else if (position ==  Position.CENTRE) {
            newBitmap = Bitmap.createBitmap(Math.max(fw, sw), Math.max(fw, sw), Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, 0, null);
            canvas.drawBitmap(second, fw / 2, fh / 2, null);
        }
        return newBitmap;
    }

    /**
     * 位置 上下左右中 左上角 左下角 右上角 右下角 中间
     * */
    public enum Position {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
        CENTRE,
        LEFT_UP,
        LEFT_DOWN,
        RIGHT_UP,
        RIGHT_DOWN,
        CENTER;
    }
    /**
     * 图片格式
     * */
    public enum Format {
        JPEG,
        PNG,
        WEBP;
    }

    /**
     * 图片平滑处理
     * 3*3掩模处理（平均处理），降低噪声
     * @param mBitmapSrc 图片源
     * @return Bitmap
     */
    public Bitmap smoothImage(Bitmap mBitmapSrc) {
        int w = mBitmapSrc.getWidth();
        int h = mBitmapSrc.getHeight();
        int[] data = new int[w * h];
        mBitmapSrc.getPixels(data, 0, w, 0, 0, w, h);
        int[] resultData = new int[w * h];
        try {
            resultData = filter(data, w, h);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap newBitmap = Bitmap.createBitmap(resultData, w, h, Config.ARGB_8888);
        return newBitmap;
    }

    private int[] filter(int[] data, int width, int height) throws Exception {
        int filterData[] = new int[data.length];
        int min = 10000;
        int max = -10000;
        if (data.length != width * height) return filterData;
        try {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (i == 0 || i == 1 || i == height - 1 || i == height - 2 || j == 0 || j == 1 || j == width - 1 || j == width - 2) {
                        filterData[i * width + j] = data[i * width + j];
                    } else {
                        double average;             //中心的九个像素点
                        average = (data[i * width + j] + data[i * width + j - 1] + data[i * width + j + 1]
                                + data[(i - 1) * width + j] + data[(i - 1) * width + j - 1] + data[(i - 1) * width + j + 1]
                                + data[(i + 1) * width + j] + data[(i + 1) * width + j - 1] + data[(i + 1) * width + j + 1]) / 9;
                        filterData[i * width + j] = (int) (average);
                    }
                    if (filterData[i * width + j] < min)
                        min = filterData[i * width + j];
                    if (filterData[i * width + j] > max)
                        max = filterData[i * width + j];
                }
            }
            for (int i = 0; i < width * height; i++) {
                filterData[i] = (filterData[i] - min) * 255 / (max - min);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return filterData;
    }


    /**
     * 图片增亮
     * @param mBitmapSrc     图片源
     * @param brightenOffset 增加的亮度值
     * @return Bitmap
     */
    public Bitmap brightenBitmap(Bitmap mBitmapSrc, int brightenOffset) {
        int width = mBitmapSrc.getWidth();
        int height = mBitmapSrc.getHeight();
        int[] pix = new int[width * height];
        mBitmapSrc.getPixels(pix, 0, width, 0, 0, width, height);

        // Apply pixel-by-pixel change
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = (pix[index] >> 16) & 0xff;
                int g = (pix[index] >> 8) & 0xff;
                int b = pix[index] & 0xff;
                r = Math.max(0, Math.min(255, r + brightenOffset));
                g = Math.max(0, Math.min(255, g + brightenOffset));
                b = Math.max(0, Math.min(255, b + brightenOffset));
                pix[index] = 0xff000000 | (r << 16) | (g << 8) | b;
                index++;
            } // x
        } // y

        // Change bitmap to use new array
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
        bitmap.setPixels(pix, 0, width, 0, 0, width, height);
        mBitmapSrc = null;
        pix = null;
        return bitmap;
    }

    /**
     * 均值滤波
     * @param mBitmapSrc   图片源
     * @param filterWidth  滤波宽度值
     * @param filterHeight 滤波高度值
     */
    public Bitmap averageFilter(Bitmap mBitmapSrc, int filterWidth, int filterHeight) {
        int width = mBitmapSrc.getWidth();
        int height = mBitmapSrc.getHeight();
        int[] pixNew = new int[width * height];
        int[] pixOld = new int[width * height];
        mBitmapSrc.getPixels(pixNew, 0, width, 0, 0, width, height);
        mBitmapSrc.getPixels(pixOld, 0, width, 0, 0, width, height);

        // Apply pixel-by-pixel change
        int filterHalfWidth = filterWidth / 2;
        int filterHalfHeight = filterHeight / 2;
        int filterArea = filterWidth * filterHeight;
        for (int y = filterHalfHeight; y < height - filterHalfHeight; y++) {
            for (int x = filterHalfWidth; x < width - filterHalfWidth; x++) {
                // Accumulate values in neighborhood
                int accumR = 0, accumG = 0, accumB = 0;
                for (int dy = -filterHalfHeight; dy <= filterHalfHeight; dy++) {
                    for (int dx = -filterHalfWidth; dx <= filterHalfWidth; dx++) {
                        int index = (y + dy) * width + (x + dx);
                        accumR += (pixOld[index] >> 16) & 0xff;
                        accumG += (pixOld[index] >> 8) & 0xff;
                        accumB += pixOld[index] & 0xff;
                    } // dx
                } // dy

                // Normalize
                accumR /= filterArea;
                accumG /= filterArea;
                accumB /= filterArea;
                int index = y * width + x;
                pixNew[index] = 0xff000000 | (accumR << 16) | (accumG << 8) | accumB;
            } // x
        } // y

        // Change bitmap to use new array
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
        bitmap.setPixels(pixNew, 0, width, 0, 0, width, height);
        mBitmapSrc = null;
        pixOld = null;
        pixNew = null;
        return bitmap;
    }

    /**
     * 中值滤波
     * @param mBitmapSrc   图片源
     * @param filterWidth  滤波宽度值
     * @param filterHeight 滤波高度值
     */
    public Bitmap medianFilter(Bitmap mBitmapSrc, int filterWidth, int filterHeight) {
        int width = mBitmapSrc.getWidth();
        int height = mBitmapSrc.getHeight();
        int[] pixNew = new int[width * height];
        int[] pixOld = new int[width * height];
        mBitmapSrc.getPixels(pixNew, 0, width, 0, 0, width, height);
        mBitmapSrc.getPixels(pixOld, 0, width, 0, 0, width, height);

        // Apply pixel-by-pixel change
        int filterHalfWidth = filterWidth / 2;
        int filterHalfHeight = filterHeight / 2;
        int filterArea = filterWidth * filterHeight;
        for (int y = filterHalfHeight; y < height - filterHalfHeight; y++) {
            for (int x = filterHalfWidth; x < width - filterHalfWidth; x++) {
                // Accumulate values in neighborhood
                int accumR = 0, accumG = 0, accumB = 0;
                for (int dy = -filterHalfHeight; dy <= filterHalfHeight; dy++) {
                    for (int dx = -filterHalfWidth; dx <= filterHalfWidth; dx++) {
                        int index = (y + dy) * width + (x + dx);
                        accumR += (pixOld[index] >> 16) & 0xff;
                        accumG += (pixOld[index] >> 8) & 0xff;
                        accumB += pixOld[index] & 0xff;
                    } // dx
                } // dy

                // Normalize
                accumR /= filterArea;
                accumG /= filterArea;
                accumB /= filterArea;
                int index = y * width + x;
                pixNew[index] = 0xff000000 | (accumR << 16) | (accumG << 8) | accumB;
            } // x
        } // y

        // Change bitmap to use new array
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
        bitmap.setPixels(pixNew, 0, width, 0, 0, width, height);
        mBitmapSrc = null;
        pixOld = null;
        pixNew = null;
        return bitmap;
    }



    /**
     * 图像上添加水印，添加的是图像
     * @param bitmap
     * @param mark
     * @param x
     * @param y
     * @return
     */
    public static Bitmap watermarkWithBmp(Bitmap bitmap, Bitmap mark, int x, int y)
    {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap newb = Bitmap.createBitmap(w, h, Config.RGB_565);
        Canvas cv = new Canvas(newb);
        cv.drawBitmap(bitmap, 0, 0, null);
        cv.drawBitmap(mark, x, y, null);  //系需要先画一幅图，再画另一幅图就可以
        cv.save();
        cv.restore();//存储
        return newb;
    }


    /**
     * 图像上添加水印，添加的是文字
     * @param bitmap
     * @param text
     * @param x
     * @param y
     * @param size
     * @param color
     * @return
     */
    public static Bitmap watermarkWithText(Bitmap bitmap, String text, int x, int y, int size, int color)
    {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap newb = Bitmap.createBitmap(w, h, Config.RGB_565);
        Canvas cv = new Canvas(newb);
        cv.drawBitmap(bitmap, 0, 0, null);
        Paint p = new Paint();
        p.setARGB(250, 255, 255, 255);
        p.setAntiAlias(true);//去除锯齿
        p.setFilterBitmap(true);//对位图进行滤波处理
        p.setTextSize(size);
        cv.drawText(text, x, y, p);  //也是先画原图，再画文字
        cv.save();
        cv.restore();
        return newb;
    }


}