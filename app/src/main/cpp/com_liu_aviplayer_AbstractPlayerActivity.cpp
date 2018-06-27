//
// Created by 刘璟博 on 2018/6/25.
//
extern "C" {
#include <avilib.h>
}

#include "Common.h"
#include "com_liu_aviplayer_AbstractPlayerActivity.h"

jlong Java_com_liu_aviplayer_AbstractPlayerActivity_open(
        JNIEnv *env, jclass clazz, jstring fileName) {
    avi_t *avi = 0;

    //  获取文件名字赋值给C的一个字符串变量
    const char *cFileName = env->GetStringUTFChars(fileName, NULL);
    if (0 == cFileName) {
        goto exit;
    }

    // 打开 AVI 文件
    avi = AVI_open_input_file(cFileName, 1);

    // 释放文件名字
    env->ReleaseStringUTFChars(fileName, cFileName);

    // 如果 AVI 文件不能打开则抛出一个异常
    if (0 == avi) {
        ThrowException(env, "java/io/IOException", AVI_strerror());
    }

    exit:
    return (jlong) avi;
}

jint Java_com_liu_aviplayer_AbstractPlayerActivity_getWidth
        (JNIEnv *env, jclass clazz, jlong avi) {
    return AVI_video_width((avi_t *) avi);
}

jint Java_com_liu_aviplayer_AbstractPlayerActivity_getHeight
        (JNIEnv *env, jclass clazz, jlong avi) {
    return AVI_video_height((avi_t *) avi);
}

jdouble Java_com_liu_aviplayer_AbstractPlayerActivity_getFrameRate
        (JNIEnv *env, jclass clazz, jlong avi) {
    return AVI_frame_rate((avi_t *) avi);
}

void Java_com_liu_aviplayer_AbstractPlayerActivity_close
        (JNIEnv *env, jclass clazz, jlong avi) {
    AVI_close((avi_t *) avi);
}
