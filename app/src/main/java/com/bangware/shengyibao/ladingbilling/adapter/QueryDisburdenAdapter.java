package com.bangware.shengyibao.ladingbilling.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.ViewHolder;
import com.bangware.shengyibao.ladingbilling.model.entity.QueryDisburdenBean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */

public class QueryDisburdenAdapter extends BaseAdapter {
    private List<QueryDisburdenBean> list;
    private Context mContext;
    private QueryDisburdenBean disburdenBean;
    private ReturnBack mCallback;
    public interface ReturnBack
    {
       void setLisenter(int p,View view,int v_id,String disburden_id);
    }

    public QueryDisburdenAdapter(List<QueryDisburdenBean> list, Context mContext,ReturnBack returnBack) {
        this.list = list;
        this.mContext = mContext;
        this.mCallback=returnBack;
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
        if (convertView==null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
           convertView=inflater.inflate(R.layout.item_disburden_query, null);
        }
        TextView disburden_time= ViewHolder.get(convertView,R.id.disburden_time);
        TextView disburden_serial_number=ViewHolder.get(convertView,R.id.disburden_serial_number);
        TextView disburden_product=ViewHolder.get(convertView,R.id.disburden_product);
        TextView disburden_carnumber=ViewHolder.get(convertView,R.id.disburden_carnumber);
        TextView disburden_remove=ViewHolder.get(convertView,R.id.disburden_remove);
        TextView creat_person=ViewHolder.get(convertView,R.id.creat_person);

        disburden_time.setText(list.get(position).getDisburn_time().toString());
        disburden_carnumber.setText(list.get(position).getCarBean().getCar_Number().toString());
        disburden_product.setText(list.get(position).getProduct_name().toString());
        disburden_serial_number.setText(list.get(position).getDisburn_numer().toString());
        creat_person.setText(list.get(position).getCreate_user_name());
        final String disburden_id=list.get(position).getDisburn_id();
        final int pos = position;
        final View view = convertView;
        final int v_id = disburden_remove.getId();
        disburden_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.setLisenter(pos,view,v_id,disburden_id);
            }
        });
        return convertView;
    }
}
