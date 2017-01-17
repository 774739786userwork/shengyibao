package com.bangware.shengyibao.user.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.manager.JPushMessageManager;
import com.bangware.shengyibao.net.ThreadPoolUtils;
import com.bangware.shengyibao.thread.HttpGetThread;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.user.presenter.UserPresenter;
import com.bangware.shengyibao.user.presenter.impl.UserPresenterImpl;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.gesture.locus.LoginGesturePassWordActivity;
import com.bangware.shengyibao.utils.volley.DataRequest;

import java.util.Timer;
import java.util.TimerTask;


public class LoginActivity extends BaseActivity implements LoginView {

	private EditText mLogin_user, mLogin_password;
	private TextView mLogin_OK;
	private CheckBox mIsRememberMe;
	private UserPresenter userPresenter;
	SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (savedInstanceState!=null)
		{
			DataRequest.buildRequestQueue(this);
		}
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);


		findViews();
		setListeners();
		userPresenter = new UserPresenterImpl(this);
	}

	private void findViews() {
		mLogin_user = (EditText) findViewById(R.id.Login_user);
		mLogin_password = (EditText) findViewById(R.id.Login_password);
		mIsRememberMe = (CheckBox)findViewById(R.id.Login_rememberme);


		/**
		 * 判断记住密码状态
		 */
		sharedPreferences = this.getSharedPreferences(User.SHARED_NAME, MODE_PRIVATE);
		if (sharedPreferences.getBoolean("ISCHECK", true)){
			//设置默认是记录密码状态
			mIsRememberMe.setChecked(true);
			mLogin_user.setText(sharedPreferences.getString("username", ""));
			mLogin_password.setText(sharedPreferences.getString("password", ""));
		}else{
			mIsRememberMe.setChecked(false);
			mLogin_user.setText(sharedPreferences.getString("username", ""));
			mLogin_password.setText("");
		}

		mLogin_OK = (TextView) findViewById(R.id.Login_OK);
		
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	private void setListeners(){
		MyOnClickListener myonclick = new MyOnClickListener();
		mLogin_OK.setOnClickListener(myonclick);

		mIsRememberMe.setOnCheckedChangeListener(changeListener);//给记住密码设置选中点击事件
	}

	private CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton.OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (buttonView.getId() == R.id.Login_rememberme){
				//判断点击是否被选中
				if (isChecked){
					mIsRememberMe.setChecked(true);
					sharedPreferences.edit().putBoolean("ISCHECK",true).commit();
				}else{
					mIsRememberMe.setChecked(false);
					sharedPreferences.edit().putBoolean("ISCHECK",false).commit();
				}
			}
		}
	};
	
	private class MyOnClickListener implements View.OnClickListener {
		public void onClick(View arg0) {
			int mID = arg0.getId();
			
			if (mID == R.id.Login_OK) {
				String username = mLogin_user.getText().toString().trim();
				String password = mLogin_password.getText().toString().trim();
				
				if(username==null || "".equals(username)){
					Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_LONG).show();
					return;
				}
				if(password==null || "".equals(password)){
					Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
					return;
				}
				userPresenter.doLogin(username, password);
				//调用消息推送接口发送手机设备序列号到后台
			}
		}
	}

	int keyBackClickCount = 0;
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			switch (keyBackClickCount++) {
				case 0:
					Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
					Timer timer = new Timer();
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							keyBackClickCount = 0;
						}
					}, 3000);
					break;
				case 1:
					finish();
					System.exit(0);
					break;
				default:
					break;
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void loginSuccess(){
		Toast.makeText(this, "登陆成功！", Toast.LENGTH_SHORT).show();
		cacheUser(AppContext.getInstance().getUser());
		Intent intent = new Intent(LoginActivity.this,LoginGesturePassWordActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("user_name",AppContext.getInstance().getUser().getUser_name());
		bundle.putString("pass_word",AppContext.getInstance().getUser().getPassword());
		bundle.putString("user_real_name",AppContext.getInstance().getUser().getUser_realname());
		intent.putExtras(bundle);
		this.startActivity(intent);	
		this.finish();
		JPushMessageManager pushMessageManager = new JPushMessageManager(LoginActivity.this);
		pushMessageManager.doMessage();
	}
	@Override
	public void onloginError(String ErrorMessage){
		showMessage(ErrorMessage);
	}

	/**
	 * 缓存用户信息
	 */
	public void cacheUser(User user){
		SharedPreferences sharedPreferences = this.getSharedPreferences(User.SHARED_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		
		editor.putString("login_token", user.getLogin_token());
		editor.putString("user_id", user.getUser_id());
		editor.putString("username", user.getUser_name());
		editor.putString("password", user.getPassword());
		editor.putString("user_real_name", user.getUser_realname());
		editor.putString("organization_id", user.getOrg_id());
		editor.putString("organization_name", user.getOrg_name());
		editor.putString("roles", user.getRoles());
		editor.putString("app_id",user.getApp_id());
		editor.putString("employee_id", user.getEmployee_id());
		editor.putString("mobile_number", user.getMobile_number());
		editor.commit();
	}

	public void onDestroy(){
		super.onDestroy();
		this.userPresenter.destroy();
	}
}
