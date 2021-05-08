package com.mini_tiktok.bytedance_camp_project.data;

import com.google.gson.annotations.SerializedName;

public class UploadResponse {
    @SerializedName("result")
    public VideoInfo videoInfo;
    @SerializedName("success")
    public boolean success;
}
