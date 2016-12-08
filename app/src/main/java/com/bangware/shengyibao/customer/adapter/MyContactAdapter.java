package com.bangware.shengyibao.customer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.config.ViewHolder;
import com.bangware.shengyibao.customer.model.entity.Customer;
import com.bangware.shengyibao.customer.view.CustomerActivity;
import com.bangware.shengyibao.utils.NumberUtils;
import com.bumptech.glide.Glide;

import java.util.List;


/**
 * 客户列表适配器
 * 
 * @author ccssll implements SectionIndexer
 */
public class MyContactAdapter extends BaseAdapter {
	private Context context;// 上下文内置对象
	private LayoutInflater mInflater;// 容器，找到数据所在位置
	private List<String> mycontactList = null;
	// 上下文对象
	public MyContactAdapter(Context context,
							List<String> mycontactList) {
		this.mycontactList = mycontactList;
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
	}

	public void refreshData(List<String> mycontactList) {
		this.mycontactList = mycontactList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// 适配器中所代表的数据集中的条目数
		return mycontactList.size();
	}

	@Override
	public Object getItem(int position) {
		// 获取数据集中与指定索引对应的数据项
		return mycontactList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// 取在列表中与指定索引对应的行id
		Log.d("TAG", "取在列表中与指定索引对应的行id==>" + position);
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 此方法是对listview的优化，节省资源，提升性能
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_mycontact, null);
		}
        TextView my_contact_item=ViewHolder.get(convertView,R.id.my_contact_item);
		my_contact_item.setText(mycontactList.get(position));
		convertView.setBackgroundResource(R.drawable.my_tab_background);// 设置listview点击的背景色
		return convertView;
	}
	
}
