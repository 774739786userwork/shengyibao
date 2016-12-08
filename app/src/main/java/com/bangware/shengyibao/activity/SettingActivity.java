package com.bangware.shengyibao.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bangware.shengyibao.activity.Suggest.view.SuggestActivity;
import com.bangware.shengyibao.activity.zxing.MipcaActivityCapture;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.net.ThreadPoolUtils;
import com.bangware.shengyibao.thread.HttpGetThread;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.user.view.LoginActivity;
import com.bangware.shengyibao.user.view.UpdatePasswordActivity;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.gesture.locus.SetGesturePassWordActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingActivity extends BaseActivity {
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private ImageButton mSettingTitleBtnLeft;
    private TextView mUpdate_password_setting,gesture_password_update,mScan,suggest_textview;
    private Button  quitBtn;
    private String name = "";
    private String pwd = "";
    private String user_real_name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mSettingTitleBtnLeft= (ImageButton) findViewById(R.id.settingTitleBtnLeft);

        //修改密码
        mUpdate_password_setting= (TextView) findViewById(R.id.update_password_setting);
        mSettingTitleBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mUpdate_password_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingActivity.this,UpdatePasswordActivity.class);
                startActivity(intent);
            }
        });

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString("user_name");
            pwd = bundle.getString("pass_word");
            user_real_name = bundle.getString("user_real_name");
        }

        //修改手势密码
        gesture_password_update= (TextView) findViewById(R.id.gesture_password_update);
        gesture_password_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingActivity.this,SetGesturePassWordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_name",name);
                bundle.putString("pass_word",pwd);
                bundle.putString("user_real_name",user_real_name);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //扫一扫
        mScan= (TextView) findViewById(R.id.saoyisao);
        mScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }
        });
        //意见反馈
        suggest_textview = (TextView) findViewById(R.id.suggest_textview);
        suggest_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingActivity.this,SuggestActivity.class);
                startActivity(intent);
            }
        });

        //安全退出
        quitBtn = (Button) findViewById(R.id.safeQuit);
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = Model.HTTPURL+"users/sign_out.json?token="+ AppContext.getInstance().getUser().getLogin_token();
                clearUserCache();
                ThreadPoolUtils.execute(new HttpGetThread(hand, url));
                Intent loginIntent = new Intent(SettingActivity.this,LoginActivity.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    Uri uri=null;
                    String text=bundle.getString("result").toString();
                    Pattern p = Pattern.compile("[0-9a-zA-Z]*");
                    Matcher m = p.matcher(text);
                    if (m.matches()) {
                   showToast("暂无该产品信息");

                    }else{
                        uri = Uri.parse(text);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                }
                break;
        }
    }

    /**
     * 清除本地用户信息缓存
     */
    private void clearUserCache(){
        SharedPreferences sharedPreferences = this.getSharedPreferences(User.SHARED_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString("username", AppContext.getInstance().getUser().getUser_name());
        editor.commit();
    }

    Handler hand = new Handler(){
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if(msg.what == 404){
            }
            if(msg.what == 100){
            }
            if(msg.what == 200){
                String result = (String) msg.obj;
                if (result != null) {
                }
            }
        };
    };
}
