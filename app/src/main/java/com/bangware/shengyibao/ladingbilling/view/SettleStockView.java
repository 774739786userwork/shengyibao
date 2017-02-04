package com.bangware.shengyibao.ladingbilling.view;

import com.bangware.shengyibao.activity.BaseView;
import com.bangware.shengyibao.ladingbilling.model.entity.CarBean;
import com.bangware.shengyibao.ladingbilling.model.entity.StockPrinterBean;

import java.util.List;

/**
 * 余货结算数据调用层接口
 * Created by bangware on 2017/1/23.
 */

public interface SettleStockView extends BaseView{
    void loadSettleStockData(List<StockPrinterBean> stockPrinterBeanList);
}
