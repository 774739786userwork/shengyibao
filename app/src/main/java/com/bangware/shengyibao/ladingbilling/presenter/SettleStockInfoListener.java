package com.bangware.shengyibao.ladingbilling.presenter;

import com.bangware.shengyibao.ladingbilling.model.entity.CarBean;

import java.util.List;

/**
 * 结算余货信息监听
 * Created by bangware on 2017/1/23.
 */

public interface SettleStockInfoListener {
    public void onSettleStockSuccess(List<CarBean> carBeanList);
    public void onErrors(String errorMessage);
}
