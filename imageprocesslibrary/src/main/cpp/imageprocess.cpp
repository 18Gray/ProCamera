#include <jni.h>
#include <string.h>
#include <android/bitmap.h>
#include<malloc.h>
#include <stdio.h>
#include <setjmp.h>
#include <stdint.h>
#include <time.h>
#include "com_eighteengray_imageprocesslibrary_ImageProcessJni.h"

#define ABS(a) ((a)<(0)?(-a):(a))
#define MAX(a,b) ((a)>(b)?(a):(b))
#define MIN(a,b) ((a)<(b)?(a):(b))

JNIEXPORT jintArray JNICALL Java_com_eighteengray_imageprocesslibrary_ImageProcessJni_toGray
        (JNIEnv *env, jclass jclazz, jintArray buf, jint w, jint h){
    jint * cbuf;
    cbuf = env->GetIntArrayElements(buf, 0); // 获取int数组元素

    int alpha = 0xFF; // 不透明值
    int i, j, color, red, green, blue;
    for (i = 0; i < h; i++) {
        for (j = 0; j < w; j++) {
            color = cbuf[w * i + j]; // 获得color值
            red = (color >> 16) & 0xFF; // 获得red值
            green = (color >> 8) & 0xFF; // 获得green值
            blue = color & 0xFF; // 获得blue值
            color = (red * 38 + green * 75 + blue * 15) >> 7; // 灰度算法（16位运算下7位精度）
            color = (alpha << 24) | (color << 16) | (color << 8) | color; // 由ARGB组成新的color值
            cbuf[w * i + j] = color; // 设置新color值
        }
    }

    int size = w * h;
    jintArray result = env->NewIntArray(size); // 新建一个jintArray
    env->SetIntArrayRegion(result, 0, size, cbuf); // 将cbuf转存入result
    env->ReleaseIntArrayElements(buf, cbuf, 0); // 释放int数组元素
    return result;
}


/**
 * OTSU算法求最适分割阈值
 */
int otsu(jint* colors, int w, int h) {
    unsigned int pixelNum[256]; // 图象灰度直方图[0, 255]
    int color; // 灰度值
    int n, n0, n1; //  图像总点数，前景点数， 后景点数（n0 + n1 = n）
    int w0, w1; // 前景所占比例， 后景所占比例（w0 = n0 / n, w0 + w1 = 1）
    double u, u0, u1; // 总平均灰度，前景平均灰度，后景平均灰度（u = w0 * u0 + w1 * u1）
    double g, gMax; // 图像类间方差，最大类间方差（g = w0*(u0-u)^2+w1*(u1-u)^2 = w0*w1*(u0-u1)^2）
    double sum_u, sum_u0, sum_u1; // 图像灰度总和，前景灰度总和， 后景平均总和（sum_u = n * u）
    int thresh; // 阈值

    memset(pixelNum, 0, 256 * sizeof(unsigned int)); // 数组置0

    // 统计各灰度数目
    int i, j;
    for (i = 0; i < h; i++) {
        for (j = 0; j < w; j++) {
            color = (colors[w * i + j]) & 0xFF; // 获得灰度值
            pixelNum[color]++; // 相应灰度数目加1
        }
    }

    // 图像总点数
    n = w * h;

    // 计算总灰度
    int k;
    for (k = 0; k <= 255; k++) {
        sum_u += k * pixelNum[k];
    }

    // 遍历判断最大类间方差，得到最佳阈值
    for (k = 0; k <= 255; k++) {
        n0 += pixelNum[k]; // 图像前景点数
        if (0 == n0) { // 未获取前景，直接继续增加前景点数
            continue;
        }
        if (n == n0) { // 前景点数包括了全部时，不可能再增加，退出循环
            break;
        }
        n1 = n - n0; // 图像后景点数

        sum_u0 += k * pixelNum[k]; // 前景灰度总和
        u0 = sum_u0 / n0; // 前景平均灰度
        u1 = (sum_u - sum_u0) / n1; // 后景平均灰度

        g = n0 * n1 * (u0 - u1) * (u0 - u1); // 类间方差（少除了n^2）

        if (g > gMax) { // 大于最大类间方差时
            gMax = g; // 设置最大类间方差
            thresh = k; // 取最大类间方差时对应的灰度的k就是最佳阈值
        }
    }

    return thresh;
}

