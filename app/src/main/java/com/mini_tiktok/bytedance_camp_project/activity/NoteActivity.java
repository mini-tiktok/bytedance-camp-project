package com.mini_tiktok.bytedance_camp_project.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mini_tiktok.bytedance_camp_project.R;
import com.mini_tiktok.bytedance_camp_project.data.UploadResponse;
import com.mini_tiktok.bytedance_camp_project.data.VideoInfo;
import com.mini_tiktok.bytedance_camp_project.service.VideoService;
import com.mini_tiktok.bytedance_camp_project.util.Constant;

import java.io.ByteArrayOutputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NoteActivity extends AppCompatActivity {

    private EditText editText;
    private Button addBtn;
    private String mp4Path = "";
    private MediaMetadataRetriever mMetadataRetriever;
    private VideoView mVideoView;
    private ImageView imageView;
    private int totalTime = 0;
    private Bitmap bitmap;
    private static final String TAG = "NoteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setTitle(R.string.upload);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BYTEDANCE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

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
        bitmap = mMetadataRetriever.getFrameAtTime(0);

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
                Uri mVideoUri=Uri.parse(mp4Path);
                System.out.println(mVideoUri.toString());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] coverImageData = stream.toByteArray();
                RequestBody requestImage = RequestBody.create(MediaType.parse("multipart/form-data"), coverImageData);
                MultipartBody.Part coverImage = MultipartBody.Part.createFormData("cover_image", "cover.png", requestImage);

                RequestBody requestVideo = RequestBody.create(MediaType.parse("multipart/form-data"), mVideoUri.toString());
                MultipartBody.Part video = MultipartBody.Part.createFormData("video", "video.mp4", requestVideo);
                Call<UploadResponse> call = retrofit.create(VideoService.class).uploadVideo(Constant.STUDENT_ID, Constant.USER_NAME,
                        content.toString(), coverImage, video);
                call.enqueue(new Callback<UploadResponse>() {
                    @Override
                    public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG,"fetch video success");
                            Toast.makeText(getApplicationContext(),"上传成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            getApplicationContext().startActivity(intent);
                        } else {
                            Snackbar.make(imageView, "网络错误，无法接收", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            Log.e(TAG,"fetch video failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<UploadResponse> call, Throwable t) {
                        Snackbar.make(imageView, "网络错误，无法接收", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        Log.e(TAG,"fetch video failed");
                    }
                });
            }
        });
    }

}

