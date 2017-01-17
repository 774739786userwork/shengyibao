package com.bangware.shengyibao.ladingbilling.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.ViewHolder;
import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenBean;
import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenGoods;
import com.bangware.shengyibao.ladingbilling.presenter.StockPresenter;
import com.bangware.shengyibao.model.Product;

import java.util.List;

/**
 * 余货数据源
 * Created by bangware on 2016/8/22.
 */
public class StockQueryAdapter extends BaseAdapter{
    private List<Product> products_stocklist;
    private Context context;
    private DisburdenBean bean;
    private LayoutInflater inflater;

    public StockQueryAdapter(Context context,List<Product> products_stocklist,StockPresenter presenter) {
        // TODO Auto-generated constructor stub
        super();
        this.bean=presenter.getDisburdenBean();
        this.context=context;
        this.products_stocklist=products_stocklist;
    }
    @Override
    public int getCount() {
        return products_stocklist.size();
    }

    @Override
    public Object getItem(int position) {
        return products_stocklist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item_stock_query, null);
        }
        TextView product_name = ViewHolder.get(convertView, R.id.productName_textview);
        TextView product_specifications = ViewHolder.get(convertView, R.id.specifications_textview);
        TextView product_stock = ViewHolder.get(convertView, R.id.stock_count_textview);
        LinearLayout Stock_Lin=ViewHolder.get(convertView,R.id.Stock_Lin);
        LinearLayout Lin_disburden=ViewHolder.get(convertView,R.id.Lin_disburden);
        TextView disburden=ViewHolder.get(convertView,R.id.disburden);
        TextView surplus=ViewHolder.get(convertView,R.id.surplus);
         Product product=products_stocklist.get(position);
        if (products_stocklist.get(position).getStock() > 0){
            product_name.setText(product.getName());
            product_specifications.setText(product.getSpecifications());
            product_stock.setText(String.valueOf(product.getStock())+product.getUnit());
        }else {
            Stock_Lin.setBackgroundColor(Color.LTGRAY);
            product_name.setText(product.getName());
            product_specifications.setText(product.getSpecifications());
            product_stock.setText(String.valueOf(product.getStock())+product.getUnit());
        }
        Log.e("DisburdenGoods",""+bean.getGoods(product.getId()));

        DisburdenGoods goods = bean.getGoods(product.getId());


        if (goods!=null)
        {
            Lin_disburden.setVisibility(View.VISIBLE);
           disburden.setText(String.valueOf(goods.getDisburden())+products_stocklist.get(position).getUnit());
            String p=String.valueOf(goods.getDisburden());
            Log.e("sdadsada",String.valueOf(goods.getDisburden())+String.valueOf(goods.getSurplus()));
            surplus.setText(String.valueOf(goods.getSurplus())+products_stocklist.get(position).getUnit());
        }else
        {
            Lin_disburden.setVisibility(View.GONE);
        }
        return convertView;
    }
}
