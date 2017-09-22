#include <jni.h>
#include <string>


#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <android/bitmap.h>
#include <android/log.h>

#ifndef eprintf
#define eprintf(...) __android_log_print(ANDROID_LOG_ERROR,"@",__VA_ARGS__)
#endif

#define RGB565_R(p) ((((p) & 0xF800) >> 11) << 3)
#define RGB565_G(p) ((((p) & 0x7E0 ) >> 5)  << 2)
#define RGB565_B(p) ( ((p) & 0x1F  )        << 3)
#define MAKE_RGB565(r,g,b) ((((r) >> 3) << 11) | (((g) >> 2) << 5) | ((b) >> 3))

#define RGBA_A(p) (((p) & 0xFF000000) >> 24)
#define RGBA_R(p) (((p) & 0x00FF0000) >> 16)
#define RGBA_G(p) (((p) & 0x0000FF00) >>  8)
#define RGBA_B(p)  ((p) & 0x000000FF)
#define MAKE_RGBA(r,g,b,a) (((a) << 24) | ((r) << 16) | ((g) << 8) | (b))

extern "C"
jstring
Java_com_eighteengray_imageprocesslibrary_imageprocess_JniExample_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Jni Test";
    return env->NewStringUTF(hello.c_str());
}


/*
JNIEXPORT void JNICALL Java_com_yxcorp_hello_Effect_update(JNIEnv *env, jclass clazz, jobject zBitmap) {
    JNIEnv J = *env;

    if (zBitmap == NULL) {
        eprintf("bitmap is null\n");
        return;
    }

    // Get bitmap info
    AndroidBitmapInfo info;
    memset(&info, 0, sizeof(info));
    AndroidBitmap_getInfo(env, zBitmap, &info);
    // Check format, only RGB565 & RGBA are supported
    if (info.width <= 0 || info.height <= 0 ||
        (info.format != ANDROID_BITMAP_FORMAT_RGB_565 &&
         info.format != ANDROID_BITMAP_FORMAT_RGBA_8888)) {
        eprintf("invalid bitmap\n");
        J->ThrowNew(env, J->FindClass(env, "java/io/IOException"), "invalid bitmap");
        return;
    }

    // Lock the bitmap to get the buffer
    void *pixels = NULL;
    int res = AndroidBitmap_lockPixels(env, zBitmap, &pixels);
    if (pixels == NULL) {
        eprintf("fail to lock bitmap: %d\n", res);
        J->ThrowNew(env, J->FindClass(env, "java/io/IOException"), "fail to open bitmap");
        return;
    }

    eprintf("Effect: %dx%d, %d\n", info.width, info.height, info.format);

    int x = 0, y = 0;
    // From top to bottom
    for (y = 0; y < info.height; ++y) {
        // From left to right
        for (x = 0; x < info.width; ++x) {
            int a = 0, r = 0, g = 0, b = 0;
            void *pixel = NULL;
            // Get each pixel by format
            if (info.format == ANDROID_BITMAP_FORMAT_RGB_565) {
                pixel = ((uint16_t *) pixels) + y * info.width + x;
                uint16_t v = *(uint16_t *) pixel;
                r = RGB565_R(v);
                g = RGB565_G(v);
                b = RGB565_B(v);
            } else {// RGBA
                pixel = ((uint32_t *) pixels) + y * info.width + x;
                uint32_t v = *(uint32_t *) pixel;
                a = RGBA_A(v);
                r = RGBA_R(v);
                g = RGBA_G(v);
                b = RGBA_B(v);
            }

            // Grayscale
            int gray = (r * 38 + g * 75 + b * 15) >> 7;

            // Write the pixel back
            if (info.format == ANDROID_BITMAP_FORMAT_RGB_565) {
                *((uint16_t *) pixel) = MAKE_RGB565(gray, gray, gray);
            } else {// RGBA
                *((uint32_t *) pixel) = MAKE_RGBA(gray, gray, gray, a);
            }
        }
    }
    AndroidBitmap_unlockPixels(env, zBitmap);
}
*/


/*jintArray Java_com_spore_meitu_jni_ImageUtilEngine_toGray(JNIEnv* env, jobject thiz, jintArray buf, jint width, jint height)
{
    // buf中是原图片的颜色数组，函数返回结果也是颜色数组，
    // 需要把颜色数组转换成Bitmap
    jint * cbuf;
    cbuf = (*env)->GetIntArrayElements(env, buf, 0);

    int newSize = width * height;
    jint rbuf[newSize]; // 新图像像素值

    int count = 0;
    int preColor = 0;
    int prepreColor = 0;
    int color = 0;
    preColor = cbuf[0];

    int i = 0;
    int j = 0;
    for (i = 0; i < width; i++)
    {
        for (j = 0; j < height; j++)
        {
            int curr_color = cbuf[j * width + i];
            int r = red(curr_color);
            int g = green(curr_color);
            int b = blue(curr_color);
            int modif_color = (int)(r * 0.3 + g * 0.59 + b * 0.11);
            rbuf[j * width + i] = ARGB(alpha(curr_color),modif_color,modif_color,modif_color);
        }
    }
    jintArray result = (*env)->NewIntArray(env, newSize); // 新建一个jintArray
    (*env)->SetIntArrayRegion(env, result, 0, newSize, rbuf); // 将rbuf转存入result
    (*env)->ReleaseIntArrayElements(env, buf, cbuf, 0); // 释放int数组元素
    return result;
}*/









