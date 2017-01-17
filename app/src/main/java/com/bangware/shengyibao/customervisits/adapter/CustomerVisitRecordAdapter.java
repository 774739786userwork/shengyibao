package com.bangware.shengyibao.customervisits.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.ViewHolder;
import com.bangware.shengyibao.customervisits.model.entity.OwnerBean;
import com.bangware.shengyibao.customervisits.model.entity.VisitRecordBean;
import com.bangware.shengyibao.utils.OnScrollGridView.CasesImageActivity;
import com.bangware.shengyibao.utils.OnScrollGridView.NoScrollGridView;

import java.io.Serializable;
import java.util.List;


/**
 * 客户拜访记录数据源适配
 * Created by bangware on 2016/11/26.
 */

public class CustomerVisitRecordAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private Context context;
    private List<VisitRecordBean> list;
    private Callback mCallback;
    /**
     * 自定义接口，用于回调语音播放触摸事件到Activity
     */
     public interface Callback {
        boolean onTouchEvent(int pos,View v,MotionEvent motionEvent,int which);
     }

    public  CustomerVisitRecordAdapter(Context context,List<VisitRecordBean> list,Callback mCallback){
        this.context = context;
        this.list = list;
        this.mCallback = mCallback;
        this.inflater = LayoutInflater.from(context);
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_visit_record_list, null);
        }

        /**客户信息UI**/
        TextView visit_time = ViewHolder.get(convertView,R.id.visit_time_textview);
        TextView visit_content = ViewHolder.get(convertView,R.id.visit_content_textview);
        LinearLayout sound_file = ViewHolder.get(convertView,R.id.sound_record_file);
        TextView visit_voice = ViewHolder.get(convertView,R.id.text_pictureReplace);
        TextView customer_level = ViewHolder.get(convertView,R.id.customer_level_textview);
        TextView voice_time = ViewHolder.get(convertView,R.id.recorded_time_textview);
        TextView visit_customer = ViewHolder.get(convertView,R.id.visit_customer_textview);
        TextView visit_type = ViewHolder.get(convertView,R.id.visit_type_textview);
        TextView visit_address = ViewHolder.get(convertView,R.id.visit_customerAddress_textview);
        TextView customer_name=ViewHolder.get(convertView,R.id.visit_customer_name);
        TextView customer_phone=ViewHolder.get(convertView,R.id.visit_customer_phone);

        /**业主信息UI**/
        LinearLayout ownerInfo_linearLayout = ViewHolder.get(convertView,R.id.owner_info_linearLayout);
        TextView owner_name = ViewHolder.get(convertView,R.id.owner_name_textview);
        TextView owner_mobile = ViewHolder.get(convertView,R.id.owner_mobile_textview);
        TextView owner_acreage = ViewHolder.get(convertView,R.id.owner_acreage_textview);
        TextView owner_price = ViewHolder.get(convertView,R.id.owner_price_textview);

        /**拜访图片信息UI**/
        NoScrollGridView gridView = ViewHolder.get(convertView,R.id.image_gridview);

        visit_time.setText(list.get(position).getVisitTime());
        visit_type.setText("("+list.get(position).getVisitType()+"),("+list.get(position).getIssueType()+")");
        customer_level.setText("("+list.get(position).getCustomerLevel()+")");
        visit_customer.setText(list.get(position).getCustomer().getName());
        visit_address.setText(list.get(position).getCustomer().getAddress());
            customer_name.setText(list.get(position).getCustomer().getContacts().get(0).getName());
            customer_phone.setText(list.get(position).getCustomer().getContacts().get(0).getMobile1());
        /**判断是否是业主**/
        OwnerBean ownerBean = list.get(position).getOwnerBean();
        if (ownerBean.is_owner() == true) {
            ownerInfo_linearLayout.setVisibility(View.VISIBLE);
            owner_name.setText(ownerBean.getOwnerName());
            owner_mobile.setText(ownerBean.getOwnerMobile());

            if (ownerBean.getAcreage() != 0.0 && ownerBean.getUnitPrice() != 0.0){
                owner_acreage.setVisibility(View.VISIBLE);
                owner_price.setVisibility(View.VISIBLE);
                owner_acreage.setText(Double.valueOf(ownerBean.getAcreage()).toString()+"/m²");
                owner_price.setText(Double.valueOf(ownerBean.getUnitPrice()).toString()+"元");
            }else {
                owner_acreage.setVisibility(View.GONE);
                owner_price.setVisibility(View.GONE);
            }

        }else {
            ownerInfo_linearLayout.setVisibility(View.GONE);
        }

        /**判断语音与文本内容的显示隐藏**/
        String content_type = list.get(position).getContentType();
        if (content_type.equals("0")){
            visit_content.setText(list.get(position).getVisitContent());
            sound_file.setVisibility(View.GONE);
            visit_content.setVisibility(View.VISIBLE);
        }else if (content_type.equals("1")){
            visit_content.setVisibility(View.GONE);
            sound_file.setVisibility(View.VISIBLE);
            voice_time.setText(list.get(position).getVisitVoiceTime());
            /**
             * 设置语音按钮触摸事件
             */
            final int pos = position;
            final View v = convertView;
            final int v_id = visit_voice.getId();
            visit_voice.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    mCallback.onTouchEvent(pos,v,motionEvent,v_id);
                    return true;
                }
            });
        }
        final VisitRecordBean visitRecordBean = (VisitRecordBean) getItem(position);
        if (visitRecordBean.getCustomer().getImages().size() > 0){
            gridView.setAdapter(new GridViewAdapter(context,visitRecordBean.getCustomer().getImages()));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                    Intent intent = new Intent(context,CasesImageActivity.class);
                    intent.putExtra("showImage",(Serializable) visitRecordBean);
                    intent.putExtra(CasesImageActivity.EXTRA_IMAGE_INDEX,index);
                    context.startActivity(intent);
                }
            });
        }
        convertView.setBackgroundResource(R.drawable.my_tab_background);// 设置listview点击的背景色
        return convertView;
    }
}
