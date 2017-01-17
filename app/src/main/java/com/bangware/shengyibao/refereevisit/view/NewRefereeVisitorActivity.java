package com.bangware.shengyibao.refereevisit.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.refereevisit.presenter.NewRefereeVisitorPresenter;
import com.bangware.shengyibao.refereevisit.presenter.impl.NewRefereePresenterImpl;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.AppContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewRefereeVisitorActivity extends BaseActivity implements NewRefereeVisitorsView{
    private ImageButton new_refereevisitor_Goback;
    private TextView new_refereevisitor_commit;
    private EditText new_refereevisitor_Nas;
    private EditText new_refereevisitor_Phone;
    private EditText new_refereevisitor_relation;
    private String name,phone,relation;
    private NewRefereeVisitorPresenter presenter;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_referee_visitor);
        SharedPreferences sharedPreferences=this.getSharedPreferences(User.SHARED_NAME, MODE_PRIVATE);
        user= AppContext.getInstance().readFromSharedPreferences(sharedPreferences);
        findView();
        setLisenter();
    }

    private void setLisenter() {
        new_refereevisitor_Goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        new_refereevisitor_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
            }
        });
    }

    private void findView() {
        new_refereevisitor_Goback= (ImageButton) findViewById(R.id.new_refereevisitor_Goback);
        new_refereevisitor_commit= (TextView) findViewById(R.id.new_refereevisitor_commit);
        new_refereevisitor_Nas= (EditText) findViewById(R.id.new_refereevisitor_Nas);
        new_refereevisitor_Phone= (EditText) findViewById(R.id.new_refereevisitor_Phone);
        new_refereevisitor_relation= (EditText) findViewById(R.id.new_refereevisitor_relation);
    }
    //得到输入框的内容信息
    private void init() {
        if (!TextUtils.isEmpty(new_refereevisitor_Nas.getText().toString())&&isPhoneNumberValid(new_refereevisitor_Phone.getText().toString()))
        {
            name=new_refereevisitor_Nas.getText().toString();
            phone=new_refereevisitor_Phone.getText().toString();
            relation=new_refereevisitor_relation.getText().toString();
            Log.e("NewRefereePresenterImpl",name+phone+relation);
            presenter=new NewRefereePresenterImpl(this);
            presenter.addRefereeVisitors(user,name,phone,relation);
            loadingdialog.show();
        }else
        {
            showToast("姓名和手机1号码不能为空或号码格式不正确");
        }
    }
    //判断电话号码是否正确输入
    public boolean isPhoneNumberValid(String phoneNumber) {
        String expression = "((^(13|15|18|17|14)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
        CharSequence inputStr = phoneNumber;

        Pattern pattern = Pattern.compile(expression);

        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {
            return true;
        }else{
            showToast("手机或电话号码不正确!格式如：13855558888或01088885555");
        }
        return false;
    }

    @Override
    public void addRefereeSuccessMessage(String successMessage) {
        showToast("添加成功");
        loadingdialog.dismiss();
    }

    @Override
    public void save() {
     finish();
    }

    public void onDestroy(){
        super.onDestroy();
        if(this.presenter!=null)
            this.presenter.destory();
    }
}
