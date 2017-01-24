package com.bangware.shengyibao.ladingbilling.model.impl;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.ladingbilling.model.StockQueryModel;
import com.bangware.shengyibao.ladingbilling.model.entity.CarBean;
import com.bangware.shengyibao.ladingbilling.presenter.SettleStockInfoListener;
import com.bangware.shengyibao.ladingbilling.presenter.StockListener;
import com.bangware.shengyibao.shopcart.model.entity.StockInfo;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by bangware on 2016/8/22.
 */
/**查询当天余货**/
public class StockQueryModelImpl implements StockQueryModel{
    @Override
    public void onQueryStockinfo(String requestTag, User user,String CarId, final StockListener stockListener) {
        String stockinfo_url = Model.STOCKQUERYURL + "&employee_id="+user.getEmployee_id()+"&token="+user.getLogin_token()+"&carbaseinfo_id="+CarId;
        Log.e("stockinfo_url",stockinfo_url);
        DataRequest.getInstance().newGsonGetRequest(requestTag,stockinfo_url, StockInfo.class,
                new Response.Listener<StockInfo>() {
                    @Override
                    public void onResponse(StockInfo stockInfo) {
                        if (stockInfo != null){
                            if(StockInfo.RESULT_SUCCESS.equals(stockInfo.getResult())) {
                                stockListener.onStockLoaded(stockInfo);
                            }else{
                                stockListener.onErrors(stockInfo.getMsg());
                            }
                        } else {
                            stockListener.onErrors("返回结果转换失败！");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        stockListener.onErrors("请求失败，服务器异常......");
                    }
                });
    }

    /**查询结算余货**/
    @Override
    public void onQuerySettleStockInfo(String requestTag, User user, List<CarBean> carBeanList, final SettleStockInfoListener settleStockInfoListener) {
        String query_settlestock_url= Model.SETTLESTOCKURL+"&token="+user.getLogin_token();
        DataRequest.getInstance().newJsonObjectGetRequest(requestTag, query_settlestock_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject!=null)
                {
                    /*List<CarBean> list= ;
                    settleStockInfoListener.onSettleStockSuccess(list);*/
                }else
                {
                    settleStockInfoListener.onErrors("返回内容为空！数据传输失败！");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                settleStockInfoListener.onErrors("请求失败，服务器异常......！");
            }
        });
    }
}
