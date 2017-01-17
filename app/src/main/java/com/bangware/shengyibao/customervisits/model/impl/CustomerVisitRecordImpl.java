package com.bangware.shengyibao.customervisits.model.impl;

/**
 * Created by bangware on 2016/12/1.
 */

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.customer.model.entity.Contacts;
import com.bangware.shengyibao.customercontacts.CustomerContactsJosnUtils;
import com.bangware.shengyibao.customervisits.CustomerVisitsUtils;
import com.bangware.shengyibao.customervisits.model.CustomerVisitRecordModel;
import com.bangware.shengyibao.customervisits.model.entity.RefereeBean;
import com.bangware.shengyibao.customervisits.model.entity.VisitRecordBean;
import com.bangware.shengyibao.customervisits.presenter.OnCustomerVisitRecordListener;
import com.bangware.shengyibao.customervisits.presenter.OnCustomerVisitStatusListener;
import com.bangware.shengyibao.customervisits.presenter.OnRefereeVisitRecoedListener;
import com.bangware.shengyibao.customervisits.presenter.OnVisitCustomerContactsListener;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
/**
 * 数据请求加载实现
 */
public class CustomerVisitRecordImpl implements CustomerVisitRecordModel{
    //获取客户拜访记录请求
    @Override
    public void loadVisitRecord(String requestTag, User user, String show_type, int nPage, int nSpage, final OnCustomerVisitRecordListener visitRecordListener) {
       try {
            final String visit_record_url = Model.CUSTOMER_VISIT_RECORD_URL +"?token="+user.getLogin_token()+"&show_type="+show_type+"&page="+nPage+"&rows="+nSpage+"&employee_id="+user.getEmployee_id()+"&flag=customer";
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

    //获取推荐人拜访记录请求
    @Override
    public void loadRefereeVisitRecord(String requestTag, User user, String show_type, int nPage, int nSpage,final OnRefereeVisitRecoedListener refereeVisitRecoedListener) {
        try {
            final String visit_referee_url = Model.CUSTOMER_VISIT_RECORD_URL +"?token="+user.getLogin_token()+"&show_type="+show_type+"&page="+nPage+"&rows="+nSpage+"&employee_id="+user.getEmployee_id()+"&flag=referee";
            Log.e("visit_record_url",visit_referee_url);
            DataRequest.getInstance().newJsonObjectGetRequest(requestTag, visit_referee_url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jsonObject) {
                    // TODO Auto-generated method stub
                    if(jsonObject != null){
                        List<RefereeBean> refereeBeanList = CustomerVisitsUtils.getRefereeDataList(jsonObject.toString());
                        refereeVisitRecoedListener.onLoadDataSuccess(refereeBeanList);
                    }else{
                        refereeVisitRecoedListener.onLoadDataFailure("数据传输失败！");
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    refereeVisitRecoedListener.onLoadDataFailure("请求失败，服务器异常......");
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //获取拜访过的客户的联系人请求
    @Override
    public void loadVisitCustomerContacts(String requestTag, User user, int nPage, int nSpage, String phone, String contactName, String employee_id,final OnVisitCustomerContactsListener customerContactsListener) {
        String customer_visit_contacts_url;
        try {
            customer_visit_contacts_url = Model.CUSETOMER_CONTACTS_DETAIL_URL+"?token="+user.getLogin_token()+"&show_status=visits"+"&page="+nPage+"&rows="+nSpage
                    +"&contactMobile="+phone+"&content="+ URLEncoder.encode(contactName, "UTF-8")+"&employee_id="+employee_id;
            Log.e("TGA",customer_visit_contacts_url);
            DataRequest.getInstance().newJsonObjectGetRequest(requestTag, customer_visit_contacts_url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jsonObject) {
                    if(jsonObject != null){
                        List<Contacts> contacts = CustomerContactsJosnUtils.getCustomerContactsList(jsonObject.toString());
                        customerContactsListener.onLoadVisitCustomerContactsSuccess(contacts);
                        Log.d("TAG", "query11111  "+contacts.size());
                    }else{
                        customerContactsListener.onLoadVisitCustomerContactsFailure("数据传输失败！");
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    customerContactsListener.onLoadVisitCustomerContactsFailure("请求失败，服务器异常......");
                }
            });
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //获取拜访状态请求
    @Override
    public void loadVisitStatus(String requestTag, User user, String customerId, final OnCustomerVisitStatusListener visitStatusListener) {
        try
        {
            String status_url = Model.CUSTOMER_VISIT_STATUS_URL + "?token="+user.getLogin_token()+"&employee_id="+user.getEmployee_id()+"&customer_id="+customerId;
            DataRequest.getInstance().newJsonObjectGetRequest(requestTag, status_url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jsonObject) {
                    // TODO Auto-generated method stub
                    if(jsonObject != null){
                        VisitRecordBean status = CustomerVisitsUtils.getVisitStatus(jsonObject.toString());
                        visitStatusListener.onLoadStatus(status);
                    }else{
                        visitStatusListener.onLoadDataFailure("数据传输失败！");
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    visitStatusListener.onLoadDataFailure("请求失败，服务器异常......");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void loadVisitTimeRecord(String requestTag, User user, int nPage, int nSpage, String begin_date, String end_date, String employee_id, final OnCustomerVisitRecordListener visitRecordListener) {
        try {
             String visit_time_url = Model.CUSTOMER_VISIT_RECORD_URL + "?token=" + user.getLogin_token() + "&page=" + nPage + "&rows=" + nSpage + "&begin_date=" + begin_date +
                    "&end_date=" + end_date + "&employee_id=" + employee_id+"&flag=customer";
            Log.e("visit_time_url",visit_time_url);
            DataRequest.getInstance().newJsonObjectGetRequest(requestTag, visit_time_url, null, new Response.Listener<JSONObject>() {

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
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
