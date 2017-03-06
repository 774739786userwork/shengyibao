package com.bangware.shengyibao.main.view;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.activity.SettingActivity;
import com.bangware.shengyibao.main.model.entity.ImageShow;
import com.bangware.shengyibao.updateversion.VersionUpdateView;
import com.bangware.shengyibao.updateversion.model.entity.VersionBean;
import com.bangware.shengyibao.updateversion.presenter.UpdateVersionPresenter;
import com.bangware.shengyibao.updateversion.presenter.UpdateVersionPresenterImpl;
import com.bangware.shengyibao.updateversion.service.UpdateVersionService;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.VersionUtil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends BaseActivity {
    private Fragment mContent;

    SharedPreferences sharedPreferences;
    private User user;
    /**
     * 首页图片变量
     */
    private ImageView setting_imageview,home_imageview;
    private ViewPager viewPager;
    private TextView tv_intro;
    private LinearLayout dot_layout;
    private ArrayList<ImageShow> list;
    private MyPagerAdapter myPagerAdapter;
    /**
     * 用于设置自动轮播
     */
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            handler.sendEmptyMessageDelayed(0, 4000);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initData();
        initListener();

        String app_id=user.getApp_id();
        if (app_id.equals("0") ) {
            //南厂版本
            mContent = new FragmentSaler();
        }else if(app_id.equals("1")) {
            //北厂硅藻泥业务员版本
            mContent = new FragmentTwoSaler();
        }else if (app_id.equals("2")) {
            //北厂硅藻泥销售主管版本
            mContent = new FragmentTwoSaler();
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        //FIXME modify by mouse
        if (fm.findFragmentById(R.id.main_FrameLayout) != null) {
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            transaction.replace(R.id.main_FrameLayout, mContent);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }


    private void init(){
        setting_imageview = (ImageView) findViewById(R.id.setting_imageview);
        home_imageview = (ImageView) findViewById(R.id.home_imageview);
        viewPager = (ViewPager)findViewById(R.id.viewpage);
        tv_intro = (TextView) findViewById(R.id.tv_intro);
        dot_layout = (LinearLayout)findViewById(R.id.dot_layout);
        sharedPreferences=this.getSharedPreferences(User.SHARED_NAME, MODE_PRIVATE);

        user=AppContext.getInstance().readFromSharedPreferences(sharedPreferences);
    }

    //初始化数据
    private void initData(){
        list = new ArrayList<ImageShow>();
        list.add(new ImageShow(R.drawable.img1, "多邦生态硅藻泥改善睡眠"));
        list.add(new ImageShow(R.drawable.img2, "新品上市外墙耐水腻子粉"));
        list.add(new ImageShow(R.drawable.img3, "多邦瓷砖胶新品上市啦"));
        initDots();

        myPagerAdapter = new MyPagerAdapter(this,list);
        viewPager.setAdapter(myPagerAdapter);
        int centerValue = Integer.MAX_VALUE/2;
        int value = centerValue % list.size();
        //设置viewPager的第一页为最大整数的中间数，实现伪无限循环
        viewPager.setCurrentItem(centerValue-value);
        updateIntroAndDot();
        handler.sendEmptyMessageDelayed(0,4000);
        //版本更新调用
    }

    //初始化文字下方的圆点
    private void initDots() {
        for (int i=0; i< list.size(); i++)
        {
            View view = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8,8);
            if(i!=0) {
                params.leftMargin = 5;
            }
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.selector_dot);
            dot_layout.addView(view);
        }
    }
    //初始化监听器，当页面改变时，更新其相应数据
    private void initListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                updateIntroAndDot();
            }
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            public void onPageScrollStateChanged(int state) {
            }
        });

        /**
         * 设置
         */
        setting_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, SettingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_name",user.getUser_name());
                bundle.putString("pass_word",user.getPassword());
                bundle.putString("user_real_name",user.getUser_realname());
                bundle.putString("app_id",user.getApp_id());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        /**
         * 主页按钮
         */
       home_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    //更新数据与圆点
    private void updateIntroAndDot(){
        int currentPage = viewPager.getCurrentItem() % list.size();
        tv_intro.setText(list.get(currentPage).getIntro());
        for (int i = 0; i < dot_layout.getChildCount(); i++){
            dot_layout.getChildAt(i).setEnabled(i==currentPage);
        }
    }

    int keyBackClickCount = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            switch (keyBackClickCount++) {
                case 0:
                    showToast("再按一次退出");
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

}
