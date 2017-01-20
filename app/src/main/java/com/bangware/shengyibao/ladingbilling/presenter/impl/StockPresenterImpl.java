package com.bangware.shengyibao.ladingbilling.presenter.impl;

import android.util.Log;

import com.bangware.shengyibao.ladingbilling.model.StockQueryModel;
import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenBean;
import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenGoods;
import com.bangware.shengyibao.ladingbilling.model.entity.LadingbillingQuery;
import com.bangware.shengyibao.ladingbilling.model.impl.StockQueryModelImpl;
import com.bangware.shengyibao.ladingbilling.presenter.StockListener;
import com.bangware.shengyibao.ladingbilling.presenter.StockPresenter;
import com.bangware.shengyibao.ladingbilling.view.StockQueryView;
import com.bangware.shengyibao.model.Product;
import com.bangware.shengyibao.shopcart.model.entity.ShopCartGoods;
import com.bangware.shengyibao.shopcart.model.entity.StockInfo;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.volley.DataRequest;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.wch.wchusbdriver.CH34xAndroidDriver.TAG;

/**
 * Created by bangware on 2016/8/22.
 */
public class StockPresenterImpl implements StockPresenter,StockListener{
    public static final String REQUEST_TAG = "Stock";
    private StockQueryModel queryModel;
    private StockQueryView queryView;
    private DisburdenBean bean;
    private String requestTag;
    LadingbillingQuery ladingbillingQuery;


    public StockPresenterImpl(StockQueryView queryView) {
        this.requestTag = REQUEST_TAG+System.currentTimeMillis();
        this.queryView=queryView;
        this.queryModel = new StockQueryModelImpl();
        this.bean=new DisburdenBean();
        this.ladingbillingQuery = new LadingbillingQuery();
    }

    @Override
    public void onStockLoaded(StockInfo stockInfo) {
        queryView.hideLoading();
        queryView.showMessage(stockInfo.getMsg());
        queryView.loadProductStockData(stockInfo.getProducts());
        ladingbillingQuery.setCarnumber(stockInfo.getCarNumber());
        ladingbillingQuery.setCarId(stockInfo.getCarId());
        queryView.doChanged(ladingbillingQuery);
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
//    private void updateDisburden(){
//        List<DisburdenGoods> productList=bean.getAllGoodsList();
//        for (DisburdenGoods product:productList)
//        {
//            DisburdenGoods.(product.get);
//        }
//        List<ShopCartGoods> shopCartGoodsList = shopCart.getAllGoodsList();
//        int totalVolumes = 0;
//        double totalAmount = 0;
//        double p_totalFordgift=0;
//        int totalFordgift=0;
//        for(ShopCartGoods goods: shopCartGoodsList){
//            totalVolumes+=goods.getTotalVolume();
//            totalAmount+=goods.getTotalAmount()+goods.getTotalForegift();
//            p_totalFordgift=goods.getTotalForegift();
//            totalFordgift+=goods.getTotalForegift();
//        }
//        BigDecimal bd = new BigDecimal(totalAmount);
//        bd = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN);
//        shopCart.setTotalAmount(bd.doubleValue());
//        shopCart.setTotalVolumes(totalVolumes);
//        shopCart.setTotalForegift(totalFordgift);
//        shopCart.setP_totalForegift(p_totalFordgift);
//    }
}
