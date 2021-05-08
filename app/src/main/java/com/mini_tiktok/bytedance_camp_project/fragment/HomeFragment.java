package com.mini_tiktok.bytedance_camp_project.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mini_tiktok.bytedance_camp_project.R;
import com.mini_tiktok.bytedance_camp_project.adapter.HomeSearchAdapter;
import com.mini_tiktok.bytedance_camp_project.data.VideoInfo;
import com.mini_tiktok.bytedance_camp_project.service.VideoService;
import com.mini_tiktok.bytedance_camp_project.util.Constant;
import com.mini_tiktok.bytedance_camp_project.view.HomeSearchLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private RecyclerView mRecyclerView;
    private HomeSearchAdapter mSearchAdapter = new HomeSearchAdapter();
    private HomeSearchLayout mSearchLayout;
    private List<VideoInfo>videoInfoList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = root.findViewById(R.id.HomeRecyclerView);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mSearchAdapter);
        getData();
        mSearchLayout = root.findViewById(R.id.HomeSearch);
        mSearchLayout.setOnSearchTextChangedListener(new HomeSearchLayout.OnSearchTextChangedListener() {
                    @Override
                    public void afterChanged(String text) {
                        Log.i(TAG, "afterChanged: " + text);
                        List<VideoInfo> tmp=new ArrayList<>();
                        for(VideoInfo videoInfo:videoInfoList){
                            if(videoInfo.getUsername().contains(text)){
                                tmp.add(videoInfo);
                            }
                        }
                        mSearchAdapter.notifyItems(tmp);
                    }
                });
        return root;
    }

    private void getData(){
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BYTEDANCE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<JsonObject> call= retrofit.create(VideoService.class).getVideoInfo();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    videoInfoList = new Gson().fromJson(response.body().get("feeds").getAsJsonArray(),
                            new TypeToken<List<VideoInfo>>(){}.getType());
//                    System.out.println(videoInfoList.toString());
//                    videoInfoList=videoInfoList.subList(0,20);
                    mSearchAdapter.notifyItems(videoInfoList);
                } else {
                    Snackbar.make(mRecyclerView, "网络错误，无法接收", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG,"fetch video failed");
                Snackbar.make(mRecyclerView, "网络错误，无法接收", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}