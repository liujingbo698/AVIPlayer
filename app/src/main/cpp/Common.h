//
// Created by 刘璟博 on 2018/6/27.
//

#pragma once

#include <jni.h>

void ThrowException(JNIEnv *env, const char *className, const char *message);
