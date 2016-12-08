package com.bangware.shengyibao.main.view;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.activity.SettingActivity;
import com.bangware.shengyibao.main.model.entity.ImageShow;
import com.bangware.shengyibao.net.ConnectionChangeReceiver;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.HomeListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private Fragment mContent;
    private ConnectionChangeReceiver myReceiver;//网络监听广播
    private HomeListener homeListener = null;//Home键监听广播
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
        String app_id="0";
        if (app_id.equals("0") ) {
            mContent = new FragmentSaler();
        }else if(app_id.equals("1")) {
            mContent = new FragmentTwoSaler();
        }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //FIXME modify by mouse

            if (getSupportFragmentManager().findFragmentById(R.id.main_FrameLayout) != null) {
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
    }

    //初始化数据
    private void initData(){
        registerReceiver();

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
                bundle.putString("user_name",AppContext.getInstance().getUser().getUser_name());
                bundle.putString("pass_word",AppContext.getInstance().getUser().getPassword());
                bundle.putString("user_real_name",AppContext.getInstance().getUser().getUser_realname());
                bundle.putString("app_id",AppContext.getInstance().getUser().getApp_id());
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

    /**广播注册*/
    private  void registerReceiver(){
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myReceiver=new ConnectionChangeReceiver();
        this.registerReceiver(myReceiver, filter);
    }

    /**注销广播服务*/
    private  void unregisterReceiver(){
        this.unregisterReceiver(myReceiver);

    }

    @Override
    protected void onResume( ) {
        super.onResume();
//        homeListener.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        homeListener.stop();
    }

    private void initHomeListen(){
        homeListener = new HomeListener(this);
        homeListener.setOnHomeBtnPressListener( new HomeListener.OnHomeBtnPressLitener( ) {
            @Override
            public void onHomeBtnPress() {
                showToast( "按下Home按键！" );
            }

            @Override
            public void onHomeBtnLongPress() {
                showToast( "长按Home按键！" );
            }
        });
    }

    private void showToast( String toastInfoStr ){
        Toast.makeText( this, toastInfoStr, Toast.LENGTH_LONG ).show( );
    }

    @Override
    protected void onDestroy() {
        if (myReceiver != null){
            unregisterReceiver();
        }
        super.onDestroy();
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
