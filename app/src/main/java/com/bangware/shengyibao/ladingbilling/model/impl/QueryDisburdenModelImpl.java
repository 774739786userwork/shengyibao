package com.bangware.shengyibao.ladingbilling.model.impl;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.ladingbilling.LadingBillingUtils;
import com.bangware.shengyibao.ladingbilling.model.QueryDisburdenModel;
import com.bangware.shengyibao.ladingbilling.model.entity.QueryDisburdenBean;
import com.bangware.shengyibao.ladingbilling.presenter.OnQueryDisburenListener;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;
import com.google.android.gms.appdatasearch.GetRecentContextCall;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017/1/20.
 */

public class QueryDisburdenModelImpl implements QueryDisburdenModel {
    @Override
    public void LoadQueryDisburden(String requestTag, User user, final OnQueryDisburenListener disburenListener) {
     String query_disburden= Model.QUERY_DISBURDEN+"&employee_id="+user.getEmployee_id()+"&token="+user.getLogin_token();
        DataRequest.getInstance().newJsonObjectGetRequest(requestTag, query_disburden, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                 if (jsonObject!=null)
                 {
                     List<QueryDisburdenBean> list= LadingBillingUtils.getLoadingDisburden(jsonObject.toString());
                     disburenListener.onSaveSuccess(list);
                 }else
                 {
                     disburenListener.onError("返回内容为空！数据传输失败！");
                 }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                disburenListener.onError("请求失败，服务器异常......！");
            }
        });
    }

    @Override
    public void OnCancellationDisburden(String requestTag, User user, String disburden_id, final OnQueryDisburenListener disburenListener) {
          String cancel_disburden_url=Model.CANCEL_DISBURDEN+"&token="+user.getLogin_token()+"&disburden_order_id="+disburden_id;
          DataRequest.getInstance().newJsonObjectPostRequest(requestTag, cancel_disburden_url, null, new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject jsonObject) {
                  if (jsonObject!=null)
                  {
                      disburenListener.onCancelSuccess("操作成功！");
                  }else
                  {
                      disburenListener.onError("返回内容为空");
                  }

              }
          }, new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError volleyError) {
                  disburenListener.onError("请求失败，服务器异常......！");
              }
          });

    }
}
