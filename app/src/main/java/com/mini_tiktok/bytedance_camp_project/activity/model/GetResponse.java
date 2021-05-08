package com.example.cs175_project.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetResponse {
    @SerializedName("feeds")
    public List<VideoResult> feeds;
    @SerializedName("success")
    public boolean success;
}
