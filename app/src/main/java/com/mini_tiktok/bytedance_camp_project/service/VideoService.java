package com.mini_tiktok.bytedance_camp_project.service;

import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface VideoService {

    @Multipart
    @POST("video")
    Call<com.mini_tiktok.bytedance_camp_project.activity.model.UploadResponse> submitVideo(@Query("student_id") String studentId,
                                     @Query("user_name") String userName,
                                     @Query("extra_value") String extraValue,
                                     @Part MultipartBody.Part coverImage,
                                     @Part MultipartBody.Part video);

    @GET("video")
    Call<JsonObject> getVideoInfo();
}
