package com.bangware.shengyibao.net.pickpicture;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bangware.shengyibao.activity.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by bangware on 2017/1/5.
 */

public class ShowImageAdapter extends PagerAdapter {
    private LayoutInflater mInflater;//容器，找到数据所在位置
    private ArrayList<String> list = null;
    private Context context;//上下文内置对象

    public ShowImageAdapter(Context context,ArrayList<String> list) {
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
        return list.size();
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

        String img_url = "file://" + list.get(position);
        Glide.with(context).load(img_url).placeholder(R.drawable.no_pic_300).error(R.drawable.no_pic_300)
                .into(imageView);
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
