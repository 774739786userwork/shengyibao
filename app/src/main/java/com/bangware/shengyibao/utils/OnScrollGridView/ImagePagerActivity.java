package com.bangware.shengyibao.utils.OnScrollGridView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.widget.TextView;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.customer.model.entity.CustomerImage;
import com.bangware.shengyibao.practicalprojects.model.entity.MyBean;
import com.bangware.shengyibao.utils.CommonUtil;

import java.util.List;

public class ImagePagerActivity extends FragmentActivity {
	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index";
	public static final String EXTRA_IMAGE_URLS = "image_urls";

	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator;

	private MyBean myBean;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_detail_pager);

		pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
//		String[] urls = getIntent().getStringArrayExtra(EXTRA_IMAGE_URLS);
		myBean = (MyBean) getIntent().getSerializableExtra(EXTRA_IMAGE_URLS);

		mPager = (HackyViewPager) findViewById(R.id.pager);
		ImagePagerAdapter mAdapter = new ImagePagerAdapter(
				getSupportFragmentManager(), myBean.getImages());
		mPager.setAdapter(mAdapter);
		indicator = (TextView) findViewById(R.id.indicator);

		CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
				.getAdapter().getCount());
		indicator.setText(text);
		// 更新下标
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				CharSequence text = getString(R.string.viewpager_indicator,
						arg0 + 1, mPager.getAdapter().getCount());
				indicator.setText(text);
			}

		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		mPager.setCurrentItem(pagerPosition);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

//		public String[] fileList;
		private List<CustomerImage> imageList;

		public ImagePagerAdapter(FragmentManager fm, List<CustomerImage> imageList) {
			super(fm);
			this.imageList = imageList;
		}

		@Override
		public int getCount() {
			return imageList == null ? 0 : imageList.size();
		}

		@Override
		public Fragment getItem(int position) {
			String url = Model.HTTPURL + imageList.get(position).getImg_url();
			return ImageDetailFragment.newInstance(url);
		}
	}
}