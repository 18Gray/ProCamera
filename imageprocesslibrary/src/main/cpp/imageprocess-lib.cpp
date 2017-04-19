#include <jni.h>
#include <string>

extern "C"
jstring
Java_com_eighteengray_imageprocesslibrary_imageprocess_JniExample_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Jni Test";
    return env->NewStringUTF(hello.c_str());
}




