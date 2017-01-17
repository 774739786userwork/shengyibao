package com.bangware.shengyibao.customervisits.view;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.customervisits.fragment.CustomerVisitsFragment;
import com.bangware.shengyibao.customervisits.fragment.RefereeFragment;
import com.bangware.shengyibao.debtowed.adapter.FragmentAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户拜访记录
 */
public class CustomerVisitRecordActivity extends FragmentActivity {
    private ViewPager mPageVp;

    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;

    /**
     * 切换的Fragment页面
     */
    private CustomerVisitsFragment mVisitFg;//客户拜访
    private RefereeFragment mRefereeFg;//推荐人
    /**
     * Tab显示内容TextView
     */
    private TextView mTabVisitTv,mTabRefereeTv;
    private TextView title_visits;
    private LinearLayout mId_tab_chat_ll,mId_tab_friend_ll;

    /**
     * Tab的那个引导线
     */
    private ImageView mTabLineIv,back_img;

    /**
     * ViewPager的当前选中页
     */
    private int currentIndex;
    /**
     * 屏幕的宽度
     */
    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_visit_record);

        findview();
        setListener();
        initTabLineWidth();
    }

    /**
     * 控件绑定
     */
    private void findview(){
        mTabVisitTv = (TextView) this.findViewById(R.id.id_customerInfo_tv);
        mTabRefereeTv = (TextView) this.findViewById(R.id.id_refereeInfo_tv);
        mTabLineIv = (ImageView) this.findViewById(R.id.id_tab_line_iv);
        back_img = (ImageView) this.findViewById(R.id.visit_record_imageview);
        mPageVp = (ViewPager) this.findViewById(R.id.id_page_vp);
        title_visits= (TextView) findViewById(R.id.title_visits);
        mId_tab_chat_ll = (LinearLayout) findViewById(R.id.id_tab_chat_ll);
        mId_tab_friend_ll = (LinearLayout) findViewById(R.id.id_tab_friend_ll);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            String time = bundle.getString("time");

            if (time.equals("day")) {
                title_visits.setText("今日拜访记录");
            }else if(time.equals("week")){
                title_visits.setText("本周拜访记录");
            }else if(time.equals("month")){
                title_visits.setText("本月拜访记录");
            }
        }
    }

    /**
     * 点击事件绑定
     */
    private void setListener(){
        //返回键点击
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mVisitFg = new CustomerVisitsFragment();
        mFragmentList.add(mVisitFg);

        mRefereeFg = new RefereeFragment();
        mFragmentList.add(mRefereeFg);

        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        mPageVp.setAdapter(mFragmentAdapter);

        mId_tab_chat_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPageVp.setCurrentItem(0);
            }
        });

        mId_tab_friend_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPageVp.setCurrentItem(1);
            }
        });
        scrollItem();
    }

    private void scrollItem(){
        //监听viewpager触屏滑动改变事件
        mPageVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }

            /**
             * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
             * offsetPixels:当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float offset,
                                       int offsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                        .getLayoutParams();

                Log.e("offset:", offset + "");
                /**
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
                 * 设置mTabLineIv的左边距 滑动场景：
                 * 记3个页面,
                 * 从左到右分别为0,1,2
                 * 0->1; 1->2; 2->1; 1->0
                 */

                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 2) + currentIndex
                            * (screenWidth / 2));

                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 2) + currentIndex
                            * (screenWidth / 2));

                } else if (currentIndex == 1 && position == 1) // 1->2
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 2) + currentIndex
                            * (screenWidth / 2));
                } else if (currentIndex == 2 && position == 1) // 2->1
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 2) + currentIndex
                            * (screenWidth / 2));
                }
                mTabLineIv.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                resetTextView();
                switch (position) {
                    case 0:
                        mTabVisitTv.setTextColor(Color.GRAY);
                        break;
                    case 1:
                        mTabRefereeTv.setTextColor(Color.GRAY);
                        break;
                    default:
                        break;
                }
                currentIndex = position;
            }
        });
    }

    /**
     * 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                .getLayoutParams();
        lp.width = screenWidth / 2;
        mTabLineIv.setLayoutParams(lp);
    }

    /**
     * 重置颜色
     */
    private void resetTextView() {
        mTabVisitTv.setTextColor(Color.BLACK);
        mTabRefereeTv.setTextColor(Color.BLACK);
    }
}