JNIEXPORT jintArray JNICALL Java_com_eighteengray_imageprocesslibrary_ImageProcessJni_toWhiteBlack
        (JNIEnv *env, jclass jclazz, jintArray buf, jint w, jint h){
    jint * cbuf;
    cbuf = env->GetIntArrayElements(buf, 0); // 获取int数组元素

    int white = 0xFFFFFFFF; // 不透明白色
    int black = 0xFF000000; // 不透明黑色
    int thresh = otsu(cbuf, w, h); // OTSU获取分割阀值

    int i, j, gray;
    for (i = 0; i < h; i++) {
        for (j = 0; j < w; j++) {
            gray = (cbuf[w * i + j]) & 0xFF; // 获得灰度值（red=green=blue）
            if (gray < thresh) {
                cbuf[w * i + j] = white; // 小于阀值设置为白色（前景）
            } else {
                cbuf[w * i + j] = black; // 否则设置为黑色（背景）
            }
        }
    }

    int size = w * h;
    jintArray result = env->NewIntArray(size); // 新建一个jintArray
    env->SetIntArrayRegion(result, 0, size, cbuf); // 将cbuf转存入result
    env->ReleaseIntArrayElements(buf, cbuf, 0); // 释放int数组元素
    return result;
}

JNIEXPORT jintArray JNICALL Java_com_eighteengray_imageprocesslibrary_ImageProcessJni_toCameo
        (JNIEnv *env, jclass jclazz, jintArray buf, jint width, jint height){

}


int min(int x, int y) {
    return (x <= y) ? x : y;
}
int alpha(int color) {
    return (color >> 24) & 0xFF;
}
int red(int color) {
    return (color >> 16) & 0xFF;
}
int green(int color) {
    return (color >> 8) & 0xFF;
}
int blue(int color) {
    return color & 0xFF;
}
int ARGB(int alpha, int red, int green, int blue) {
    return (alpha << 24) | (red << 16) | (green << 8) | blue;
}

/**
 * 按双线性内插值算法将对应源图像四点颜色某一颜色值混合
 * int(*fun)(int)指向从color中获取某一颜色值的方法
 */
int mixARGB(int *color, int i, int j, float u, float v, int(*fun)(int)) {
    // f(i+u,j+v)=(1-u)(1-v)*f(i,j)+(1-u)v*f(i,j+1)+u(1-v)*f(i+1,j)+uv*f(i+1,j+1)
    return (1 - u) * (1 - v) * (*fun)(color[0]) + (1 - u) * v * (*fun)(color[1])
           + u * (1 - v) * (*fun)(color[2]) + u * v * (*fun)(color[3]);
}

/**
 * 按双线性内插值算法将对应源图像四点颜色值混合
 * color[]需要有四个颜色值，避免越界
 */
int mixColor(int *color, int i, int j, float u, float v) {
    int a = mixARGB(color, i, j, u, v, alpha); // 获取alpha混合值
    int r = mixARGB(color, i, j, u, v, red); // 获取red混合值
    int g = mixARGB(color, i, j, u, v, green); // 获取green混合值
    int b = mixARGB(color, i, j, u, v, blue); // 获取blue混合值
    return ARGB(a, r, g, b);
}

