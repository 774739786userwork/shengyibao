package com.bangware.shengyibao.customer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.customer.model.entity.Customer;
import com.bangware.shengyibao.customer.model.entity.CustomerTypes;
import com.bangware.shengyibao.returngood.model.entity.ProductsTypes;
import com.jauker.widget.BadgeView;

import java.util.List;

/**
 * 基本功能：左侧Adapter
 * 创建时间：16/4/14
 * 邮箱：w489657152@gmail.com
 */
public class MonthCustomerLeftListAdapter extends BaseAdapter {
//    private String[] leftStr; String[] leftStr
    boolean[] flagArray;
    private List<CustomerTypes> leftCustomerList;
    private Context context;

    private BadgeView quantityIcon;
    public MonthCustomerLeftListAdapter(Context context, List<CustomerTypes> leftCustomerList, boolean[] flagArray) {
        this.leftCustomerList = leftCustomerList;
        this.context = context;
        this.flagArray = flagArray;
    }

    @Override
    public int getCount() {
        return leftCustomerList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return leftCustomerList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        Holder holder = null;
        if (arg1 == null) {
            holder = new Holder();
            arg1 = LayoutInflater.from(context).inflate(R.layout.customer_left_item, null);
            holder.left_list_item = (TextView) arg1.findViewById(R.id.customer_left_list_item);
            arg1.setTag(holder);
        } else {
            holder = (Holder) arg1.getTag();
        }

        /*quantityIcon = new BadgeView(context.getApplicationContext());
        quantityIcon.setTargetView(arg1.findViewById(R.id.quantityIcon));
        quantityIcon.setBadgeGravity(Gravity.RIGHT);
        quantityIcon.setTextSize(8.5f);
        quantityIcon.setBadgeCount(112);*/

        holder.updataView(arg0);
        return arg1;
    }

    private class Holder {
        private TextView left_list_item;

        public void updataView(final int position) {
            left_list_item.setText(leftCustomerList.get(position).getGovernmentArea());
            if (flagArray[position]) {
                left_list_item.setBackgroundColor(Color.rgb(255, 255, 255));
            } else {
                left_list_item.setBackgroundColor(Color.TRANSPARENT);
            }
        }

    }
}
