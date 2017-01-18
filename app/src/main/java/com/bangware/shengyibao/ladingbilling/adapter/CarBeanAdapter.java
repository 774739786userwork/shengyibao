package com.bangware.shengyibao.ladingbilling.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.ViewHolder;
import com.bangware.shengyibao.ladingbilling.model.entity.CarBean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/17.
 */

public class CarBeanAdapter extends BaseAdapter {
    private List<CarBean> list;
    private Context context;
        private LayoutInflater inflater;

        public CarBeanAdapter(Context context,List<CarBean> carBeanList) {
            // TODO Auto-generated constructor stub
            super();
            this.context=context;
            this.list=carBeanList;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
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
                inflater = LayoutInflater.from(parent.getContext());
                convertView = inflater.inflate(R.layout.item_query_carbean, null);
            }

            TextView query_car_number = ViewHolder.get(convertView, R.id.query_car_number);
            query_car_number.setText(list.get(position).getCar_Number().toString());
            return convertView;
        }

    }
