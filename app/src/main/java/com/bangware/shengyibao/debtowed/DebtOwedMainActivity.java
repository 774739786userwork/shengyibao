package com.bangware.shengyibao.debtowed;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.debtowed.adapter.FragmentAdapter;
import com.bangware.shengyibao.debtowed.fragment.DebtFragment;
import com.bangware.shengyibao.debtowed.fragment.OwedFragment;

import java.util.ArrayList;
import java.util.List;

public class DebtOwedMainActivity extends FragmentActivity {
    private ViewPager mPageVp;

    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;

    /**
     * 切换的Fragment页面
     */
    private DebtFragment mDebtFg;//欠款
    private OwedFragment mOwedFg;//欠货
    /**
     * Tab显示内容TextView
     */
    private TextView mTabOwedTv,mTabDebtTv;
    /**
     * Tab的那个引导线
     */
    private ImageView mTabLineIv,debtowed_backImg;

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
        setContentView(R.layout.activity_debt_owed_main);

        findview();
        setListener();
        initTabLineWidth();
    }

    /**
     * 控件绑定
     */
    private void findview(){
        mTabOwedTv = (TextView) this.findViewById(R.id.id_owed_tv);
        mTabDebtTv = (TextView) this.findViewById(R.id.id_debt_tv);
        mTabLineIv = (ImageView) this.findViewById(R.id.id_tab_line_iv);
        debtowed_backImg = (ImageView) this.findViewById(R.id.debtowed_backImg);
        mPageVp = (ViewPager) this.findViewById(R.id.id_page_vp);
    }

    /**
     * 点击事件绑定
     */
    private void setListener(){
        //返回键点击
        debtowed_backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mDebtFg = new DebtFragment();
        mOwedFg = new OwedFragment();
        mFragmentList.add(mDebtFg);
        mFragmentList.add(mOwedFg);

        mFragmentAdapter = new FragmentAdapter(
                this.getSupportFragmentManager(), mFragmentList);
        mPageVp.setAdapter(mFragmentAdapter);
        mPageVp.setCurrentItem(0);

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
                        mTabOwedTv.setTextColor(Color.GRAY);
                        break;
                    case 1:
                        mTabDebtTv.setTextColor(Color.GRAY);
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
        mTabOwedTv.setTextColor(Color.BLACK);
        mTabDebtTv.setTextColor(Color.BLACK);
    }

}