JNIEXPORT jintArray JNICALL Java_com_eighteengray_imageprocesslibrary_ImageProcessJni_zoom
        (JNIEnv *env, jclass jclazz, jintArray buf, jint srcW, jint srcH, jint dstW, jint dstH){
    jint * cbuf;
    cbuf = env->GetIntArrayElements(buf, 0); // 获取int数组元素

    int newSize = dstW * dstH;
    jint rbuf[newSize]; // 新图像像素值

    float rateH = (float) srcH / dstH; // 高度缩放比例
    float rateW = (float) srcW / dstW; // 宽度缩放比例

    int dstX, dstY; // 目标图像XY坐标
    float srcX, srcY; // 目标图像对应源图像XY坐标
    int i, j; // 对应源图像XY坐标整数部分
    int i1, j1; // 对应源图像XY坐标整数部分+1
    float u, v; // 对应源图像XY坐标小数部分
    int color[4]; // f(i+u,j+v)对应源图像(i,j)、(i+1,j)、(i,j+1)、(i+1,j+1)的像素值

    for (dstY = 0; dstY <= dstH - 1; dstY++) {

        srcY = dstY * rateH; // 对应源图像Y坐标
        j = (int) srcY; // 对应源图像Y坐标整数部分
        j1 = min(j + 1, srcH - 1); // 对应源图像Y坐标整数部分+1
        v = srcY - j; // 对应源图像Y坐标小数部分

        for (dstX = 0; dstX <= dstW - 1; dstX++) {

            srcX = dstX * rateW; // 对应源图像X坐标
            i = (int) srcX; // 对应源图像X坐标整数部分
            i1 = min(i + 1, srcW - 1); // 对应源图像X坐标整数部分+1
            u = srcX - i; // 对应源图像X坐标小数部分

            // 双线性内插值算法（注意ARGB时，需要分别由插值算法求得后重组）:
            // f(i+u,j+v)=(1-u)(1-v)*f(i,j)+(1-u)v*f(i,j+1)+u(1-v)*f(i+1,j)+uv*f(i+1,j+1)
            color[0] = cbuf[j * srcW + i]; // f(i,j)颜色值
            color[1] = cbuf[j1 * srcW + i]; // f(i,j+1)颜色值
            color[2] = cbuf[j * srcW + i1]; // f(i+1,j)颜色值
            color[3] = cbuf[j1 * srcW + i1]; // f(i+1,j+1)颜色值

            // 给目标图像赋值为双线性内插值求得的混合色
            rbuf[dstY * dstW + dstX] = mixColor(color, i, j, u, v);
        }
    }

    jintArray result = env->NewIntArray(newSize); // 新建一个jintArray
    env->SetIntArrayRegion(result, 0, newSize, rbuf); // 将rbuf转存入result
    env->ReleaseIntArrayElements(buf, cbuf, 0); // 释放int数组元素
    return result;
}

JNIEXPORT jintArray JNICALL Java_com_eighteengray_imageprocesslibrary_ImageProcessJni_dilation
        (JNIEnv *env, jclass jclazz, jintArray buf, jint w, jint h){
    jint * cbuf; // 源图像
    cbuf = env->GetIntArrayElements(buf, 0); // 获取int数组元素

    int white = 0xFFFFFFFF; // 不透明白色
    int black = 0xFF000000; // 不透明黑色

    int size = w * h;
    jint rbuf[size]; // 目标图像
    memset(rbuf, black, size * sizeof(jint)); // 将目标图像置成全黑

    int i, j, m, n, gray;
    jint *p, *q;
    // 由于使用3×3的结构元素，为防止越界，所以不处理上下左右四边像素
    for (i = 1; i < h - 1; i++) {
        for (j = 1; j < w - 1; j++) {
            p = cbuf + w * i + j; // 指向源图像i行j列

            // 遍历源图像对应结构元素的各点
            for (m = -1; m <= 1; m++) {
                for (n = -1; n <= 1; n++) {
                    gray = (*(p + w * m + n)) & 0xFF; // 获取源图像对应结构元素点的灰度值
                    // 如果对应3x3范围内有白点（其他色都算为黑）
                    if (gray == 255) {
                        q = rbuf + w * i + j; // 指向目标图像i行j列
                        *q = white; // 将目标图像中的当前点赋成白色
                        break;
                    }
                }
            }
        }
    }

    jintArray result = env->NewIntArray(size); // 新建一个jintArray
    env->SetIntArrayRegion(result, 0, size, rbuf); // 将rbuf转存入result
    env->ReleaseIntArrayElements(buf, cbuf, 0); // 释放int数组元素
    return result;
}

