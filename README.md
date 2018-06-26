# AVIPlayer

https://blog.csdn.net/u012528526/article/details/80647537

在`{ndk-bundle}/sources/transcode-1.1.5/avilib`目录创建`Android.md`文件添加下列代码。

```
LOCAL_PATH:= $(call my-dir)

#
# 转码 AVILib
#

# 源文件
MY_AVILIB_SRC_FILES:= avilib.c platform_posix.c

# 包含导出路径
MY_AVILIB_C_INCLUDES:= $(LOCAL_PATH)

#
# AVILib 静态
#
include $(CLEAR_VARS)

# 模块名称
LOCAL_MODULE:=avilib_static

# 源文件
LOCAL_SRC_FILES:= $(MY_AVILIB_SRC_FILES)

# 包含导出路径
LOCAL_EXPORT_C_INCLUDES:= $(MY_AVILIB_C_INCLUDES)

# 构件静态库
include $(BUILD_STATIC_LIBRARY)

#
# AVILib 共享
#
include $(CLEAR_VARS)

# 模块名称
LOCAL_MODULE:= avilib_share

# 源文件
LOCAL_SRC_FILES:= $(MY_AVILIB_SRC_FILES)

# 包含导出路径
LOCAL_EXPORT_C_INCLUDES:= $(MY_AVILIB_C_INCLUDES)

# 构件共享库
include $(BUILD_SHARED_LIBRARY)
```

