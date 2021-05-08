package com.example.cs175_project.model;

import com.google.gson.annotations.SerializedName;

public class UploadResponse {
    @SerializedName("result")
    public VideoResult videoResult;
    @SerializedName("url")
    public String url;
    @SerializedName("success")
    public boolean success;
}