JNIEXPORT jintArray JNICALL Java_com_eighteengray_imageprocesslibrary_ImageProcessJni_erosion
        (JNIEnv *env, jclass jclazz, jintArray buf, jint w, jint h){
    jint * cbuf; // 源图像
    cbuf = env->GetIntArrayElements(buf, 0); // 获取int数组元素

    int white = 0xFFFFFFFF; // 不透明白色
    int black = 0xFF000000; // 不透明黑色

    int size = w * h;
    jint rbuf[size]; // 目标图像
    memset(rbuf, black, size * sizeof(jint)); // 将目标图像置成全黑

    int i, j, m, n, gray;
    jint *p, *q;
    // 由于使用3×3的结构元素，为防止越界，所以不处理上下左右四边像素
    for (i = 1; i < h - 1; i++) {
        for (j = 1; j < w - 1; j++) {
            p = cbuf + w * i + j; // 指向源图像i行j列

            q = rbuf + w * i + j; // 指向目标图像i行j列
            *q = white; // 将目标图像中的当前点赋成白色

            // 遍历源图像对应结构元素的各点
            for (m = -1; m <= 1; m++) {
                for (n = -1; n <= 1; n++) {
                    gray = (*(p + w * m + n)) & 0xFF; // 获取源图像对应结构元素点的灰度值
                    // 如果对应3x3范围内有黑点（其他色都算为白）
                    if (gray == 0) {
                        *q = black; // 将目标图像中的当前点赋成黑色
                        break;
                    }
                }
            }
        }
    }

    jintArray result = env->NewIntArray(size); // 新建一个jintArray
    env->SetIntArrayRegion(result, 0, size, rbuf); // 将rbuf转存入result
    env->ReleaseIntArrayElements(buf, cbuf, 0); // 释放int数组元素
    return result;
}

