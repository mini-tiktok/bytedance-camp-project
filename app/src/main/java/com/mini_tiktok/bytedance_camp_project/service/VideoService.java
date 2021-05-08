package com.mini_tiktok.bytedance_camp_project.service;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface VideoService {
    @GET("video")
    Call<JsonObject> getVideoInfo();
}
