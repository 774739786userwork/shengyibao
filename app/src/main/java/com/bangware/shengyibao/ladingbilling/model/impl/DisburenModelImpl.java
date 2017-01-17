package com.bangware.shengyibao.ladingbilling.model.impl;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.ladingbilling.model.DisburenModel;
import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenGoods;
import com.bangware.shengyibao.ladingbilling.presenter.OnDisburenSaveListener;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 * @author zcb
 */

public class DisburenModelImpl implements DisburenModel {
    @Override
    public void save(User user, String requestTag, final List<DisburdenGoods> disburdenGoodsList,String carId, final OnDisburenSaveListener listener) {
        String disburen_url= Model.DISBUREN_SAVA+"token="+user.getLogin_token();
        try{
        JSONArray jsonArray = new JSONArray();
        JSONObject objectParams;
        JSONObject jsonDelivery = new JSONObject();
        JSONObject jsonData = new JSONObject();
        for (int i = 0; i < disburdenGoodsList.size(); i++) {
            objectParams = new JSONObject();
            objectParams.put("product_id",disburdenGoodsList.get(i).getProduct().getId());
            objectParams.put("quantity",disburdenGoodsList.get(i).getDisburden());
            jsonArray.put(objectParams);
        }
            jsonDelivery.put("list_goods",jsonArray);
            jsonDelivery.put("carbaseinfo_id",carId);
            jsonDelivery.put("user_id",user.getUser_id());
            jsonData.put("data", jsonDelivery);
            DataRequest.getInstance().newJsonObjectPostRequest(requestTag,disburen_url, jsonData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    if (jsonObject != null) {
                        try{
                            if(jsonObject.getInt("result")==0){
                                listener.onSaveSuccess(disburdenGoodsList);
                            }else{
                                listener.onError("数据保存失败！");
                            }
                        }catch(JSONException jsonException){
                            listener.onError("JSON解析错误！");
                        }
                    } else {
                        listener.onError("返回内容为空！");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listener.onError("请求失败，服务器异常......");
                }
            });
        }catch(JSONException ex){
            listener.onError("请求参数设置失败！");
        }
    }
}
