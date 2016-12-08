package com.bangware.shengyibao.main.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bangware.shengyibao.activity.ChartLibActivity;
import com.bangware.shengyibao.activity.HtmlWebActivity;
import com.bangware.shengyibao.activity.HtmlWebMainActivity;
import com.bangware.shengyibao.activity.HtmlWebThirdActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.customer.view.CustomerActivity;
import com.bangware.shengyibao.main.model.entity.ImageShow;

import java.util.ArrayList;

/**
 * 首页图片ViewPager的主体部分
 * Created by bangware on 2016/10/24.
 */
public class MyPagerAdapter extends PagerAdapter {
    private LayoutInflater mInflater;//容器，找到数据所在位置
    private ArrayList<ImageShow> list = null;
    private Context context;//上下文内置对象

    public MyPagerAdapter(Context context,ArrayList<ImageShow> list) {
        // TODO Auto-generated constructor stub
        this.context=context;
        this.list=list;
        this.mInflater = LayoutInflater.from(context);
    }

    /**
     * 返回多少page
     */
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    /**view滑动到一半时是否创建新的view
     * true:表示不去创建，使用缓存；false:去重新创建
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
    /**
     * 类似于BaseAdapter的getView方法
     */
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = View.inflate(context, R.layout.item_main_image, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        ImageShow imageShow = list.get(position%list.size());//实现viewpager图片的无线循环
        imageView.setImageResource(imageShow.getIconResId());
        switch (position%list.size()){
            case 0:
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, HtmlWebActivity.class);
                        context.startActivity(intent);
                    }
                });
                break;
            case 1:
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, HtmlWebMainActivity.class);
                        context.startActivity(intent);
                    }
                });
                break;
            case 2:
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, HtmlWebThirdActivity.class);
                        context.startActivity(intent);
                    }
                });
                break;
        }

        container.addView(view);
        return view;
    }
    /**
     * @param position:当前需要销毁第几个page
     * @param object:当前需要销毁的page
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}
