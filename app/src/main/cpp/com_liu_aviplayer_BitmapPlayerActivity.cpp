//
// Created by 刘璟博 on 2018/6/27.
//

extern "C" {
#include <avilib.h>
}

#include <android/bitmap.h>
#include "Common.h"
#include "com_liu_aviplayer_BitmapPlayerActivity.h"

jboolean Java_com_liu_aviplayer_BitmapPlayerActivity_render
        (JNIEnv *env, jclass clazz, jlong avi, jobject bitmap) {
    jboolean isFrameRead = JNI_FALSE;

    char *frameBuffer = 0;
    long frameSize = 0;
    int keyFrame = 0;

    // 锁定 bitmap 并得到 raw byte
    if (0 > AndroidBitmap_lockPixels(env, bitmap, (void **) &frameBuffer)) {
        ThrowException(env, "java/io/IOException", "Unable to lock pixels.");
        goto exit;
    }

    // 将 AVI 帧 byte 读到 bitmap 中
    frameSize = AVI_read_frame((avi_t *) avi, frameBuffer, &keyFrame);

    // 解锁 bitmap
    if (0 > AndroidBitmap_unlockPixels(env, bitmap)) {
        ThrowException(env, "java/io/IOException", "Unable to unlock pixels.");
        goto exit;
    }

    // 检查帧是否成功读取
    if (0 < frameSize) {
        isFrameRead = JNI_TRUE;
    }

    exit:
    return isFrameRead;

}