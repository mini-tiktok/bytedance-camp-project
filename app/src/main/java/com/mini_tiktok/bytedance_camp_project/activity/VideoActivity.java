package com.mini_tiktok.bytedance_camp_project.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.mini_tiktok.bytedance_camp_project.R;
import com.mini_tiktok.bytedance_camp_project.util.MyClick;

public class VideoActivity extends AppCompatActivity {
    boolean startPlay;
    private VideoView videoView;
    private LottieAnimationView heart;
    private ImageView videoPause;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        String videoUrl=getIntent().getStringExtra("videoUrl");
        System.out.println("play video: "+videoUrl);
        videoView = findViewById(R.id.video_container);
        heart = findViewById(R.id.heart);
        videoPause = findViewById(R.id.imageView);
        videoPause.bringToFront();
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(Uri.parse(videoUrl));
        videoView.requestFocus();
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                startPlay = true;
            }
        });

        heart.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(animation.getAnimatedFraction() == 1f){
                    if(heart.getVisibility() == View.VISIBLE){
                        heart.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        videoView.setOnTouchListener(new MyClick(new MyClick.MyClickCallBack() {
            @Override
            public void oneClick() {
                if(startPlay) {
                    if (videoView.isPlaying()) {
                        videoView.pause();
                        videoPause.setVisibility(View.VISIBLE);
                    } else {
                        videoView.start();
                        videoPause.setVisibility(View.INVISIBLE);
                    }
                }
            }
            @Override
            public void doubleClick() {
                if(startPlay) {
                    heart.playAnimation();
                    heart.setVisibility(View.VISIBLE);
                }
            }
        }));

    }

}