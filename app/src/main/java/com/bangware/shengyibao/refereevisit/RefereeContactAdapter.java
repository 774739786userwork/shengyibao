package com.bangware.shengyibao.refereevisit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.ViewHolder;
import com.bangware.shengyibao.customer.model.entity.Contacts;
import com.bangware.shengyibao.refereevisit.model.entity.RefereeVisitor;

import java.util.List;

/**
 * 客户联系人适配器
 * @author zcb
 *
 */
public class RefereeContactAdapter extends BaseAdapter{
	private Context context;// 上下文内置对象
	private LayoutInflater mInflater;// 容器，找到数据所在位置
	private List<RefereeVisitor> contactsList = null;

	public RefereeContactAdapter(Context context,
								 List<RefereeVisitor> contactsList) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.contactsList = contactsList;
	}
	public void refreshData(List<RefereeVisitor> contactsList) {
		this.contactsList = contactsList;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contactsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		// 获取数据集中与指定索引对应的数据项
		if (contactsList.size() == 0 || contactsList == null) {
			return 0;
		}
		return contactsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		 return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_referee_contacts, null);
		}
		TextView con_name = ViewHolder.get(convertView, R.id.Referee_Con_name);
		TextView con_phone=ViewHolder.get(convertView, R.id.Referee_Phone);
		ImageView con_must=ViewHolder.get(convertView, R.id.Referee_contact_must);

		    con_phone.setText(contactsList.get(position).getMobile());
	    	con_name.setText(contactsList.get(position).getName());
	    	con_must.setImageResource(R.drawable.custom_must);
		convertView.setBackgroundResource(R.drawable.my_tab_background);// 设置listview点击的背景色
		return convertView;
	}
}
