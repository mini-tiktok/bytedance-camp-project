package com.mini_tiktok.bytedance_camp_project.view;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mini_tiktok.bytedance_camp_project.R;
import com.mini_tiktok.bytedance_camp_project.activity.VideoActivity;
import com.mini_tiktok.bytedance_camp_project.data.VideoInfo;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class HomeCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private TextView mAuthorName;
    private ImageView mVideoImage;
    private TextView mVideoPlayNum;
    private VideoInfo videoInfo;

    public HomeCardViewHolder(@NonNull View itemView) {
        super(itemView);
        mAuthorName = itemView.findViewById(R.id.VideoAuthor);
        mVideoImage=itemView.findViewById(R.id.VideoImage);
        mVideoPlayNum=itemView.findViewById(R.id.VideoPlayNum);
        itemView.setOnClickListener(this);
    }

    public void bind(VideoInfo videoInfo) {
        this.videoInfo=videoInfo;
        mAuthorName.setText(videoInfo.getUsername());
        mVideoPlayNum.setText(String.valueOf((int)(Math.random()*1000)));

        Glide.with(mVideoImage).load(videoInfo.getImageUrl()).into(mVideoImage);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), VideoActivity.class);
        intent.putExtra("videoUrl", videoInfo.getVideoUrl());
        intent.putExtra("username",videoInfo.getUsername());
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        intent.putExtra("updateTime",sdf.format(new Date(videoInfo.getUpdateTime().getTime())));
        v.getContext().startActivity(intent);
    }
}
