package com.bangware.shengyibao.manager.shoptypeflowlayout.model.impl;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.customer.CustomerUtils;
import com.bangware.shengyibao.manager.shoptypeflowlayout.flowlayout.Flow;
import com.bangware.shengyibao.manager.shoptypeflowlayout.model.ShopFlowModel;
import com.bangware.shengyibao.manager.shoptypeflowlayout.presenter.ShopFlowListener;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by bangware on 2016/10/12.
 */
public class ShopFlowModelImpl implements ShopFlowModel{
    @Override
    public void onLoadShopType(String requestTag, User user, final ShopFlowListener typeListener) {
        String shop_type_url = Model.CUSTOMER_TYPE_URL+"&token="+ user.getLogin_token();
        DataRequest.getInstance().newJsonObjectGetRequest(requestTag, shop_type_url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                // TODO Auto-generated method stub
                if (jsonObject!=null) {
                    List<Flow> grouplist = CustomerUtils.getShopTypeFlow(jsonObject.toString());
                    typeListener.onLoadGroupSuccess(grouplist);
                }else
                {
                    typeListener.onLoadGroupFailure("返回内容为空！");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                typeListener.onLoadGroupFailure("请求失败，服务器异常......");
            }
        });
    }
}
