package com.bangware.shengyibao.customervisits.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.ViewHolder;
import com.bangware.shengyibao.customervisits.model.entity.VisitsCountBean;

import java.util.List;
import java.util.Map;

/**
 * Created by bangware on 2016/12/20.
 */

public class VisitCountAdapter extends BaseAdapter{
    private Context context;// 上下文内置对象
    private LayoutInflater mInflater;// 容器，找到数据所在位置
    private List<VisitsCountBean> list;
    public VisitCountAdapter(Context context,List<VisitsCountBean> list){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_visitcount_list, null);
        }
        TextView salerPerson = ViewHolder.get(convertView,R.id.saler_person_textview);
        TextView visitCount = ViewHolder.get(convertView,R.id.visit_count_textview);

        TextView billingCount = ViewHolder.get(convertView,R.id.billing_count_textview);
        TextView visitBillingCount = ViewHolder.get(convertView,R.id.visit_billing_count_textview);

        TextView billingPercentage = ViewHolder.get(convertView,R.id.billing_percentage_textview);
        TextView visitBillingPercentage = ViewHolder.get(convertView,R.id.visit_biiling_percentage_textview);

        TextView totalVisitCount = ViewHolder.get(convertView,R.id.total_visit_count_textview);
        TextView totalBillingCount = ViewHolder.get(convertView,R.id.total_billing_count_textview);

        salerPerson.setText(list.get(position).getEmploye_name());
        visitCount.setText(String.valueOf(list.get(position).getVisits_Num()));
        billingCount.setText(String.valueOf(list.get(position).getOrder_Num()));
        visitBillingCount.setText(String.valueOf(list.get(position).getVisits_Order_Num()));
        billingPercentage.setText(list.get(position).getOrder_Precent());
        visitBillingPercentage.setText(list.get(position).getVisits_Order_Precent());
        totalVisitCount.setText(String.valueOf(list.get(position).getCOUNT_VISITS_NUM()));
        totalBillingCount.setText(String.valueOf(list.get(position).getCount_Order_Num()));

        convertView.setBackgroundResource(R.drawable.my_tab_background);// 设置listview点击的背景色
        return convertView;
    }
}
