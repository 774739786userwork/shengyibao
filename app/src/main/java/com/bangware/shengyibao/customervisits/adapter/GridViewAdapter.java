package com.bangware.shengyibao.customervisits.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.config.ViewHolder;
import com.bangware.shengyibao.customer.model.entity.CustomerImage;
import com.bangware.shengyibao.customer.view.ShowImageViewActivity;
import com.bumptech.glide.Glide;

import java.util.List;


/**
 * Created by bangware on 2016/12/2.
 */

/**
 * 拜访图片展示列表
 */
public class GridViewAdapter extends BaseAdapter{
    private List<CustomerImage> list;
    private LayoutInflater inflater;
    private Context context;
    public GridViewAdapter(Context context, List<CustomerImage> data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.list = data;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.visit_image_girdview_item, null);
        }

        ImageView visit_image = ViewHolder.get(convertView,R.id.visit_img);

        visit_image.setImageResource(R.drawable.no_pic_300);
        String img_url = list.get(position).getImg_url();
        if (img_url != null && !"".equals(img_url)) {
            img_url = Model.HTTPURL+img_url;
            Glide.with(context).load(img_url).placeholder(R.drawable.no_pic_300).error(R.drawable.no_pic_300)
                    .into(visit_image);
        } else {
            Log.d("TAG", "无图片");
        }

        visit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ShowImageViewActivity.class);
                intent.putExtra("showImage", list.get(position).getImg_url());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

}

 /*   //得到要设置的GridView
    GridView gv = (GridView) parent;
    //获得每个Item之间的间隔
    int horizontalSpacing=gv.getHorizontalSpacing();
    //获得总共的列数
    int numColums=gv.getNumColumns();
    int itemWith=(gv.getWidth()-(numColums-1)*horizontalSpacing-gv.getPaddingLeft()-gv.getPaddingRight())/numColums;

    LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(itemWith, itemWith);
visit_image.setLayoutParams(p);*/
