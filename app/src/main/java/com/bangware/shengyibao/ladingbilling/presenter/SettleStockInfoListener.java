package com.bangware.shengyibao.ladingbilling.presenter;

import com.bangware.shengyibao.ladingbilling.model.entity.CarBean;
import com.bangware.shengyibao.ladingbilling.model.entity.StockPrinterBean;

import java.util.List;

/**
 * 结算余货信息监听
 * Created by bangware on 2017/1/23.
 */

public interface SettleStockInfoListener {
    public void onSettleStockSuccess(List<StockPrinterBean> stockPrinterBeanList);
    public void onErrors(String errorMessage);
}
