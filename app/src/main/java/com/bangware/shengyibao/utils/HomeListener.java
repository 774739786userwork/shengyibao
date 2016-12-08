package com.bangware.shengyibao.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Home按键监听类，实现广播监听
 * Created by bangware on 2016/11/7.
 */
public class HomeListener {
    private Context mContext = null;
    private IntentFilter mHomeBtnIntentFilter = null;
    private OnHomeBtnPressLitener mOnHomeBtnPressListener = null;
    private HomeBtnReceiver mHomeBtnReceiver = null;

    public HomeListener(Context context) {
        this.mContext = context;
        mHomeBtnReceiver = new HomeBtnReceiver();
        mHomeBtnIntentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    }

    public void setOnHomeBtnPressListener( OnHomeBtnPressLitener onHomeBtnPressListener ){
        mOnHomeBtnPressListener = onHomeBtnPressListener;
    }

    public void start(){
        if (mHomeBtnReceiver != null){
            mContext.registerReceiver( mHomeBtnReceiver, mHomeBtnIntentFilter );
        }
    }

    public void stop(){
        if (mHomeBtnReceiver != null) {
            mContext.unregisterReceiver(mHomeBtnReceiver);
        }
    }

    class HomeBtnReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            receive(context, intent);
        }
    }

    private void receive(Context context, Intent intent){
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra( "reason" );
            if (reason != null) {
                if( null != mOnHomeBtnPressListener ){
                    if( reason.equals( "homekey" ) ){
                        // 按Home按键
                        mOnHomeBtnPressListener.onHomeBtnPress( );
                    }else if( reason.equals( "recentapps" ) ){
                        // 长按Home按键
                        mOnHomeBtnPressListener.onHomeBtnLongPress( );
                    }
                }
            }
        }
    }

    public interface OnHomeBtnPressLitener{
        public void onHomeBtnPress();
        public void onHomeBtnLongPress();
    }
}
