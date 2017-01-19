package com.bangware.shengyibao.ladingbilling.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bangware.shengyibao.ladingbilling.model.entity.QueryDisburdenBean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */

public class QueryDisburdenAdapter extends BaseAdapter {
    private List<QueryDisburdenBean> list;
    private Context mContext;
    private QueryDisburdenBean disburdenBean;

    public QueryDisburdenAdapter(List<QueryDisburdenBean> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
//        inflater.inflate()
        return convertView;
    }
}
