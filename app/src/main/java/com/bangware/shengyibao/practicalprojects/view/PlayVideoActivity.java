package com.bangware.shengyibao.practicalprojects.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.bangware.shengyibao.activity.R;

public class PlayVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        String urlPath = getIntent().getExtras().getString("videoPath");

        Log.e("TAG", "onCreate: ====================>"+urlPath);
    }
}
