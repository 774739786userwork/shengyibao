package com.bangware.shengyibao.practicalprojects.view;

import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.Model;


public class PlayVideoActivity extends BaseActivity implements MediaPlayer.OnCompletionListener {
    private RelativeLayout playVideoRelLayout;
    private TextureView playVideoTextureView;
    private ImageView playVideoImage;
    private MediaPlayer playMedia;
    private String urlPath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        findView();
        setListener();
    }

    private void findView(){
        urlPath = getIntent().getExtras().getString("videoPath");
        playVideoRelLayout = (RelativeLayout) findViewById(R.id.play_video_parent);
        playVideoTextureView = (TextureView) findViewById(R.id.play_video_textureview);
        playVideoImage = (ImageView) findViewById(R.id.play_image);

        playMedia = new MediaPlayer();

        //获取屏幕的分辨率
        /*DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) playVideoRelLayout.getLayoutParams();
        layoutParams.width = dm.widthPixels;
        layoutParams.height = dm.widthPixels;
        playVideoRelLayout.setLayoutParams(layoutParams);*/
    }

    private void setListener(){
        PlayOnClick playclick = new PlayOnClick();
        playVideoRelLayout.setOnClickListener(playclick);
        playVideoImage.setOnClickListener(playclick);
        playVideoTextureView.setOnClickListener(playclick);

        PlaySurfaceTextureListener textureListener = new PlaySurfaceTextureListener();
        playVideoTextureView.setSurfaceTextureListener(textureListener);
        playMedia.setOnCompletionListener(this);
    }

    //点击事件
    private class PlayOnClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.play_image:
                    if (!playMedia.isPlaying()){
                        playMedia.start();
                    }
                    playVideoImage.setVisibility(View.GONE);
                    break;
                case R.id.play_video_textureview:
                    if (playMedia.isPlaying()){
                        playMedia.pause();
                        playVideoImage.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }

    //surfaceView播放视频事件
    private class PlaySurfaceTextureListener implements TextureView.SurfaceTextureListener{

        @Override
        public void onSurfaceTextureAvailable(final SurfaceTexture surfaceTexture, int i, int i1) {
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    preparePlay(new Surface(surfaceTexture));
                }
            }, 500);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    }

    public void preparePlay(Surface surface) {
        try {
            playMedia.reset();
            playMedia.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 设置需要播放的视频
            playMedia.setDataSource(Model.HTTPURL + urlPath);
            // 把视频画面输出到Surface
            playMedia.setSurface(surface);
            playMedia.setLooping(true);
            playMedia.prepare();
            playMedia.seekTo(0);
        } catch (Exception e) {
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        playMedia.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        playMedia.stop();
        finish();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        playVideoImage.setVisibility(View.VISIBLE);
    }
}
