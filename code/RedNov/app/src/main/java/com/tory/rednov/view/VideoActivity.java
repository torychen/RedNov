package com.tory.rednov.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.tory.rednov.R;

public class VideoActivity extends AppCompatActivity {
    private static final String TAG = "VideoActivity_CCC";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Intent intent = getIntent();
        String ip = intent.getStringExtra(getString(R.string.intent_key_ip));

        Log.d(TAG, "onCreate: the ip is " + ip);

    }
}
