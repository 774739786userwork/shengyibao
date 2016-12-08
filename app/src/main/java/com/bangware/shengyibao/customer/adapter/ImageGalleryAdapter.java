package com.bangware.shengyibao.customer.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.customer.model.entity.CustomerImage;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 图片画廊列表数据适配
 */
public class ImageGalleryAdapter extends BaseAdapter{
    private List<CustomerImage> imageList;
    private Context mContext;
    //Item的修饰背景
    int mGalleryItemBackground;

    public ImageGalleryAdapter(List<CustomerImage> imageList,Context c){
        this.mContext = c;
        this.imageList = imageList;
        //读取styleable资源
        TypedArray a = mContext.obtainStyledAttributes(R.styleable.HelloGallery);
        mGalleryItemBackground = a.getResourceId(
                R.styleable.HelloGallery_android_galleryItemBackground, 0);
        a.recycle();
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ImageView iv = new ImageView(mContext);
        //给生成的ImageView设置Id，不设置的话Id都是-1
        CustomerImage customerImage = imageList.get(position);
        iv.setId(position);
        String img_url = customerImage.getImg_url();
        iv.setImageResource(R.drawable.no_pic_300);
        if (img_url != null && !"".equals(img_url)) {
            img_url = Model.HTTPURL+img_url;
            //谷歌推荐的图片加载库  用于显示图片
            Glide.with(mContext).load(img_url).placeholder(R.drawable.no_pic_300).error(R.drawable.no_pic_300)
                    .into(iv);
        } else {
            Log.e("TAG", "无图片");
        }
        iv.setLayoutParams(new Gallery.LayoutParams(120, 160));
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        iv.setBackgroundResource(mGalleryItemBackground);
        return iv;
    }
}
