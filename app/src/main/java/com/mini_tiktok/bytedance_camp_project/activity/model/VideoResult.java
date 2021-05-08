package com.example.cs175_project.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class VideoResult {
    @SerializedName("_id")
    public String Id;
    @SerializedName("student_id")
    public String studentId;
    @SerializedName("user_name")
    public String userName;
    @SerializedName("video_url")
    public String videoUrl;
    @SerializedName("image_url")
    public String imageUrl;
    @SerializedName("image_w")
    public int imageW;
    @SerializedName("image_h")
    public int imageH;
    @SerializedName("createdAt")
    public Date createdAt;
    @SerializedName("updatedAt")
    public Date updatedAt;
}