JNIEXPORT jintArray JNICALL Java_com_eighteengray_imageprocesslibrary_ImageProcessJni_thinning
        (JNIEnv *env, jclass jclazz, jintArray buf, jint w, jint h){
    jint * cbuf;
    cbuf = env->GetIntArrayElements(buf, 0); // 获取int数组元素

    int black = 0xFF000000; // 不透明黑色

    unsigned char foreground = 0xFF; // 前景灰度值：255（白）
    unsigned char background = 0; // 背景灰度值：0（黑）

    jboolean modified = 1; // 设置脏标记：true
    unsigned char count; // 计数器
    unsigned char mark[w][h]; // 可删除标记

    int size = w * h; // 数据数目

    /*
     * 8-领域示意图
     *
     * P9   P2  P3
     * P8   P1  P4
     * P7   P6  P5
     */

    int i, j, m, n; // 循环标记
    unsigned char gray; // 灰度值
    unsigned char grays[3][3]; // 领域各点灰度值
    jint *p; // 指向源图像像素的指针

    // 一次迭代操作（直到没有点再满足标记条件）
    while (modified) {
        modified = 0; // 设置脏标记：false

        /*
         * 第一层子循环，删除条件：
         *
         * (1.1) 2<=N(p1)<=6
         * (1.2) S(p1)=1
         * (1.3) p2*p4*p6=0
         * (1.4) p4*p6*p8=0
         *
         * N(p1)：p1的非零邻点的个数
         * S(p1)：以p2 ，p3 ，…… ，p9为序时这些点的值从0到1变化的次数
         */

        memset(mark, 0, sizeof(mark)); // 重置删除标记为false

        // 防止越界，不处理上下左右四边像素
        for (i = 1; i < h - 1; i++) {
            for (j = 1; j < w - 1; j++) {

                p = cbuf + w * i + j; // 指向源图像i行j列
                gray = (*p) & 0xFF; // 获得灰度值

                if (gray == foreground) { // 判断是否为细化像素（前景像素）

                    // 计算N(p1)
                    count = 0; // 重置计数器
                    for (m = -1; m <= 1; m++) {
                        for (n = -1; n <= 1; n++) {
                            gray = (*(p + w * m + n)) & 0xFF; // 获取领域各点的灰度值
                            grays[m + 1][n + 1] = gray; // 同时存储领域各点的灰度值
                            if (gray == foreground) { // 如果为前景像素
                                count++;
                            }
                        }
                    }
                    count--; // 去除中心点

                    // 判断条件(1.1)
                    if (2 <= count && count <= 6) {
                    } else {
                        continue; // 条件(1.1)不成立，跳出循环
                    }

                    // 计算S(p1)：四周像素由0变255的次数
                    // 需先计算N(p1)，获取领域各点的灰度值
                    count = 0; // 重置计数器
                    if (grays[0][1] < grays[0][2])
                        count++; // p2->p3
                    if (grays[0][2] < grays[1][2])
                        count++; // p3->p4
                    if (grays[1][2] < grays[2][2])
                        count++; // p4->p5
                    if (grays[2][2] < grays[2][1])
                        count++; // p5->p6
                    if (grays[2][1] < grays[2][0])
                        count++; // p6->p7
                    if (grays[2][0] < grays[1][0])
                        count++; // p7->p8
                    if (grays[1][0] < grays[0][0])
                        count++; // p8->p9
                    if (grays[0][0] < grays[0][1])
                        count++; // p9->p2

                    // 判断条件(1.2)
                    if (1 == count) {
                    } else {
                        continue; // 条件(1.2)不成立，跳出循环
                    }

                    // 判断条件(1.3)
                    if (background == grays[0][1] || background == grays[1][2]
                        || background == grays[2][1]) {
                    } else {
                        continue; // 条件(1.3)不成立，跳出循环
                    }

                    // 判断条件(1.4)
                    if (background == grays[1][2] || background == grays[2][1]
                        || background == grays[1][0]) {
                    } else {
                        continue; // 条件(1.4)不成立，跳出循环
                    }

                    /*
                     * 四条件都成立时
                     */
                    mark[j][i] = 1; // 删除标记为true
                    modified = 1; // 脏标记为true
                }
            }
        }

        // 由删除标记去除
        if (modified) {
            for (i = 1; i < h - 1; i++) {
                for (j = 1; j < w - 1; j++) {
                    // 如果删除标记为true
                    if (1 == mark[j][i]) {
                        cbuf[w * i + j] = black; // 修改成背景色（黑）
                    }
                }
            }
        }

        /*
         * 第二层子循环，删除条件：
         *
         * (1.1) 2<=N(p1)<=6
         * (1.2) S(p1)=1
         * (2.3) p2*p4*p8=0
         * (2.4) p2*p6*p8=0
         */
        memset(mark, 0, sizeof(mark)); // 重置删除标记为false

        // 防止越界，不处理上下左右四边像素
        for (i = 1; i < h - 1; i++) {
            for (j = 1; j < w - 1; j++) {

                p = cbuf + w * i + j; // 指向源图像i行j列
                gray = (*p) & 0xFF; // 获得灰度值

                if (gray == foreground) { // 判断是否为细化像素（前景像素）

                    // 计算N(p1)
                    count = 0; // 重置计数器
                    for (m = -1; m <= 1; m++) {
                        for (n = -1; n <= 1; n++) {
                            gray = (*(p + w * m + n)) & 0xFF; // 获取领域各点的灰度值
                            grays[m + 1][n + 1] = gray; // 同时存储领域各点的灰度值
                            if (gray == foreground) { // 如果为前景像素
                                count++;
                            }
                        }
                    }
                    count--; // 去除中心点

                    // 判断条件(1.1)
                    if (2 <= count && count <= 6) {
                    } else {
                        continue; // 条件(1.1)不成立，跳出循环
                    }

                    // 计算S(p1)：四周像素由0变255的次数
                    // 需先计算N(p1)，获取领域各点的灰度值
                    count = 0; // 重置计数器
                    if (grays[0][1] < grays[0][2])
                        count++; // p2->p3
                    if (grays[0][2] < grays[1][2])
                        count++; // p3->p4
                    if (grays[1][2] < grays[2][2])
                        count++; // p4->p5
                    if (grays[2][2] < grays[2][1])
                        count++; // p5->p6
                    if (grays[2][1] < grays[2][0])
                        count++; // p6->p7
                    if (grays[2][0] < grays[1][0])
                        count++; // p7->p8
                    if (grays[1][0] < grays[0][0])
                        count++; // p8->p9
                    if (grays[0][0] < grays[0][1])
                        count++; // p9->p2

                    // 判断条件(1.2)
                    if (1 == count) {
                    } else {
                        continue; // 条件(1.2)不成立，跳出循环
                    }

                    // 判断条件(2.3)
                    if (background == grays[0][1] || background == grays[1][2]
                        || background == grays[1][0]) {
                    } else {
                        continue; // 条件(2.3)不成立，跳出循环
                    }

                    // 判断条件(2.4)
                    if (background == grays[0][1] || background == grays[2][1]
                        || background == grays[1][0]) {
                    } else {
                        continue; // 条件(2.4)不成立，跳出循环
                    }

                    /*
                     * 四条件都成立时
                     */
                    mark[j][i] = 1; // 删除标记为true
                    modified = 1; // 脏标记为true
                }
            }
        }

        // 由删除标记去除
        if (modified) {
            for (i = 1; i < h - 1; i++) {
                for (j = 1; j < w - 1; j++) {
                    // 如果删除标记为true
                    if (1 == mark[j][i]) {
                        cbuf[w * i + j] = black; // 修改成背景色（黑）
                    }
                }
            }
        }
    }

    jintArray result = env->NewIntArray(size); // 新建一个jintArray
    env->SetIntArrayRegion(result, 0, size, cbuf); // 将cbuf转存入result
    env->ReleaseIntArrayElements(buf, cbuf, 0); // 释放int数组元素
    return result;
}


