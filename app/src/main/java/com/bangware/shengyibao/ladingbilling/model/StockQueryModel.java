package com.bangware.shengyibao.ladingbilling.model;

import com.bangware.shengyibao.ladingbilling.presenter.SettleStockInfoListener;
import com.bangware.shengyibao.ladingbilling.presenter.StockListener;
import com.bangware.shengyibao.user.model.entity.User;

/**
 * 余货查询模型接口
 * Created by bangware on 2016/8/22.
 */
public interface StockQueryModel {
    void onQueryStockinfo(String requestTag, User user,String CarId, StockListener stockListener);

    /**查询结算余货信息**/
    void onQuerySettleStockInfo(String requestTag, User user, String carbaseinfo_ids, SettleStockInfoListener settleStockInfoListener);
}
