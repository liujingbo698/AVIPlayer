package com.liu.aviplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.io.File;

public class MainActivity extends Activity implements View.OnClickListener {

    /**
     * AVI 文件名字编辑
     */
    private EditText fileNameEdit;

    /**
     * Player 类型的单选组
     */
    private RadioGroup playerRadioGroup;

    /**
     * Player 按钮
     */
    private Button playButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileNameEdit = findViewById(R.id.file_name_edit);
        playerRadioGroup = findViewById(R.id.player_radio_group);
        playButton = findViewById(R.id.play_button);
        playButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_button:
                onPlayButtonClick();
                break;
        }
    }

    private void onPlayButtonClick() {
        Intent intent = null;

        // 获得选择的单选按钮ID
        int radioId = playerRadioGroup.getCheckedRadioButtonId();

        // 基于ID选择 activity
        switch (radioId) {
            case R.id.bitmap_player_radio:
                intent = new Intent(this, BitmapPlayerActivity.class);
                break;
            default:
                // throw new UnsupportedOperationException("radioId=" + radioId);
        }

        // 基于外部存储器
        File file = new File(Environment.getExternalStorageDirectory(), fileNameEdit.getText().toString());

        // 将 AVI 文件的名字作为 extra 内容
        intent.putExtra(AbstractPlayerActivity.EXTRA_FILE_NAME, file.getAbsolutePath());

        // 启动 player activity
        startActivity(intent);
    }
}
