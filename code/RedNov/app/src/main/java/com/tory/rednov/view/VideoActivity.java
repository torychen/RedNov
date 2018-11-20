package com.tory.rednov.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tory.rednov.R;
import com.tory.rednov.utilities.SystemUtil;
import com.tory.rednov.utilities.UtiToast;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.videolan.libvlc.IVLCVout;
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

    private RelativeLayout rlVideoCtrl;

    private MediaPlayer mediaPlayer;

    private SeekBar.OnSeekBarChangeListener onTimeSeekBarChangeListener;
    private IVLCVout.Callback callback;
    private MediaPlayer.EventListener eventListener;

    private IVLCVout vlcVout;
    private boolean isFullScreen = false;

    private int videoWidth;
    private int videoHight;



    private Uri videoUri;

    ImageView ivPlay;

    private static final boolean DEBUG_INPUT_IP = false;
    private long totalTime;
    private SeekBar seekBarTime;
    private TextView tvTotalTime;
    private TextView tvCurrentTime;
    private ImageView ivFullScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Intent intent = getIntent();
        if (intent == null) {
            Log.d(TAG, "onCreate: no intent");
            return;
        }

        String videoType = intent.getStringExtra(getString(R.string.intent_key_video_type));
        if (TextUtils.isEmpty(videoType)) {
            Log.d(TAG, "onCreate: no video type in intent");
            return;
        }

        if (videoType.equals(getString(R.string.intent_value_video_type_local))) {
            File file = new File(intent.getStringExtra("FilePath"));
            videoUri = Uri.fromFile(file);
        } else {
            String ip = intent.getStringExtra(getString(R.string.intent_key_ip));
            Log.d(TAG, "onCreate: the ip is " + ip);
            videoUri = getUri(ip);
            if (videoUri == null) {
                UtiToast.Toast("The ip is invalid, please check out.");
                finish();
            }
        }

        Log.d(TAG, "onCreate: the uri is " + videoUri);

        if (DEBUG_INPUT_IP) {
            finish();
            return;
        }

        tvTotalTime = findViewById(R.id.tvTotalTime);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);

        seekBarTime = findViewById(R.id.sbTime);
        onTimeSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    if (!mediaPlayer.isSeekable() || totalTime == 0) {
                        return;
                    }

                    if (progress > totalTime) {
                        progress = (int) totalTime;
                    }

                    if (fromUser) {
                        mediaPlayer.setTime((long) progress);
                        tvCurrentTime.setText(SystemUtil.getMediaTime(progress));
                    }
                } catch (Exception e) {
                    Log.d("vlc-time", e.toString());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        ivPlay = findViewById(R.id.ivPlay);
        ivPlay.setOnClickListener(this);

        ImageView ivStop = findViewById(R.id.ivStop);
        ivStop.setOnClickListener(this);

        ImageView ivNext = findViewById(R.id.ivNext);
        ivNext.setOnClickListener(this);

        svVideoMain  = findViewById(R.id.svVideoMain);
        svVideoMain.getHolder().setKeepScreenOn(true);

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
            vlcVout = mediaPlayer.getVLCVout();

            callback = new IVLCVout.Callback() {
                public void onNewLayout(IVLCVout ivlcVout, int width, int height, int i2, int i3, int i4, int i5) {
                    try {
                        totalTime = mediaPlayer.getLength();
                        seekBarTime.setMax((int) totalTime);
                        tvTotalTime.setText(SystemUtil.getMediaTime((int) totalTime));

                        videoWidth = width;
                        videoHight = height;
                        Log.d(TAG, "onNewLayout: width is " + width + " height is " + height);

                        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                        Display display = windowManager.getDefaultDisplay();

                        Point point = new Point();
                        display.getSize(point);

                        ViewGroup.LayoutParams layoutParams = svVideoMain.getLayoutParams();
                        layoutParams.width = point.x;
                        layoutParams.height = (int) Math.ceil((float) videoHight * (float) point.x / (float) videoWidth);
                        svVideoMain.setLayoutParams(layoutParams);
                    } catch (Exception e) {
                        Log.d("vlc-newlayout", e.toString());
                    }
                }

                @Override
                public void onSurfacesCreated(IVLCVout vlcVout) {

                }

                @Override
                public void onSurfacesDestroyed(IVLCVout vlcVout) {

                }

                public void onHardwareAccelerationError(IVLCVout vlcVout) {
                    Log.e(TAG, "onHardwareAccelerationError: something wrong.", null);
                }
            };

            vlcVout.addCallback(callback);
            vlcVout.setVideoView(svVideoMain);
            vlcVout.attachViews();

            Media media = new Media(libVLC, videoUri);

            mediaPlayer.setMedia(media);
            eventListener = new MediaPlayer.EventListener() {
                @Override
                public void onEvent(MediaPlayer.Event event) {
                    try {
                        if (event.getTimeChanged() == 0 || totalTime == 0 || event.getTimeChanged() > totalTime) {
                            return;
                        }

                        seekBarTime.setProgress((int) event.getTimeChanged());
                        tvCurrentTime.setText(SystemUtil.getMediaTime((int) event.getTimeChanged()));

                        //播放结束
                        if (mediaPlayer.getPlayerState() == Media.State.Ended) {
                            seekBarTime.setProgress(0);
                            mediaPlayer.setTime(0);
                            tvTotalTime.setText(SystemUtil.getMediaTime((int) totalTime));
                            mediaPlayer.stop();
                            ivPlay.setImageResource(R.drawable.player_play);
                        }
                    } catch (Exception e) {
                        Log.d("vlc-event", e.toString());
                    }
                }
            };

            mediaPlayer.setEventListener(eventListener);

            ivFullScreen = findViewById(R.id.ivFullScreen);
            ivFullScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isFullScreen = !isFullScreen;
                    if (isFullScreen) {
                        ivFullScreen.setImageResource(R.drawable.player_normal_screen);
                        rlVideoCtrl.setVisibility(View.GONE);
                        WindowManager.LayoutParams params = getWindow().getAttributes();
                        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                        getWindow().setAttributes(params);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    } else {
                        ivFullScreen.setImageResource(R.drawable.player_full_screen);
                        rlVideoCtrl.setVisibility(View.VISIBLE);
                        WindowManager.LayoutParams params = getWindow().getAttributes();
                        params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        getWindow().setAttributes(params);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    }
                }
            });



            mediaPlayer.play();

            isPlaying = true;
            ivPlay.setImageResource(R.drawable.player_pause);

            rlVideoCtrl = findViewById(R.id.rlVideoCtrl);

            RelativeLayout rlEntireScreen = findViewById(R.id.rlPlayer);
            rlEntireScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFullScreen) {
                        rlVideoCtrl.setVisibility(View.VISIBLE);
                    }
                }
            });
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



    //TODO the player logic should be carefully reviewed.
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivNext:
                Log.d(TAG, "onClick: ivNext");
                break;

            case R.id.ivStop:
                Log.d(TAG, "onClick: ivStop");
                isPlaying = false;
                Log.d(TAG, "should pause video.");
                ivPlay.setImageResource(R.drawable.player_play);
                mediaPlayer.stop();
                seekBarTime.setProgress(0);

                break;

            case R.id.ivPlay:
                if (!isPlaying) {
                    isPlaying = true;
                    ivPlay.setImageResource(R.drawable.player_pause);
                    mediaPlayer.play();

                    Log.d(TAG, "onClick: play");
                } else {
                    isPlaying = false;
                    Log.d(TAG, "should pause video.");
                    ivPlay.setImageResource(R.drawable.player_play);
                    mediaPlayer.pause();
                }

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            resumePlay();
            mediaPlayer.play();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            pausePlay();
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            pausePlay();
            mediaPlayer.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isPlaying = false;

        if (mediaPlayer != null) {
            pausePlay();
            mediaPlayer.release();
        }

        Log.d(TAG, "onDestroy: video");
    }

    private void pausePlay() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            ivPlay.setImageResource(R.drawable.player_pause);
        }

        /*
        vlcVout.detachViews();

        seekBarTime.setOnSeekBarChangeListener(null);
        vlcVout.removeCallback(callback);
        mediaPlayer.setEventListener(null);
        */
    }

    private void resumePlay() {
        /*
        vlcVout.setVideoView(svVideoMain);
        vlcVout.attachViews();

        seekBarTime.setOnSeekBarChangeListener(onTimeSeekBarChangeListener);
        vlcVout.addCallback(callback);
        mediaPlayer.setEventListener(eventListener);
        */
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
