package com.mini_tiktok.bytedance_camp_project.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import com.mini_tiktok.bytedance_camp_project.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.mini_tiktok.bytedance_camp_project.activity.model.UploadResponse;
import com.google.android.material.button.MaterialButton;
import com.mini_tiktok.bytedance_camp_project.service.VideoService;
import com.mini_tiktok.bytedance_camp_project.util.Constant;

import java.io.ByteArrayOutputStream;
import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoverChooseActivity extends AppCompatActivity {

    private ImageView mImageView;
    private final static String TAG = "CoverChooseActivity";
    private Uri mVideoUri;
    Bitmap mCoverBmp;
    private VideoService videoserv;
    private final static int PICK_IMAGE_REQUEST_CODE = 1001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover_choose);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mImageView = findViewById(R.id.image_view);

        MaterialButton autoButton = findViewById(R.id.btn_auto);
        autoButton.setOnClickListener((view) -> autoSelect());

        MaterialButton manualButton = findViewById(R.id.btn_manual);
        manualButton.setOnClickListener((view) -> manualSelect());


        MaterialButton uploadButton = findViewById(R.id.btn_upload);
        uploadButton.setOnClickListener((view) -> upload());

        ImageButton prevButton = findViewById(R.id.btn_prev);
        prevButton.setOnClickListener((view) -> finish());

        initNetwork();

        mVideoUri = Uri.parse(getIntent().getStringExtra("videoUri"));
        autoSelect();
        setLoadingScreen(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }


    private void initNetwork() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BYTEDANCE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        videoserv = retrofit.create(VideoService.class);
    }

    private void autoSelect() {
        mCoverBmp = getVideoThumb();
        mImageView.setImageBitmap(mCoverBmp);
    }

    private Bitmap getVideoThumb() {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(this, mVideoUri);
        Bitmap bmp = mmr.getFrameAtTime();
        return bmp;
    }


    private void manualSelect() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST_CODE) {
                Uri uri = data.getData();
                mImageView.setImageURI(uri);
                File file = new File(uri.getPath());
                mCoverBmp = BitmapFactory.decodeFile(file.getAbsolutePath());
            }
        }
    }

    private void upload() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mCoverBmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] coverImageData = stream.toByteArray();
        RequestBody requestImage = RequestBody.create(MediaType.parse("multipart/form-data"), coverImageData);
        MultipartBody.Part coverImage = MultipartBody.Part.createFormData("cover_image", "cover.png", requestImage);

        RequestBody requestVideo = RequestBody.create(MediaType.parse("multipart/form-data"), mVideoUri.toString());
        MultipartBody.Part video = MultipartBody.Part.createFormData("video", "video.mp4", requestVideo);

        Call<UploadResponse> resp = videoserv.submitVideo(Constant.STUDENT_ID, Constant.USER_NAME, "", coverImage, video);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        setLoadingScreen(true);

        resp.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(final Call<UploadResponse> call, final Response<UploadResponse> response) {
                if (!response.isSuccessful()) return;
                setResult(Activity.RESULT_OK);
                Toast.makeText(com.mini_tiktok.bytedance_camp_project.activity.CoverChooseActivity.this, R.string.uplaod_success, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(final Call<UploadResponse> call, final Throwable t) {
                t.printStackTrace();
                Toast.makeText(com.mini_tiktok.bytedance_camp_project.activity.CoverChooseActivity.this, getString(R.string.upload_fail), Toast.LENGTH_SHORT).show();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                setLoadingScreen(false);
            }
        });
    }

    private void setLoadingScreen(boolean status) {
        int visibility = status ? View.VISIBLE : View.GONE;
        findViewById(R.id.bg_loading).setVisibility(visibility);
        findViewById(R.id.text_loading).setVisibility(visibility);
        findViewById(R.id.progress_upload).setVisibility(visibility);
    }

}