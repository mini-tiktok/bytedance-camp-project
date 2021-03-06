package com.mini_tiktok.bytedance_camp_project.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
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

import static android.media.MediaMetadataRetriever.OPTION_CLOSEST_SYNC;
import static android.media.MediaMetadataRetriever.OPTION_PREVIOUS_SYNC;


public class NoteActivity extends AppCompatActivity {

    private EditText editText;
    private Button addBtn;
    private Button chooseBtn;
    private String mp4Path = "";
    private MediaMetadataRetriever mMetadataRetriever;
    private VideoView mVideoView;
    private ImageView imageView;
    private SeekBar seekBar;
    private boolean isTouch = false;
    private int totalTime = 0;
    private Bitmap bitmap;
    private static final String TAG = "NoteActivity";
    private int currentTime = 0;


    public static final int PICK_PHOTO = 102;

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
                seekBar.setMax(totalTime);
                Log.i("TAG", "getDuration  " + totalTime);
                mVideoView.start();
                mVideoView.pause();
                mVideoView.seekTo(currentTime);
            }
        });
        mVideoView.setVideoPath(mp4Path);
        mVideoView.start();
        System.out.println(mp4Path);
        mMetadataRetriever = new MediaMetadataRetriever();
        mMetadataRetriever.setDataSource(this, Uri.parse(mp4Path));
        String duration = mMetadataRetriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);//??????(??????)
        String width = mMetadataRetriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);//???
        String height = mMetadataRetriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);//???
        bitmap = mMetadataRetriever.getFrameAtTime(0);

        imageView = findViewById(R.id.note_image);
        imageView.setImageBitmap(bitmap);

        seekBar = findViewById(R.id.note_seekbar);
        Log.i("TAG", "J  " + totalTime);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isTouch) {
                    currentTime = progress;
                    mVideoView.seekTo(currentTime);
                    Log.i("TAG", "currenttime:  " + currentTime);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTouch = false;
                //????????????????????????bitmap?????? ???????????????
                bitmap = mMetadataRetriever.getFrameAtTime(currentTime * 1000 ,OPTION_CLOSEST_SYNC);
                Log.i("TAG", "K  " + totalTime);

                imageView.setImageBitmap(bitmap);
           }
        });
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
                Call<UploadResponse> call = retrofit.create(VideoService.class).submitVideo(Constant.STUDENT_ID, Constant.USER_NAME,
                        content.toString(), coverImage, video);
                call.enqueue(new Callback<UploadResponse>() {
                    @Override
                    public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG,"fetch video success");
                            Toast.makeText(getApplicationContext(),"????????????",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(intent);
                        } else {
                            Snackbar.make(imageView, "???????????????????????????", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            Log.e(TAG,"fetch video failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<UploadResponse> call, Throwable t) {
                        Snackbar.make(imageView, "???????????????????????????", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        Log.e(TAG,"fetch video failed");
                    }
                });
            }
        });

        chooseBtn = findViewById(R.id.note_choose);
        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //???????????????????????? ?????????????????????
                if (ContextCompat.checkSelfPermission(NoteActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NoteActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                } else {
                    //????????????
                    mVideoView.pause();
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    //Intent.ACTION_GET_CONTENT = "android.intent.action.GET_CONTENT"
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_PHOTO); // ????????????
                    mVideoView.resume();
                    mVideoView.seekTo(currentTime);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_PHOTO:
                if (resultCode == RESULT_OK) { // ???????????????????????????
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4?????????????????????????????????????????????
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4??????????????????????????????????????????
                        handleImageBeforeKitKat(data);
                    }
                }

                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // ?????????document?????????Uri????????????document id??????
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                // ????????????????????????id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content: //downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // ?????????content?????????Uri??????????????????????????????
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // ?????????file?????????Uri?????????????????????????????????
            imagePath = uri.getPath();
        }
        // ??????????????????????????????
        displayImage(imagePath);
    }

    /**
     * android 4.4?????????????????????
     * @param data
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // ??????Uri???selection??????????????????????????????
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show();
        }
    }

}

