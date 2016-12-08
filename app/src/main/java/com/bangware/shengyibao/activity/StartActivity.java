package com.bangware.shengyibao.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.Toast;

import com.bangware.shengyibao.net.NetWork;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.user.view.LoginActivity;
import com.bangware.shengyibao.utils.gesture.locus.LoginGesturePassWordActivity;
import com.bangware.shengyibao.utils.volley.DataRequest;


import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;


/**
 * 软件启动界面
 * */
public class StartActivity extends Activity {
	private long mExitTime=System.currentTimeMillis();
	NetWork network = NetWork.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		Message msg = hand.obtainMessage();
		hand.sendMessage(msg);

		JPushInterface.setDebugMode(true);// 设置开启日志,发布时请关闭日志
	    JPushInterface.init(this);// 初始化 JPush
	    DataRequest.buildRequestQueue(getApplicationContext());

	}

	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	Handler hand = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (isFristRun()) {
				new Thread() {//等待1.5秒
					public void run() {
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}.start();
				Intent intent = new Intent(StartActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			} else {
				if (network.IsConnect(StartActivity.this)){
					final Map<String,String> userInfo = getUserInfo();
					//如果没有用户信息缓存，则跳入登陆页面
					if("".equals(userInfo.get("username")) || "".equals(userInfo.get("password"))){
						Intent intent = new Intent(StartActivity.this,LoginActivity.class);
						startActivity(intent);
						finish();
					}else{
						Intent intent = new Intent(StartActivity.this,LoginGesturePassWordActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("user_name",userInfo.get("username"));
						bundle.putString("pass_word",userInfo.get("password"));
						bundle.putString("user_real_name",userInfo.get("user_real_name"));
						intent.putExtras(bundle);
						startActivity(intent);
						finish();
					}
				}
				else {
					Intent intent = new Intent(StartActivity.this,LoginActivity.class);
					startActivity(intent);
					finish();
				}
			}
		};
	};

	private Map<String,String>  getUserInfo(){
		SharedPreferences sharedPreferences = this.getSharedPreferences(User.SHARED_NAME, MODE_PRIVATE);
		Map<String,String> userInfo = new HashMap<String,String>();
		String username = sharedPreferences.getString("username", "");
		String password = sharedPreferences.getString("password", "");
		String user_real_name = sharedPreferences.getString("user_real_name","");
		userInfo.put("username", username);
		userInfo.put("password", password);
		userInfo.put("user_real_name",user_real_name);
		return userInfo;
	}
	private boolean isFristRun() {
		SharedPreferences sharedPreferences = this.getSharedPreferences(
				"share", MODE_PRIVATE);
		boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
		Editor editor = sharedPreferences.edit();
		if (!isFirstRun) {
			return false;
		} else {
			editor.putBoolean("isFirstRun", false);
			editor.commit();
			return true;
		}
	}


	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {// 如果两次按键时间间隔大于2000毫秒，则不退出
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();// 更新mExitTime
			} else {
				System.exit(0);// 否则退出程序
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}