int *blur_ARGB_8888(int *pix, int w, int h, int radius) {
    int wm = w - 1;
    int hm = h - 1;
    int wh = w * h;
    int div = radius + radius + 1;

    short *r = (short *) malloc(wh * sizeof(short));
    short *g = (short *) malloc(wh * sizeof(short));
    short *b = (short *) malloc(wh * sizeof(short));
    int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;

    int *vmin = (int *) malloc(MAX(w, h) * sizeof(int));

    int divsum = (div + 1) >> 1;
    divsum *= divsum;
    short *dv = (short *) malloc(256 * divsum * sizeof(short));
    for (i = 0; i < 256 * divsum; i++) {
        dv[i] = (short) (i / divsum);
    }

    yw = yi = 0;
    int(*stack)[3] = (int (*)[3]) malloc(div * 3 * sizeof(int));
    int stackpointer;
    int stackstart;
    int *sir;
    int rbs;
    int r1 = radius + 1;
    int routsum, goutsum, boutsum;
    int rinsum, ginsum, binsum;

    for (y = 0; y < h; y++) {
        rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
        for (i = -radius; i <= radius; i++) {
            p = pix[yi + (MIN(wm, MAX(i, 0)))];
            sir = stack[i + radius];
            sir[0] = (p & 0xff0000) >> 16;
            sir[1] = (p & 0x00ff00) >> 8;
            sir[2] = (p & 0x0000ff);

            rbs = r1 - ABS(i);
            rsum += sir[0] * rbs;
            gsum += sir[1] * rbs;
            bsum += sir[2] * rbs;
            if (i > 0) {
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
            }
            else {
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
            }
        }
        stackpointer = radius;

        for (x = 0; x < w; x++) {

            r[yi] = dv[rsum];
            g[yi] = dv[gsum];
            b[yi] = dv[bsum];

            rsum -= routsum;
            gsum -= goutsum;
            bsum -= boutsum;

            stackstart = stackpointer - radius + div;
            sir = stack[stackstart % div];

            routsum -= sir[0];
            goutsum -= sir[1];
            boutsum -= sir[2];

            if (y == 0) {
                vmin[x] = MIN(x + radius + 1, wm);
            }
            p = pix[yw + vmin[x]];

            sir[0] = (p & 0xff0000) >> 16;
            sir[1] = (p & 0x00ff00) >> 8;
            sir[2] = (p & 0x0000ff);

            rinsum += sir[0];
            ginsum += sir[1];
            binsum += sir[2];

            rsum += rinsum;
            gsum += ginsum;
            bsum += binsum;

            stackpointer = (stackpointer + 1) % div;
            sir = stack[(stackpointer) % div];

            routsum += sir[0];
            goutsum += sir[1];
            boutsum += sir[2];

            rinsum -= sir[0];
            ginsum -= sir[1];
            binsum -= sir[2];

            yi++;
        }
        yw += w;
    }
    for (x = 0; x < w; x++) {
        rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
        yp = -radius * w;
        for (i = -radius; i <= radius; i++) {
            yi = MAX(0, yp) + x;

            sir = stack[i + radius];

            sir[0] = r[yi];
            sir[1] = g[yi];
            sir[2] = b[yi];

            rbs = r1 - ABS(i);

            rsum += r[yi] * rbs;
            gsum += g[yi] * rbs;
            bsum += b[yi] * rbs;

            if (i > 0) {
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
            }
            else {
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
            }

            if (i < hm) {
                yp += w;
            }
        }
        yi = x;
        stackpointer = radius;
        for (y = 0; y < h; y++) {
            // Preserve alpha channel: ( 0xff000000 & pix[yi] )
            pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

            rsum -= routsum;
            gsum -= goutsum;
            bsum -= boutsum;

            stackstart = stackpointer - radius + div;
            sir = stack[stackstart % div];

            routsum -= sir[0];
            goutsum -= sir[1];
            boutsum -= sir[2];

            if (x == 0) {
                vmin[y] = MIN(y + r1, hm) * w;
            }
            p = x + vmin[y];

            sir[0] = r[p];
            sir[1] = g[p];
            sir[2] = b[p];

            rinsum += sir[0];
            ginsum += sir[1];
            binsum += sir[2];

            rsum += rinsum;
            gsum += ginsum;
            bsum += binsum;

            stackpointer = (stackpointer + 1) % div;
            sir = stack[stackpointer];

            routsum += sir[0];
            goutsum += sir[1];
            boutsum += sir[2];

            rinsum -= sir[0];
            ginsum -= sir[1];
            binsum -= sir[2];

            yi += w;
        }
    }

    free(r);
    free(g);
    free(b);
    free(vmin);
    free(dv);
    free(stack);
    return (pix);
}


