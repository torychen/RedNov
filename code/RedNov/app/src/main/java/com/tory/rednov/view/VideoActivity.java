package com.tory.rednov.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;

import com.tory.rednov.MainActivity;
import com.tory.rednov.R;
import com.tory.rednov.utilities.UtiToast;

import java.io.File;
import java.util.ArrayList;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "VideoActivity_CCC";

    //private static final String DEBUG_URI = "rtsp://admin:888888@192.168.9.100:10554/tcp/av0_0";
    private static final String DEBUG_URI2 = "rtsp://admin:admin@192.168.9.6:554/0";
    private static final String DEBUG_URI_ONLINE = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov";

    private boolean isPlaying = false;

    //private VideoView videoView;
    private SurfaceView svVideoMain;

    private MediaPlayer mediaPlayer;

    private String videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Intent intent = getIntent();
        String ip = intent.getStringExtra(getString(R.string.intent_key_ip));


        SeekBar seekBar = findViewById(R.id.sbVideoProgress);
        seekBar.setMax(100);
        seekBar.setProgress(1);

        //videoView = findViewById(R.id.vvMain);
        //videoView.setMediaController(new MediaController(this));

        Button btnPlay = findViewById(R.id.btnPlayVideo);
        btnPlay.setOnClickListener(this);

        Button btnPlayFile = findViewById(R.id.btnPlayFile);
        btnPlayFile.setOnClickListener(this);

        svVideoMain  = findViewById(R.id.svVideoMain);
        LibVLC libVLC;
        ArrayList<String> options = new ArrayList<>();
        libVLC = new LibVLC(getApplication(), options);
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = new MediaPlayer(libVLC);
            String url = getUri();

            //
            mediaPlayer.getVLCVout().setVideoSurface(svVideoMain.getHolder().getSurface(), svVideoMain.getHolder());
            //播放前还要调用这个方法
            mediaPlayer.getVLCVout().attachViews();

            Media media = new Media(libVLC, Uri.parse(url));

            mediaPlayer.setMedia(media);
            mediaPlayer.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUri() {
        return DEBUG_URI_ONLINE;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPlayVideo:
                if (!isPlaying) {
                    isPlaying = true;
                    videoUri = getUri();
                    Log.d(TAG, "onCreate: the uri is " + videoUri);

                    //videoView.setVideoURI(Uri.parse(videoUri));
                    //videoView.requestFocus();
                    //videoView.start();
                    Log.d(TAG, "onClick: play");
                }
                break;

            case R.id.btnPlayFile:
                if (!isPlaying) {

                    ///storage/emulated/0/ is phone memory
                    File[] files;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        files = getExternalFilesDirs(Environment.MEDIA_MOUNTED);
                        for (File file : files) {
                            Log.d(TAG, "current storage path have:" + file.getAbsolutePath());
                        }
                    }

                    File file = new File(Environment.getExternalStorageDirectory(), "/test.mp4");
                    if (file.exists()) {
                        //videoView.setVideoPath(file.getPath()); // 指定视频文件的路径
                        Log.d(TAG, "sd path is " + file.getPath());

                    } else {
                        UtiToast.Toast("file NOT found. please check.");
                        return;
                    }

                    isPlaying = true;

                    //videoView.requestFocus();
                    //videoView.start();
                    Log.d(TAG, "onClick: play");
                } else {
                    //TODO Should pause playing.
                    Log.d(TAG, "should pause video.");


                }

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //videoView.pause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //videoView.pause();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isPlaying = false;

        //videoView.suspend();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        Log.d(TAG, "onDestroy: video");
    }
}
