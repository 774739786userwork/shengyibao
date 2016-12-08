package com.bangware.shengyibao.customervisits.model.impl;

/**
 * Created by bangware on 2016/12/1.
 */

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.customervisits.CustomerVisitsUtils;
import com.bangware.shengyibao.customervisits.model.CustomerVisitRecordModel;
import com.bangware.shengyibao.customervisits.model.entity.VisitRecordBean;
import com.bangware.shengyibao.customervisits.presenter.OnCustomerVisitRecordListener;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;

import org.json.JSONObject;

import java.util.List;
/**
 * 数据请求加载实现
 */
public class CustomerVisitRecordImpl implements CustomerVisitRecordModel{
    @Override
    public void loadVisitRecord(String requestTag, User user, String show_type, int nPage, int nSpage, final OnCustomerVisitRecordListener visitRecordListener) {
       try {
            final String visit_record_url = Model.CUSTOMER_VISIT_RECORD_URL +"?token="+user.getLogin_token()+"&show_type="+show_type+"&page="+nPage+"&rows="+nSpage+"&employee_id="+user.getEmployee_id();
           Log.e("visit_record_url",visit_record_url);
            DataRequest.getInstance().newJsonObjectGetRequest(requestTag, visit_record_url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jsonObject) {
                    // TODO Auto-generated method stub
                    if(jsonObject != null){
                            List<VisitRecordBean> recordBeanLists = CustomerVisitsUtils.getCustomersList(jsonObject.toString());
                            visitRecordListener.onLoadDataSuccess(recordBeanLists);
                    }else{
                        visitRecordListener.onLoadDataFailure("数据传输失败！");
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    visitRecordListener.onLoadDataFailure("请求失败，服务器异常......");
                }
            });
       }catch (Exception ex){
            ex.printStackTrace();
       }
    }
}
