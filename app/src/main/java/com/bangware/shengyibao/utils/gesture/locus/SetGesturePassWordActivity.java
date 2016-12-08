package com.bangware.shengyibao.utils.gesture.locus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.utils.gesture.StringUtil;

/**
 * 重新设置手势密码
 */
public class SetGesturePassWordActivity extends Activity {
    private LocusPassWordView lpwv;
    private String password;
    private boolean needverify = true;
    private String name = "";
    private String pwd = "";
    private String userrealname = "";
    private TextView setting_canvas_textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_gesture_pass_word);

        init();
        initListener();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null){
            name = bundle.getString("user_name");
            pwd = bundle.getString("pass_word");
            userrealname = bundle.getString("user_real_name");
        }
    }

    private void init(){
        setting_canvas_textview = (TextView) findViewById(R.id.setting_canvas_textview);
        lpwv = (LocusPassWordView) this.findViewById(R.id.mSetLocusPassWordView);
        lpwv.setOnCompleteListener(new LocusPassWordView.OnCompleteListener() {
            @Override
            public void onComplete(String mPassword) {
                password = mPassword;
				if (needverify) {
					if (lpwv.verifyPassword(mPassword)) {
                        setting_canvas_textview.setText("手势密码绘制正确,请绘制新的手势密码!");
                        setting_canvas_textview.setTextColor(Color.WHITE);
						lpwv.clearPassword();
						needverify = false;
					} else {
                        setting_canvas_textview.setText("请先绘制原始密码!");
                        setting_canvas_textview.setTextColor(Color.RED);
						lpwv.clearPassword();
						password = "";
					}
				}
                /*if (StringUtil.isNotEmpty(password)) {
                    lpwv.resetPassWord(password);
                    lpwv.clearPassword();
                    showToast("密码修改成功,请记住密码.");
                    startActivity(new Intent(SetGesturePassWordActivity.this,
                            LoginGesturePassWordActivity.class));
                    finish();
                } else  {
                    lpwv.clearPassword();
                    showToast("密码不能为空,请输入密码.");
                }*/
            }
        });
    }

    private void initListener(){
        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tvSave:
                        if (StringUtil.isNotEmpty(password)) {
                            lpwv.resetPassWord(password);
                            lpwv.clearPassword();
                            setting_canvas_textview.setText("手势密码设置成功,请记住密码!");
                            setting_canvas_textview.setTextColor(Color.WHITE);
                            Intent intent = new Intent(SetGesturePassWordActivity.this,LoginGesturePassWordActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("user_name",name);
                            bundle.putString("pass_word",pwd);
                            bundle.putString("user_real_name",userrealname);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        } else {
                            lpwv.clearPassword();
                            Toast.makeText(SetGesturePassWordActivity.this,"密码不能为空,请绘制手势密码！",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.tvReset:
                        lpwv.clearPassword();
                        break;
                }
            }
        };
        Button buttonSave = (Button) this.findViewById(R.id.tvSave);
        buttonSave.setOnClickListener(mOnClickListener);
        Button tvReset = (Button) this.findViewById(R.id.tvReset);
        tvReset.setOnClickListener(mOnClickListener);
        // 如果密码为空,直接输入密码
        if (lpwv.isPasswordEmpty()) {
            this.needverify = false;
            /*setting_canvas_textview.setText("请绘制手势密码!");
            setting_canvas_textview.setTextColor(Color.WHITE);*/
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
