//
// Created by 刘璟博 on 2018/6/27.
//

#include "Common.h"

void ThrowException(JNIEnv *env, const char *className, const char *message) {
    // 获取异常类
    jclass clazz = env->FindClass(className);

    // 如果找到异常类
    if (0 != clazz) {
        // 抛出异常
        env->ThrowNew(clazz, message);

        // 释放本地引用
        env->DeleteLocalRef(clazz);
    }
}