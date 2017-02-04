package com.bangware.shengyibao.ladingbilling.presenter.impl;

import com.bangware.shengyibao.ladingbilling.model.StockQueryModel;
import com.bangware.shengyibao.ladingbilling.model.entity.CarBean;
import com.bangware.shengyibao.ladingbilling.model.entity.StockPrinterBean;
import com.bangware.shengyibao.ladingbilling.model.impl.StockQueryModelImpl;
import com.bangware.shengyibao.ladingbilling.presenter.SettleStockInfoListener;
import com.bangware.shengyibao.ladingbilling.presenter.SettleStockPresenter;
import com.bangware.shengyibao.ladingbilling.view.SettleStockView;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;

import java.util.List;

/**
 * 结算余货的数据请求实现类
 * Created by bangware on 2017/1/23.
 */

public class SettleStockPresenterImpl implements SettleStockPresenter,SettleStockInfoListener{
    public static final String REQUEST_TAG = "SettleStockInfo";
    private StockQueryModel queryModel;
    private SettleStockView settleStockView;
    private String requestTag;

    public SettleStockPresenterImpl(SettleStockView queryView) {
        this.requestTag = REQUEST_TAG+System.currentTimeMillis();
        this.settleStockView=queryView;
        this.queryModel = new StockQueryModelImpl();
    }
    @Override
    public void onSettleStockSuccess(List<StockPrinterBean> printerBeanList) {
        settleStockView.hideLoading();
        settleStockView.loadSettleStockData(printerBeanList);
    }

    @Override
    public void onErrors(String errorMessage) {
        settleStockView.hideLoading();
        settleStockView.showMessage(errorMessage);
    }

    @Override
    public void onLoadSettleStock(User user, String carbaseinfo_ids) {
        settleStockView.showLoading();
        queryModel.onQuerySettleStockInfo(requestTag,user,carbaseinfo_ids,this);
    }

    @Override
    public void destroy() {
        DataRequest.getInstance().cancelRequestByTag(requestTag);
    }
}
