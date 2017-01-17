package com.bangware.shengyibao.customervisits.model.impl;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.customervisits.VisitCountUtils;
import com.bangware.shengyibao.customervisits.model.VisitsCountModel;
import com.bangware.shengyibao.customervisits.model.entity.VisitsCountBean;
import com.bangware.shengyibao.customervisits.presenter.OnVisitsCountListener;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;

import java.util.List;

/**
 * Created by Administrator on 2016/12/21.
 */

public class VisitsCountModelImpl implements VisitsCountModel{
    @Override
    public void loadVisitCount(String requestTag, User user,String employee_ids, String begin_date, String end_date,final OnVisitsCountListener listener) {
        try {
      final String visitscount_url= Model.VISITS_COUNT_URL+"?token="+user.getLogin_token()+"&customer_visits_start_date="+begin_date+"&customer_visits_end_date="+end_date+"&employee_ids="+employee_ids+"&show_type=0";
        Log.e("visitscount_url",visitscount_url);
        DataRequest.getInstance().newJsonObjectPostRequest(requestTag,visitscount_url,null, new Response.Listener() {
            @Override
            public void onResponse(Object obj) {
                if (obj!=null)
                {
                    List<VisitsCountBean> countBean= VisitCountUtils.getVisitsBeanList(obj.toString());
                    Log.e("visitscount_url+++++",obj.toString());
                    listener.onLoadDataSuccess(countBean);
                }else
                {
                    listener.onLoadDataFailure("数据传输失败");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                    listener.onLoadDataFailure("请求失败，服务器异常.....");
            }
        });
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
