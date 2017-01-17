package com.bangware.shengyibao.utils.OnScrollGridView;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.customer.model.entity.CustomerImage;
import com.bumptech.glide.Glide;
import java.util.List;

/**
 * 案例图片展示的数据源
 * Created by bangware on 2017/1/9.
 */

public class CasesImageAdapter extends PagerAdapter {
    private LayoutInflater mInflater;//容器，找到数据所在位置
    private List<CustomerImage> imageList;
    private Context mContext;
    public CasesImageAdapter(Context context,List<CustomerImage> customerImages) {
        // TODO Auto-generated constructor stub
        this.mContext=context;
        this.imageList=customerImages;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    /**
     * 类似于BaseAdapter的getView方法
     */
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = View.inflate(mContext, R.layout.item_main_image, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);

        String img_url = Model.HTTPURL + imageList.get(position).getImg_url();
        Glide.with(mContext).load(img_url).placeholder(R.drawable.no_pic_300).error(R.drawable.no_pic_300)
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
        container.removeView((View) object);
    }
}