short *blur_RGB_565(short *pix, int w, int h, int radius) {
    int wm = w - 1;
    int hm = h - 1;
    int wh = w * h;
    int div = radius + radius + 1;

    short *r = (short *) malloc(wh * sizeof(short));
    short *g = (short *) malloc(wh * sizeof(short));
    short *b = (short *) malloc(wh * sizeof(short));

    int rsum, gsum, bsum, x, y, p, i, yp, yi, yw;

    int *vmin = (int *) malloc(MAX(w, h) * sizeof(int));

    int divsum = (div + 1) >> 1;
    divsum *= divsum;

    short *dv = (short *) malloc(256 * divsum * sizeof(short));

    for (i = 0; i < 256 * divsum; i++) {
        dv[i] = (short) (i / divsum);
    }

    yw = yi = 0;

    int(*stack)[3] = (int (*)[3]) malloc(div * 3 * sizeof(int));
    int stackpointer;
    int stackstart;
    int *sir;
    int rbs;
    int r1 = radius + 1;
    int routsum, goutsum, boutsum;
    int rinsum, ginsum, binsum;

    for (y = 0; y < h; y++) {
        rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
        for (i = -radius; i <= radius; i++) {
            p = pix[yi + (MIN(wm, MAX(i, 0)))];
            sir = stack[i + radius];
            sir[0] = (((p) & 0xF800) >> 11) << 3;
            sir[1] = (((p) & 0x7E0) >> 5) << 2;
            sir[2] = ((p) & 0x1F) << 3;

            rbs = r1 - ABS(i);
            rsum += sir[0] * rbs;
            gsum += sir[1] * rbs;
            bsum += sir[2] * rbs;
            if (i > 0) {
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
            }
            else {
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
            }
        }
        stackpointer = radius;

        for (x = 0; x < w; x++) {

            r[yi] = dv[rsum];
            g[yi] = dv[gsum];
            b[yi] = dv[bsum];

            rsum -= routsum;
            gsum -= goutsum;
            bsum -= boutsum;

            stackstart = stackpointer - radius + div;
            sir = stack[stackstart % div];

            routsum -= sir[0];
            goutsum -= sir[1];
            boutsum -= sir[2];

            if (y == 0) {
                vmin[x] = MIN(x + radius + 1, wm);
            }
            p = pix[yw + vmin[x]];

            sir[0] = (((p) & 0xF800) >> 11) << 3;
            sir[1] = (((p) & 0x7E0) >> 5) << 2;
            sir[2] = ((p) & 0x1F) << 3;

            rinsum += sir[0];
            ginsum += sir[1];
            binsum += sir[2];

            rsum += rinsum;
            gsum += ginsum;
            bsum += binsum;

            stackpointer = (stackpointer + 1) % div;
            sir = stack[(stackpointer) % div];

            routsum += sir[0];
            goutsum += sir[1];
            boutsum += sir[2];

            rinsum -= sir[0];
            ginsum -= sir[1];
            binsum -= sir[2];

            yi++;
        }
        yw += w;
    }
    for (x = 0; x < w; x++) {
        rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
        yp = -radius * w;
        for (i = -radius; i <= radius; i++) {
            yi = MAX(0, yp) + x;

            sir = stack[i + radius];

            sir[0] = r[yi];
            sir[1] = g[yi];
            sir[2] = b[yi];

            rbs = r1 - ABS(i);

            rsum += r[yi] * rbs;
            gsum += g[yi] * rbs;
            bsum += b[yi] * rbs;

            if (i > 0) {
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
            }
            else {
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
            }

            if (i < hm) {
                yp += w;
            }
        }
        yi = x;
        stackpointer = radius;
        for (y = 0; y < h; y++) {
            // Not have alpha channel
            pix[yi] = ((((dv[rsum]) >> 3) << 11) | (((dv[gsum]) >> 2) << 5) | ((dv[bsum]) >> 3));

            rsum -= routsum;
            gsum -= goutsum;
            bsum -= boutsum;

            stackstart = stackpointer - radius + div;
            sir = stack[stackstart % div];

            routsum -= sir[0];
            goutsum -= sir[1];
            boutsum -= sir[2];

            if (x == 0) {
                vmin[y] = MIN(y + r1, hm) * w;
            }
            p = x + vmin[y];

            sir[0] = r[p];
            sir[1] = g[p];
            sir[2] = b[p];

            rinsum += sir[0];
            ginsum += sir[1];
            binsum += sir[2];

            rsum += rinsum;
            gsum += ginsum;
            bsum += binsum;

            stackpointer = (stackpointer + 1) % div;
            sir = stack[stackpointer];

            routsum += sir[0];
            goutsum += sir[1];
            boutsum += sir[2];

            rinsum -= sir[0];
            ginsum -= sir[1];
            binsum -= sir[2];

            yi += w;
        }
    }

    free(r);
    free(g);
    free(b);
    free(vmin);
    free(dv);
    free(stack);
    return (pix);
}

JNIEXPORT jintArray JNICALL Java_com_eighteengray_imageprocesslibrary_ImageProcessJni_blurPixels
        (JNIEnv *env, jclass jclazz, jintArray arrIn, jint w, jint h, jint r){
    jint *pixels;
    pixels = (env)->GetIntArrayElements(arrIn, 0);
    pixels = blur_ARGB_8888(pixels, w, h, r);
    int size = w * h;
     jintArray result = env->NewIntArray(size);
     env->SetIntArrayRegion(result, 0, size, pixels);
    (env)->ReleaseIntArrayElements(arrIn, pixels, 0);
    return result;
}










