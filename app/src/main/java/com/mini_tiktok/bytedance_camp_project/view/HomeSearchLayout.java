package com.mini_tiktok.bytedance_camp_project.view;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import com.mini_tiktok.bytedance_camp_project.R;

public class HomeSearchLayout extends LinearLayout {
    private static final String TAG = "SearchLayout";
    private EditText mEditView;
    private OnSearchTextChangedListener mListener;

    public HomeSearchLayout(Context context) {
        super(context);
        initView();
    }

    public HomeSearchLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HomeSearchLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.layout_home_search, this);

        mEditView = findViewById(R.id.edit);
        ImageView mImageView = findViewById(R.id.image);

        mImageView.setImageResource(R.drawable.icon_search);

        mEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Log.i(TAG, "beforeTextChanged: " + s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.i(TAG, "onTextChanged: " + s);
            }

            @Override
            public void afterTextChanged(Editable s) {
//                Log.i(TAG, "afterTextChanged: " + s);
                if (mListener != null) {
                    mListener.afterChanged(s.toString());
                }
            }
        });
    }

    public void setOnSearchTextChangedListener(OnSearchTextChangedListener listener) {
        mListener = listener;
    }

    public interface OnSearchTextChangedListener {

        void afterChanged(String text);

    }
}
