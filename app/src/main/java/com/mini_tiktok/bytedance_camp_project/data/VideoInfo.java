package com.mini_tiktok.bytedance_camp_project.data;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.util.Date;

import lombok.Data;

@Data
public class VideoInfo {
    @SerializedName("user_name")
    String username;
    @SerializedName("video_url")
    String videoUrl;
    @SerializedName("image_url")
    String imageUrl;
    @SerializedName("image_w")
    Integer imageWidth;
    @SerializedName("image_h")
    Integer imageHeight;
    @SerializedName("createdAt")
    Timestamp createTime;
    @SerializedName("updatedAt")
    Timestamp updateTime;
}
