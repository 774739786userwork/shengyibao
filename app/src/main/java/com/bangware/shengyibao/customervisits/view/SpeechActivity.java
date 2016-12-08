package com.bangware.shengyibao.customervisits.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.utils.CommonUtil;

import java.io.File;

/**
 * Created by bangware on 2016/11/29.
 */

public class SpeechActivity extends BaseActivity{
    private Button bntRecord;
    private int flag=1;
    private View rcChat_popup;
    private Handler mHandler = new Handler();
    private long startVoiceT, endVoiceT;
    private String voiceName;
    private SoundMeter mSensor;
    private static final int POLL_INTERVAL = 300;
    private ImageView volume;
    private LinearLayout voice_rcd_hint_rcding;
    private Chronometer timedown;//显示倒计时
    private TextView textview_replace;//文本替换

    private long timeTotalInS = 0;
    private long timeLeftInS = 0;
    private long remain_time = 0;
    private MediaPlayer player ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speechfile);

        bntRecord=(Button)findViewById(R.id.bntRecord);
        rcChat_popup = this.findViewById(R.id.rcChat_popup);
        volume = (ImageView) this.findViewById(R.id.volume);
        textview_replace = (TextView) findViewById(R.id.textview_replace);
        voice_rcd_hint_rcding = (LinearLayout)this.findViewById(R.id.voice_rcd_hint_rcding);
        timedown=(Chronometer)findViewById(R.id.timedown);
        mSensor = new SoundMeter();
        //触摸录音按钮触发事件
        bntRecord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // TODO Auto-generated method stub
                //手指按下事件
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    textview_replace.setVisibility(View.GONE);
                    int[] location = new int[2];
                    bntRecord.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
                    int btn_rc_Y = location[1];
                    int btn_rc_X = location[0];
                    if(flag==1){
                        if (!Environment.getExternalStorageDirectory().exists()) {
                            Toast.makeText(SpeechActivity.this, "没有内存卡！", Toast.LENGTH_LONG).show();
                            return false;
                        }
                        System.out.println("2");
                        System.out.println(event.getY()+"..."+btn_rc_Y+"...."+event.getX() +"...."+btn_rc_X);
                        if (event.getY() < btn_rc_Y && event.getX() > btn_rc_X) {//判断手势按下的位置是否是语音录制按钮的范围内
                            System.out.println("3");
                            rcChat_popup.setVisibility(View.VISIBLE);
                            mHandler.postDelayed(new Runnable() {
                                public void run() {
                                }
                            }, 300);
                            voiceName = CommonUtil.getUUID32() + ".mp3";
                            start(voiceName);
                            //设置录音时间
                            timedown.setVisibility(View.VISIBLE);
                            initTimer(60);
                            timedown.start();
                            flag = 2;
                        }
                    }
                }
                //手指抬起事件
                else if(event.getAction()==MotionEvent.ACTION_UP){
                    textview_replace.setVisibility(View.VISIBLE);
                    textview_replace.setText("长按录制语音");
                    timedown.stop();
                    if(flag==2){
                        rcChat_popup.setVisibility(View.GONE);
                        timedown.setVisibility(View.GONE);
                        stop();
                        flag = 1;

                        /**
                         * 判断按住时间及回传值
                         */
                        remain_time = timeTotalInS - timeLeftInS;
                        if (remain_time < 2){
                            showToast("按住时间太短！");
                        }else {
                            Intent intent = new Intent();
                            intent.putExtra("speechContent", voiceName);
                            intent.putExtra("speechTime", String.valueOf(remain_time));
                            setResult(1001,intent);
                            finish();
                        }
                    }else {
                        stop();
                        endVoiceT = SystemClock.currentThreadTimeMillis();
                        flag = 1;
                    }
                }
                //手指移动事件
                else if (event.getAction() == MotionEvent.ACTION_MOVE){
                    int[] del_location = new int[2];
                    bntRecord.getLocationInWindow(del_location);
                    int del_Y = del_location[1];
                    int del_x = del_location[0];
                    if (flag == 2){
                        if (event.getY() < del_Y && event.getX() < del_x) {
                            textview_replace.setVisibility(View.VISIBLE);
                            textview_replace.setText("松开手指，取消录制！");
                            //删除语音文件
                            CommonUtil.deleteFile(android.os.Environment.getExternalStorageDirectory()+"/shengyibao_voice/"+ voiceName);
                            voiceName = "";
                            rcChat_popup.setVisibility(View.GONE);
                            timedown.stop();
                            timedown.setVisibility(View.GONE);
                            flag = 1;
                        }
                    }
                }
                return false;
            }
        });
    }


    private Runnable mSleepTask = new Runnable() {
        public void run() {
            stop();
        }
    };
    private Runnable mPollTask = new Runnable(){
        public void run() {
            double amp = mSensor.getAmplitude();
            updateDisplay(amp);
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        }
    };
    private void start(String name) {
        mSensor.start(name);
        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    }

    private void stop() {
        mHandler.removeCallbacks(mSleepTask);
        mHandler.removeCallbacks(mPollTask);
        mSensor.stop();
        volume.setImageResource(R.drawable.amp1);
    }

    private void updateDisplay(double signalEMA) {

        switch ((int) signalEMA) {
            case 0:
            case 1:
                volume.setImageResource(R.drawable.amp1);
                break;
            case 2:
            case 3:
                volume.setImageResource(R.drawable.amp2);
                break;
            case 4:
            case 5:
                volume.setImageResource(R.drawable.amp3);
                break;
            case 6:
            case 7:
                volume.setImageResource(R.drawable.amp4);
                break;
            case 8:
            case 9:
                volume.setImageResource(R.drawable.amp5);
                break;
            case 10:
            case 11:
                volume.setImageResource(R.drawable.amp6);
                break;
            default:
                volume.setImageResource(R.drawable.amp7);
                break;
        }
    }

    /**
     * 初始化计时器，计时器是通过widget.Chronometer来实现的
     * @param total 一共多少秒
     */
    private void initTimer(long total) {
        this.timeTotalInS = total;
        this.timeLeftInS = total;
        timedown.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (timeLeftInS <= 0) {
                    Toast.makeText(SpeechActivity.this, "录音时间到", Toast.LENGTH_SHORT).show();
                    timedown.stop();
                    //录音停止
                    stop();
                    rcChat_popup.setVisibility(View.GONE);
                    timedown.setVisibility(View.GONE);
                    return;
                }
                timeLeftInS--;
                refreshTimeLeft();
            }
        });
    }
    private void refreshTimeLeft() {
        this.timedown.setText("录音时间剩余：" + timeLeftInS);
        //TODO 格式化字符串
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if(player!=null){
            player.stop();
            player.release();
            player=null;
        }
    }
}
