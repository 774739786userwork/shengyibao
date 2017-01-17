package com.bangware.shengyibao.utils.OnScrollGridView;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.TextView;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.customervisits.model.entity.VisitRecordBean;

/**
 * 案例图片展示
 */
public class CasesImageActivity extends BaseActivity {
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    private VisitRecordBean visitRecordBean;
    private ViewPager viewPager;
    private int pagerPosition;
    private TextView number_text;
    private CasesImageAdapter casesImageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cases_image);

        viewPager = (ViewPager)findViewById(R.id.cases_viewpage);
        number_text = (TextView) findViewById(R.id.cases_Number_text);
        visitRecordBean = (VisitRecordBean) getIntent().getSerializableExtra("showImage");
        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX,0);

        casesImageAdapter = new CasesImageAdapter(this,visitRecordBean.getCustomer().getImages());
        viewPager.setAdapter(casesImageAdapter);
        //获得图片总数
        CharSequence text = getString(R.string.viewpager_indicator, 1, viewPager
                .getAdapter().getCount());
        number_text.setText(text);
        initListener();
    }

    //初始化监听器，当页面改变时，更新其相应数据
    private void initListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //切换图片时更新图片数的下标
                CharSequence text = getString(R.string.viewpager_indicator,
                        position + 1, viewPager.getAdapter().getCount());
                number_text.setText(text);
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            public void onPageScrollStateChanged(int state) {
            }
        });

        viewPager.setCurrentItem(pagerPosition);
    }
}
