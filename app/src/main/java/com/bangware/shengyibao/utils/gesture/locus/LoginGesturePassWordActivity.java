package com.bangware.shengyibao.utils.gesture.locus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.main.view.MainActivity;
import com.bangware.shengyibao.net.NetWork;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.user.presenter.UserPresenter;
import com.bangware.shengyibao.user.presenter.impl.UserPresenterImpl;
import com.bangware.shengyibao.user.view.LoginActivity;
import com.bangware.shengyibao.user.view.LoginView;
import com.bangware.shengyibao.utils.AppContext;

/**
 * 手势密码正确登录成功页
 */
public class LoginGesturePassWordActivity extends BaseActivity implements LoginView{
    private LocusPassWordView lpwv;
    private UserPresenter userPresenter;
    private String name = "";
    private String pwd = "";
    private String userrealname = "";
    NetWork network = NetWork.getInstance();
    private Bundle bundle;
    private TextView clear_gesture_pwd,canvas_textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_gesture_pass_word);

        bundle = this.getIntent().getExtras();
        if (bundle != null){
            name = bundle.getString("user_name");
            pwd = bundle.getString("pass_word");
            userrealname = bundle.getString("user_real_name");
        }
        init();
        userPresenter = new UserPresenterImpl(this);
    }

    private void init(){
        canvas_textview = (TextView) findViewById(R.id.canvas_textview);
        lpwv = (LocusPassWordView) this.findViewById(R.id.mLocusPassWordView);
        lpwv.setOnCompleteListener(new LocusPassWordView.OnCompleteListener() {
            @Override
            public void onComplete(String mPassword) {
                // 判断网络连接
                if (network.IsConnect(LoginGesturePassWordActivity.this)){
                    // 如果密码正确,则进入主页面
                    if (lpwv.verifyPassword(mPassword)) {
                        userPresenter.doLogin(name,pwd);
                        canvas_textview.setText("手势密码绘制正确");
                        canvas_textview.setTextColor(Color.WHITE);
                    } else {
                        lpwv.markError();
                        canvas_textview.setText("手势密码绘制错误,请重新绘制！");
                        canvas_textview.setTextColor(Color.RED);
                    }
                }else {
                    lpwv.clearPassword();
                    Toast.makeText(LoginGesturePassWordActivity.this,"目前无网络连接！",Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * 清理手势密码
         */
        clear_gesture_pwd = (TextView)this.findViewById(R.id.clear_gesture_pwd);

        clear_gesture_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lpwv.clearGestureCache();
                clearUserCache();
                Intent intent = new Intent(LoginGesturePassWordActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 如果密码为空,则进入设置密码的界面
        if (lpwv.isPasswordEmpty()){
            Intent intent = new Intent(LoginGesturePassWordActivity.this,SetGesturePassWordActivity.class);
            Bundle bundleStr = new Bundle();
            bundleStr.putString("user_name",name);
            bundleStr.putString("pass_word",pwd);
            bundleStr.putString("user_real_name",userrealname);
            intent.putExtras(bundleStr);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void loginSuccess() {
        Toast.makeText(LoginGesturePassWordActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
        Bundle bundleStr = new Bundle();
        bundleStr.putString("user_real_name", userrealname);
        Intent intent = new Intent(LoginGesturePassWordActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(bundleStr);
        startActivity(intent);
        finish();
    }

    @Override
    public void onloginError(String ErrorMessage) {
        showMessage(ErrorMessage);
        lpwv.clearPassword();
        clearUserCache();
        Intent intent = new Intent(LoginGesturePassWordActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /**
     * 清除本地用户信息缓存
     */
    private void clearUserCache(){
        SharedPreferences sharedPreferences = this.getSharedPreferences(User.SHARED_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString("username", name);
        editor.commit();
    }

}
