package com.liu.aviplayer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.atomic.AtomicBoolean;

public class BitmapPlayerActivity extends AbstractPlayerActivity {
    /**
     * 正在播放
     */
    private final AtomicBoolean isPlaying = new AtomicBoolean();

    /**
     * SurfaceHolder
     */
    private SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_player);

        SurfaceView surfaceView = findViewById(R.id.surface_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(surfaceHolderCallback);
    }

    /**
     * Surface holder 监听 surface 事件回调
     */
    private final SurfaceHolder.Callback surfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // surface 准备好后开始播放
            isPlaying.set(true);

            // 在一个单独的线程中进行渲染
            new Thread(renderer).start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // surface 销毁后停止播放
            isPlaying.set(false);
        }
    };

    private final Runnable renderer = new Runnable() {
        @Override
        public void run() {
            // 创建一个新的 bitmap 来保存所有的帧
            Bitmap bitmap = Bitmap.createBitmap(
                    getWidth(avi),
                    getHeight(avi),
                    Bitmap.Config.RGB_565);

            // 使用帧速来计算延时
            long frameDelay = (long) (1000 / getFrameRate(avi));

            // 播放时开始渲染
            while (isPlaying.get()) {
                // 将帧渲染至bitmap
                render(avi, bitmap);

                // 锁定 canvas
                Canvas canvas = surfaceHolder.lockCanvas();

                // 将 bitmap 绘制至 canvas
                canvas.drawBitmap(bitmap, 0, 0, null);

                // canvas 准备显示
                surfaceHolder.unlockCanvasAndPost(canvas);

                // 等待下一帧
                try {
                    Thread.sleep(frameDelay);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    };

    /**
     * 从 AVI 文件描述符输出到指定 Bitmap 来渲染帧
     * @param avi
     * @param bitmap
     * @return
     */
    private native static boolean render(long avi, Bitmap bitmap);

}
