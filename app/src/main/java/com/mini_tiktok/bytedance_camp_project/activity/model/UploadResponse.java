package com.mini_tiktok.bytedance_camp_project.activity.model;

import com.google.gson.annotations.SerializedName;

public class UploadResponse {
    @SerializedName("url")
    public String url;
    @SerializedName("success")
    public boolean success;
}
