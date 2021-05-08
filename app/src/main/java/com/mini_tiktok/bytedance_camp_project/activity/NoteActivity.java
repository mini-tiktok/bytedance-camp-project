package com.mini_tiktok.bytedance_camp_project.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.snackbar.Snackbar;
import com.mini_tiktok.bytedance_camp_project.R;


public class NoteActivity extends AppCompatActivity {

    private EditText editText;
    private Button addBtn;
    private String mp4Path = "";
    private MediaMetadataRetriever mMetadataRetriever;
    private VideoView mVideoView;
    private ImageView imageView;
    private int totalTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setTitle(R.string.upload);

        Intent intent = getIntent();
        mp4Path = intent.getStringExtra("path");

        mVideoView = findViewById(R.id.note_video);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                totalTime = mVideoView.getDuration();
            }
        });
        mVideoView.setVideoPath(mp4Path);
        mVideoView.start();

        mMetadataRetriever = new MediaMetadataRetriever();
        mMetadataRetriever.setDataSource(mp4Path);
        String duration = mMetadataRetriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);//时长(毫秒)
        String width = mMetadataRetriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);//宽
        String height = mMetadataRetriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);//高
        Bitmap bitmap = mMetadataRetriever.getFrameAtTime(0);

        imageView = findViewById(R.id.note_image);
        imageView.setImageBitmap(bitmap);
        
        editText = findViewById(R.id.edit_text);
        editText.setFocusable(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.showSoftInput(editText, 0);
        }

        addBtn = findViewById(R.id.btn_add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence content = editText.getText();
                Snackbar.make(view, "Replace with your own action" + mp4Path, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}

