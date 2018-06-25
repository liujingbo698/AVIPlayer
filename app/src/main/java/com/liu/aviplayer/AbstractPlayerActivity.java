package com.liu.aviplayer;

import android.app.Activity;
import android.app.AlertDialog;

import java.io.IOException;

public abstract class AbstractPlayerActivity extends Activity {
    /**
     * AVI 文件名字的 extra
     */
    public static final String EXTRA_FILE_NAME = "com.liu.aviplayer.EXTRA_FILE_NAME";

    /**
     * AVI 文件描述符
     */
    protected long avi = 0;

    @Override
    protected void onStart() {
        super.onStart();

        try {
            avi = open(getFileName());
        } catch (IOException e) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.error_alert_title)
                    .setMessage(e.getMessage())
                    .show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // 如果 AVI 文件是打开的
        if (0 != avi) {
            // 关闭文件描述符
            close(avi);
            avi = 0;
        }
    }

    /**
     * 获取 AVI 视频文件的名字
     *
     * @return 文件的名字
     */
    protected String getFileName() {
        return getIntent().getExtras().getString(EXTRA_FILE_NAME);
    }

    protected native static long open(String fileName) throws IOException;

    /**
     * 获得视频的宽度
     * @param avi 文件描述符
     * @return 宽度
     */
    protected native static int getWidth(long avi);

    /**
     * 获得视频的高度
     * @param avi 文件描述符
     * @return 高度
     */
    protected native static int getHeight(long avi);

    /**
     * 获得真帧速
     * @param avi 文件描述符
     * @return 帧速
     */
    protected native static double getFrameRate(long avi);

    /**
     * 基于给定文件描述符关闭指定 AVI 文件
     * @param avi
     */
    protected native static void close(long avi);

    static{
        System.loadLibrary("AVIPl");
    }

}
