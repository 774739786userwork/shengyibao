package com.bangware.shengyibao.customer.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.net.pickpicture.ShowImageAdapter;
import java.util.ArrayList;

/**
 * 点击查看照片详细
 * @author ccssll
 *
 */
public class ShowImageActivity extends Activity {
	public static final String EXTRA_IMAGE_INDEX = "image_index";
	private ArrayList<String> list;
	private ShowImageAdapter imageAdapter;
	private ViewPager viewPager;
	private TextView number_image;
	private int pagerPosition;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//显示传递过来的照片地址
		setContentView(R.layout.show_big_image);

		viewPager = (ViewPager)findViewById(R.id.bigviewpage);
		number_image = (TextView) findViewById(R.id.imageNumber_text);
		list = getIntent().getStringArrayListExtra("ImageUrl");
		pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX,0);

		imageAdapter = new ShowImageAdapter(this,list);
		viewPager.setAdapter(imageAdapter);

		//获得图片总数
		CharSequence text = getString(R.string.viewpager_indicator, 1, viewPager
				.getAdapter().getCount());
		number_image.setText(text);
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
				number_image.setText(text);
			}

			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			public void onPageScrollStateChanged(int state) {
			}
		});

		viewPager.setCurrentItem(pagerPosition);
	}
}
