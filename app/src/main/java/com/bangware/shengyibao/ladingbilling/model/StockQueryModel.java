package com.bangware.shengyibao.ladingbilling.model;

import com.bangware.shengyibao.ladingbilling.model.entity.CarBean;
import com.bangware.shengyibao.ladingbilling.presenter.SettleStockInfoListener;
import com.bangware.shengyibao.ladingbilling.presenter.StockListener;
import com.bangware.shengyibao.user.model.entity.User;

import java.util.List;

/**
 * 余货查询模型接口
 * Created by bangware on 2016/8/22.
 */
public interface StockQueryModel {
    void onQueryStockinfo(String requestTag, User user,String CarId, StockListener stockListener);

    /**查询结算余货信息**/
    void onQuerySettleStockInfo(String requestTag, User user, List<CarBean> carBeanList, SettleStockInfoListener settleStockInfoListener);
}
