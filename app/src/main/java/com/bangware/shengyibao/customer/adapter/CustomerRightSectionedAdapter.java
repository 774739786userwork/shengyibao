package com.bangware.shengyibao.customer.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.customer.model.entity.Customer;
import com.bangware.shengyibao.customer.model.entity.CustomerTypes;
import com.bangware.shengyibao.model.Product;
import com.bangware.shengyibao.returngood.adapter.SectionedBaseAdapter;
import com.bangware.shengyibao.returngood.model.entity.ProductsTypes;
import com.bangware.shengyibao.utils.volley.ImageRequester;

import java.util.List;

/**
 * 基本功能：右侧Adapter
 */
public class CustomerRightSectionedAdapter extends SectionedBaseAdapter {

    private Context mContext;
    private List<CustomerTypes> leftStr;
    private Customer[][] rightArray;
    private ImageRequester mImageRequester;
    public CustomerRightSectionedAdapter(Context context, List<CustomerTypes> leftStr, Customer[][] rightStr) {
        this.mContext = context;
        this.leftStr = leftStr;
        this.rightArray = rightStr;
        this.mImageRequester = new ImageRequester(this.mContext, true);
    }

   private class Holder
    {
        public ImageView imageItem;
        public TextView mainCustomerBillingNameTextView;
        public TextView mainCustomerContactName_textview;
        public TextView mainCustomerBillingItemPhone;
        public TextView mMainCustomerBillingTimeTextView;
        public TextView CustomerBillingCountTextView;
    }
    @Override
    public Object getItem(int section, int position) {
        return rightArray[section][position];
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return leftStr.size();
    }

    @Override
    public int getCountForSection(int section) {
        if(rightArray!=null && section<=rightArray.length) {
            return rightArray[section].length;
        }
        return 0;
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView==null)
        {
            holder=new Holder();
            convertView=View.inflate(mContext,R.layout.item_mainmonth_billing_customer,null);
            holder.imageItem= (ImageView) convertView.findViewById(R.id.mainCustomerBillingItemImage);
            holder.CustomerBillingCountTextView= (TextView) convertView.findViewById(R.id.CustomerBillingCountTextView);
            holder.mainCustomerBillingNameTextView= (TextView) convertView.findViewById(R.id.mainCustomerBillingNameTextView);
            holder.mainCustomerContactName_textview= (TextView) convertView.findViewById(R.id.mainCustomerContactName_textview);
            holder.mMainCustomerBillingTimeTextView= (TextView) convertView.findViewById(R.id.mainCustomerBillingTimeTextView);
            holder.mainCustomerBillingItemPhone= (TextView) convertView.findViewById(R.id.mainCustomerBillingItemPhone);
            convertView.setTag(holder);
        }else
        {
            holder= (Holder) convertView.getTag();
        }
        holder.mainCustomerBillingNameTextView.setText(rightArray[section][position].getName().toString());
        holder.CustomerBillingCountTextView.setText("进货金额"+rightArray[section][position].getPurchase_total_sum());
        if (rightArray[section][position].getContacts().size()>0) {
            holder.mainCustomerContactName_textview.setText(rightArray[section][position].getContacts().get(0).getName().trim());
            holder.mainCustomerBillingItemPhone.setText(rightArray[section][position].getContacts().get(0).getMobile1().trim());
        }
        holder.mMainCustomerBillingTimeTextView.setText(rightArray[section][position].getLasttime());
        if(!holder.mMainCustomerBillingTimeTextView.getText().toString().isEmpty() && !holder.mMainCustomerBillingTimeTextView.getText().toString().equals("null")){
            holder.mMainCustomerBillingTimeTextView.setText("上次进货日期："+rightArray[section][position].getLasttime());
        }else{
            holder.mMainCustomerBillingTimeTextView.setText("暂无进货");
        }
        holder.imageItem.setImageResource(R.drawable.no_pic_300);
        if (rightArray[section][position].getImages().size()>0) {
            String ImageUrl = rightArray[section][position].getImages().get(0).getImg_url();
            if (ImageUrl != null && !"".equals(ImageUrl)) {
                ImageUrl = Model.HTTPURL+ImageUrl;
                mImageRequester.load(holder.imageItem, ImageUrl, R.drawable.shop_photo_frame, R.drawable.shop_photo_frame);
            }else{
                Log.e("MainSectionedAdapter",rightArray[section][position].getName()+"图片缺失");
            }
        }
        convertView.setBackgroundResource(R.drawable.my_tab_background);
        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.header_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        layout.setClickable(false);
        ((TextView) layout.findViewById(R.id.textItem)).setText(leftStr.get(section).getGovernmentArea());
        return layout;
    }

}
