package com.bangware.shengyibao.deliverynote.adapter;

import java.util.List;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bangware.shengyibao.deliverynote.model.entity.DeliveryNote;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.ViewHolder;
import com.bangware.shengyibao.user.model.entity.User;

/**
 * 送货单查询适配器
 * @author ccssll
 *
 */
public class DeliveryNoteQueryAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;// 容器，找到数据所在位置
	List<DeliveryNote> querylist;
	private User user;
	
	public DeliveryNoteQueryAdapter(Context context, List<DeliveryNote> querylist, User user) {
		// TODO Auto-generated constructor stub
		super();
		this.context = context;
		this.querylist = querylist;
		this.user=user;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return querylist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (querylist.size() == 0 || querylist == null) {
			return 0;
		}
		return querylist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			mInflater = LayoutInflater.from(parent.getContext());
			convertView = mInflater.inflate(R.layout.item_query_deliverynote, null);
		}
		TextView shop_name = ViewHolder.get(convertView,R.id.shop_name);
		TextView total_money = ViewHolder.get(convertView, R.id.total_money);
		TextView serial_number = ViewHolder.get(convertView,R.id.delivery_serial_number);
		TextView show_time = ViewHolder.get(convertView,R.id.show_time);
		TextView unpaid = ViewHolder.get(convertView,R.id.unPaid);
		TextView delivery_detail=ViewHolder.get(convertView,R.id.deliverynote_detail);
		TextView delivery_condition=ViewHolder.get(convertView,R.id.delivery_condition);
		TextView remember_name=ViewHolder.get(convertView,R.id.remember_name);

		DeliveryNote deliveryNote = querylist.get(position);
		
		String timeStr = deliveryNote.getDelivery_date();
		String serialNumberStr = deliveryNote.getSerial_number();
		String nameStr = deliveryNote.getCustomer().getName();
		String product_detail=deliveryNote.getDeliveryNote_product();
		String rem_name=deliveryNote.getRemember_employee_name();
		String rem_id=deliveryNote.getRemember_employee_id();
		double moneyStr = deliveryNote.getTotalAmount();
		double unpaidStr = deliveryNote.getUnpaidAmount();

		if (deliveryNote.getFlag()==3)
		{
			delivery_condition.setVisibility(View.VISIBLE);
		}else
		{
			delivery_condition.setVisibility(View.GONE);
		}
		int p = position + 1;
		show_time.setText(timeStr);
		serial_number.setText(String.valueOf(p));
		shop_name.setText(nameStr);
		if (rem_id.equals(user.getEmployee_id()))
		{
			remember_name.setVisibility(View.GONE);
		}
		remember_name.setText("记:"+rem_name);
		delivery_detail.setText(product_detail);
		total_money.setText("总计金额：¥"+moneyStr+"元");
		unpaid.setText("未付：¥"+unpaidStr+"元");
		convertView.setBackgroundResource(R.drawable.my_tab_background);// 设置listview点击的背景色
		return convertView;
	}

}
