package com.tory.rednov.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.tory.rednov.R;
import com.tory.rednov.utilities.UtiToast;

import java.io.File;
import java.util.ArrayList;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "VideoActivity---> ";

    //private static final String DEBUG_URI = "rtsp://admin:888888@192.168.9.100:10554/tcp/av0_0";
    private static final String DEBUG_URI_ONLINE = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov";
    private static final String DEBUG_URI_LOCALFILE = "/storage/emulated/0/test.mp4";
    private static final String DEBUG_URI_INFRARED_IPC = "rtsp://admin:admin@192.168.9.6:554/0";

    private boolean isPlaying = false;

    private SurfaceView svVideoMain;

    private MediaPlayer mediaPlayer;

    private Uri videoUri;

    ImageView ivPlay;

    private static final boolean DEBUG_INPUT_IP = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Intent intent = getIntent();
        String ip = intent.getStringExtra(getString(R.string.intent_key_ip));
        Log.d(TAG, "onCreate: the ip is " + ip);
        videoUri = getUri(ip);
        if (videoUri == null) {
            UtiToast.Toast("The ip is invalid, please check out.");
            finish();
        }

        Log.d(TAG, "onCreate: the uri is " + videoUri);

        if (DEBUG_INPUT_IP) {
            finish();
            return;
        }


        SeekBar seekBar = findViewById(R.id.sbTime);
        seekBar.setMax(100);
        seekBar.setProgress(1);

        ivPlay = findViewById(R.id.ivPlay);
        ivPlay.setOnClickListener(this);

        ImageView ivStop = findViewById(R.id.ivStop);
        ivStop.setOnClickListener(this);

        ImageView ivNext = findViewById(R.id.ivNext);
        ivNext.setOnClickListener(this);

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

            mediaPlayer.getVLCVout().setVideoSurface(svVideoMain.getHolder().getSurface(), svVideoMain.getHolder());
            //播放前还要调用这个方法
            mediaPlayer.getVLCVout().attachViews();

            Media media = new Media(libVLC, videoUri);

            mediaPlayer.setMedia(media);
            mediaPlayer.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Uri getUri(String ip) {
        if (TextUtils.isEmpty(ip)) {
            Log.e(TAG, "getUri: the input ip is null, please check!", null);
            return null;
        }

        Uri uri = null;

        if (ip.equals(getString(R.string.play_list_local_file_ip))) {
            File file = new File(DEBUG_URI_LOCALFILE);
            if (file.exists()) {
                uri = Uri.fromFile(file);
            }
            else {
                UtiToast.Toast("The file" + DEBUG_URI_LOCALFILE + "does NOT exists.");
            }

        } else if (ip.equals(getString(R.string.play_list_internet_ip))) {
            uri = Uri.parse(DEBUG_URI_ONLINE);
        } else if (ip.equals(getString(R.string.play_list_infrared_ipc_ip))) {
            uri = Uri.parse(DEBUG_URI_INFRARED_IPC);
        } else {
            Log.e(TAG, "getUri: invalid ip! please check.", null);
        }

        return uri;
    }

    //TODO the player logic should be implemented.
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivNext:
                Log.d(TAG, "onClick: ivFullScreen");
                break;

            case R.id.ivStop:
                Log.d(TAG, "onClick: ivStop");
                break;

            case R.id.ivPlay:
                if (!isPlaying) {
                    isPlaying = true;
                    ivPlay.setImageResource(R.drawable.player_pause);

                    Log.d(TAG, "onClick: play");
                } else {
                    isPlaying = false;
                    Log.d(TAG, "should pause video.");
                    ivPlay.setImageResource(R.drawable.player_play);
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

    /*
    https://blog.csdn.net/u013274497/article/details/79041912
    //Not test yet
    public void changeVideoSize() {
        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();

        //根据视频尺寸去计算->视频可以在sufaceView中放大的最大倍数。 float max;
        if (getResources().getConfiguration().orientation== ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            //竖屏模式下按视频宽度计算放大倍数值 max = Math.max((float) videoWidth / (float) surfaceWidth,(float) videoHeight / (float) surfaceHeight);
        } else{
            //横屏模式下按视频高度计算放大倍数值 max = Math.max(((float) videoWidth/(float) surfaceHeight),(float) videoHeight/(float) surfaceWidth);
        }

        //视频宽高分别/最大倍数值 计算出放大后的视频尺寸 videoWidth = (int) Math.ceil((float) videoWidth / max);
        videoHeight = (int) Math.ceil((float) videoHeight / max);

        //无法直接设置视频尺寸，将计算出的视频尺寸设置到surfaceView 让视频自动填充。
        surfaceView.setLayoutParams(new RelativeLayout.LayoutParams(videoWidth, videoHeight));
    }
    */

}
