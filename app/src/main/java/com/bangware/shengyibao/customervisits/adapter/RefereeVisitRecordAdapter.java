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
import com.bangware.shengyibao.customer.view.ShowImageViewActivity;
import com.bangware.shengyibao.customervisits.model.entity.OwnerBean;
import com.bangware.shengyibao.customervisits.model.entity.RefereeBean;
import com.bangware.shengyibao.utils.OnScrollGridView.NoScrollGridView;

import java.util.List;

/**
 * 推荐人拜访记录数据源适配
 * Created by bangware on 2016/12/28.
 */

public class RefereeVisitRecordAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private Context context;
    private List<RefereeBean> list;
    private Callback mCallback;
    /**
     * 自定义接口，用于回调语音播放触摸事件到Activity
     */
    public interface Callback {
        boolean onTouchEvent(int pos, View v, MotionEvent motionEvent, int which);
    }

    public  RefereeVisitRecordAdapter(Context context,List<RefereeBean> list,Callback mCallback){
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
            convertView = inflater.inflate(R.layout.item_visit_referee_list, null);
        }
        /**客户信息UI**/
        TextView visit_time = ViewHolder.get(convertView,R.id.referee_time_textview);
        TextView visit_content = ViewHolder.get(convertView,R.id.referee_content_textview);
        LinearLayout sound_file = ViewHolder.get(convertView,R.id.sound_referee_file);
        TextView visit_voice = ViewHolder.get(convertView,R.id.text_pictureReplace);
        TextView voice_time = ViewHolder.get(convertView,R.id.recorded_time_textview);
        TextView visit_refereeName = ViewHolder.get(convertView,R.id.visit_refereeName_textview);
        TextView visit_type = ViewHolder.get(convertView,R.id.visit_refereeStatus_textview);
        TextView visit_refereePhone = ViewHolder.get(convertView,R.id.visit_refereeMobile_textview);

        /**业主信息UI**/
        LinearLayout ownerInfo_linearLayout = ViewHolder.get(convertView,R.id.owner_referee_linearLayout);
        TextView owner_name = ViewHolder.get(convertView,R.id.owner_refereeName_textview);
        TextView owner_mobile = ViewHolder.get(convertView,R.id.owner_refereeMobile_textview);
        TextView owner_acreage = ViewHolder.get(convertView,R.id.owner_refereeAcreage_textview);
        TextView owner_price = ViewHolder.get(convertView,R.id.owner_refereePrice_textview);

        /**拜访图片信息UI**/
        NoScrollGridView gridView = ViewHolder.get(convertView,R.id.image_referee_gridview);

        visit_time.setText(list.get(position).getVisitTime());
        visit_type.setText("("+list.get(position).getIssueType()+")");
        visit_refereeName.setText(list.get(position).getRefereeName());
        visit_refereePhone.setText(list.get(position).getRefereeMobile());

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

        //listview嵌套了gridview ，往gridview中添加数据源
        GridViewAdapter gridViewAdapter=new GridViewAdapter(context, list.get(position).getCustomer().getImages());
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                Intent intent = new Intent(context,ShowImageViewActivity.class);
                intent.putExtra("showImage", list.get(position).getCustomer().getImages().get(index).getImg_url());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
