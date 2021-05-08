package com.mini_tiktok.bytedance_camp_project.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mini_tiktok.bytedance_camp_project.data.VideoInfo;
import com.mini_tiktok.bytedance_camp_project.service.VideoService;
import com.mini_tiktok.bytedance_camp_project.view.HomeCardViewHolder;

import java.util.ArrayList;
import java.util.List;
import com.mini_tiktok.bytedance_camp_project.R;

public class HomeSearchAdapter extends RecyclerView.Adapter<HomeCardViewHolder>{
    @NonNull
    private List<VideoInfo> mItems = new ArrayList<>();

    @NonNull
    @Override
    public HomeCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeCardViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_home_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCardViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void notifyItems(@NonNull List<VideoInfo> items) {
        mItems.clear();
        if(items!=null) {
            mItems.addAll(items);
        }
        notifyDataSetChanged();
    }

//    public void filter(@NonNull String text){
//        List<VideoInfo> tmp=mItems;
//        mItems=new ArrayList<>();;
//        for(VideoInfo videoInfo:tmp){
//            if(videoInfo.getUsername().contains(text)){
//                mItems.add(videoInfo);
//            }
//        }
//        notifyDataSetChanged();
//    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
