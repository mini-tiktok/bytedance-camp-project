package com.mini_tiktok.bytedance_camp_project.view;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mini_tiktok.bytedance_camp_project.R;
import com.mini_tiktok.bytedance_camp_project.data.VideoInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private TextView mAuthorName;
    private TextView mUpdateTime;
    private ImageView mVideoImage;

    public HomeCardViewHolder(@NonNull View itemView) {
        super(itemView);
        mAuthorName = itemView.findViewById(R.id.VideoAuthor);
        mUpdateTime=itemView.findViewById(R.id.VideoUpdateTime);
        mVideoImage=itemView.findViewById(R.id.VideoImage);
        itemView.setOnClickListener(this);
    }

    public void bind(VideoInfo videoInfo) {
        mAuthorName.setText(videoInfo.getUsername());
        Date date = new Date(videoInfo.getUpdateTime().getTime());
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        mUpdateTime.setText(sdf.format(date));
        Glide.with(mVideoImage).load(videoInfo.getImageUrl()).into(mVideoImage);
    }

    @Override
    public void onClick(View v) {
    }
}
