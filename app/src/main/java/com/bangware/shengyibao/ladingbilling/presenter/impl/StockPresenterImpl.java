package com.bangware.shengyibao.ladingbilling.presenter.impl;

import com.bangware.shengyibao.ladingbilling.model.StockQueryModel;
import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenBean;
import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenGoods;
import com.bangware.shengyibao.ladingbilling.model.impl.StockQueryModelImpl;
import com.bangware.shengyibao.ladingbilling.presenter.StockListener;
import com.bangware.shengyibao.ladingbilling.presenter.StockPresenter;
import com.bangware.shengyibao.ladingbilling.view.StockQueryView;
import com.bangware.shengyibao.model.Product;
import com.bangware.shengyibao.shopcart.model.entity.StockInfo;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;

/**
 * Created by bangware on 2016/8/22.
 */
public class StockPresenterImpl implements StockPresenter,StockListener{
    public static final String REQUEST_TAG = "Stock";
    private StockQueryModel queryModel;
    private StockQueryView queryView;
    private DisburdenBean bean;
    private String requestTag;


    public StockPresenterImpl(StockQueryView queryView) {
        this.requestTag = REQUEST_TAG+System.currentTimeMillis();
        this.queryView=queryView;
        this.queryModel = new StockQueryModelImpl();
        this.bean=new DisburdenBean();
    }

    @Override
    public void onStockLoaded(StockInfo stockInfo) {
        queryView.hideLoading();
        queryView.showMessage(stockInfo.getMsg());
        queryView.loadProductStockData(stockInfo.getProducts());
    }

    @Override
    public void onErrors(String errorMessage) {
        queryView.hideLoading();
        queryView.showMessage(errorMessage);
    }

    @Override
    public void onLoadStock(User user,String CarId) {
        queryView.showLoading();
        queryModel.onQueryStockinfo(requestTag, user,CarId,this);
    }

    @Override
    public void addProductGoods(DisburdenGoods disburdenGoods) {
        bean.addProductGoods(disburdenGoods);
        queryView.doProcuctChanged(bean);
    }

    @Override
    public void removeProductGoods(Product product) {
            bean.removeProductGoods(product);
            queryView.doProcuctChanged(bean);
    }

    @Override
    public DisburdenBean getDisburdenBean() {
        return this.bean;
    }


    @Override
    public void destroy() {
        DataRequest.getInstance().cancelRequestByTag(requestTag);
    }
}
