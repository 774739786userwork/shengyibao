package com.bangware.shengyibao.utils.OnScrollGridView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.customer.model.entity.CustomerImage;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class MyGridAdapter extends BaseAdapter {
//	private String[] files;
    private List<CustomerImage> imageList;
	private LayoutInflater mLayoutInflater;

	public MyGridAdapter( List<CustomerImage> imageList, Context context) {
		this.imageList = imageList;
		mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return imageList == null ? 0 : imageList.size();
	}

	@Override
	public CustomerImage getItem(int position) {
		return imageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyGridViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new MyGridViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.gridview_item,
					parent, false);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.album_image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (MyGridViewHolder) convertView.getTag();
		}
		String url =  Model.HTTPURL+getItem(position).getImg_url();
		ImageLoader.getInstance().displayImage(url, viewHolder.imageView);

		return convertView;
	}

	private static class MyGridViewHolder {
		ImageView imageView;
	}
}
