package com.mini_tiktok.bytedance_camp_project.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.mini_tiktok.bytedance_camp_project.R;


public class NoteActivity extends AppCompatActivity {

    private EditText editText;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setTitle(R.string.upload);


        editText = findViewById(R.id.edit_text);
//        editText.setFocusable(true);
//        editText.requestFocus();
//        InputMethodManager inputManager = (InputMethodManager)
//                getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (inputManager != null) {
//            inputManager.showSoftInput(editText, 0);
//        }

        addBtn = findViewById(R.id.btn_add);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence content = editText.getText();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}

