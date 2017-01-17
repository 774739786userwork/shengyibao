package com.bangware.shengyibao.utils.OnScrollGridView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
/**
 * 自定义gridView，重写onMeasure方法控制gridview的高度与宽度
 */
public class NoScrollGridView extends GridView {

	public NoScrollGridView(Context context) {
		super(context);
	}

	public NoScrollGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	/**
	 * MeasureSpec.AT_MOST表示的就是子控件可根据自己的实际需要自适应大小
	 * Integer.MAX_VALUE >> 2表示父布局给的参考的大小无限大，也就是无边界的意思
	 * @param widthMeasureSpec
	 * @param heightMeasureSpec
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
