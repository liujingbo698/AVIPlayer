# AVIPlayer

## 将 AVILib 作为 NDK 的一个导入模块

### 下载安装 Transcode 并对其进行修改

1. 下载 [transcode-1.1.5.tar.bz2](https://pan.baidu.com/s/13euPyyWYkO6gHgIFF1Y4kA) 源压缩包 *密码: ybmt*

2. 在 MacOS 或 Linux 系统下，打开终端；在 Windows 系统下，打开 Cygwin

3. 命令进入 Android NDK 下的 sources 目录，使用命令需先配置NDK环境变量

    `cd $ANDROID_NDK_HOME/sources`
    
4. 提取压缩文件，输入下面的命令，用 transcode-1.1.5.tar.bz2 文件存放目录替换{Download        Location}
    
    `tar jxvf {Download Location}/transcode-1.1.5.tar.bz2`
    
5. 进入进入目录 `/transcode-1.1.5` 下的字目录 `/avilib`
    `cd transcode-1.1.5/avilib`
    
6. 当前目录打开 `platform.h` 头文件。在 `config.h` 头文件包含上下各添加一行的语句
    `#ifdef HAVE_CONFIG_H` 和 `#endif` 
    
    ```
    #ifdef HAVE_CONFIG_H
    #include "config.h"
    #endif
    
    // 这样就将通过 Android NDK 构建立系统而不是 Transcode 项目的 Makefile 来完成 AVILIB 的编译
    ```
    
7. Android NDK 构建系统需要它自己的 Android.mk 文件中的导入模块，在当前目录创建`Android.md`文件并添加下列代码。

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
    
### 修改 `CMakeLists.txt` 来使项目包含新的源文件，同时静态链接到 AVILib 第三方模块上。

```
# 将包含源文件库目录添加到构建
include_directories( ${ANDROID_NDK}/sources/transcode-1.1.5/avilib )
# 将给定的目录添加到编译器用来搜索包含文件的目录。相对路径被解释为相对于当前源目录。
    
# 导入第三方库
add_library( # 自定义库名
             avi-lib 
    
             # 以静态方式添加，语法规定，使用 IMPORTED
             STATIC IMPORTED )
    
# 第三方库属性设置
set_target_properties( # 指定要设定的库
                       avi-lib
                           
                       # 指定要设定的库的属性，即库所在位置
                       PROPERTIES IMPORTED_LOCATION
                           
                       # 提供要导入的库的路径
                       # avilib_static: 上一步 Android.mk 中定义的模块名称
                       ${ANDROID_NDK}/sources/transcode-1.1.5/avilib/avilib_static )

# 将第三方库进行链接                           
target_link_libraries( AVIPlayer
                       ${avi-lib} )
```
    
最后在 **Android Studio** 中 **Build** -> **Refresh Linked C++ Projects**
在C++代码中使用 `#include <avilib.h>` 即可使用该库


## 使用 JNI 图形 API 进行渲染

### 修改 `CMakeLists.txt` 文件以动态的与 `jnigraphics` 库进行连接

添加、修改以下代码

```
find_library( jnigraphics-lib jnigraphics )

target_link_libraries( # 指定的目标库
                       AVIPlayer

                       # 把目标库与以下库进行连接
                       # 导入NDK中
                       ${avi-lib}
                       ${jnigraphics-lib} )
```

在 **Android Studio** 中 **Build** -> **Refresh Linked C++ Projects** 只后，就能导入相关头文件了
`#include <android/bitmap.h>`

