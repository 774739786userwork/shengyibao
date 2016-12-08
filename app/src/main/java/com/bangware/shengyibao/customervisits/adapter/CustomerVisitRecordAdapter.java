package com.bangware.shengyibao.customervisits.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.ViewHolder;
import com.bangware.shengyibao.customervisits.model.entity.VisitRecordBean;
import com.bangware.shengyibao.customervisits.view.CustomGridView;

import java.util.List;


/**
 * 拜访记录数据源适配
 * Created by bangware on 2016/11/26.
 */

public class CustomerVisitRecordAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private Context context;
    private List<VisitRecordBean> list;
    private int flag = 0;
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
        return position;
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

        TextView visit_time = ViewHolder.get(convertView,R.id.visit_time_textview);
        TextView visit_content = ViewHolder.get(convertView,R.id.visit_content_textview);
        LinearLayout sound_file = ViewHolder.get(convertView,R.id.sound_record_file);
        TextView visit_voice = ViewHolder.get(convertView,R.id.text_pictureReplace);
        TextView voice_time = ViewHolder.get(convertView,R.id.recorded_time_textview);
        TextView visit_customer = ViewHolder.get(convertView,R.id.visit_customer_textview);
        TextView visit_type = ViewHolder.get(convertView,R.id.visit_type_textview);
        TextView visit_address = ViewHolder.get(convertView,R.id.visit_customerAddress_textview);
        CustomGridView gridView = ViewHolder.get(convertView,R.id.image_gridview);

        visit_time.setText(list.get(position).getVisitTime());
        visit_type.setText("("+list.get(position).getVisitType()+"),("+list.get(position).getIssueType()+")");
        visit_customer.setText(list.get(position).getCustomer().getName());
        visit_address.setText(list.get(position).getCustomer().getAddress());

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

        //listview嵌套了gridview ，往gridview中添加数据源
        GridViewAdapter gridViewAdapter=new GridViewAdapter(context, list.get(position).getCustomer().getImages());
        gridView.setAdapter(gridViewAdapter);
//        convertView.setBackgroundResource(R.drawable.my_tab_background);// 设置listview点击的背景色
        return convertView;
    }
}
