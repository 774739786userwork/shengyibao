package com.bangware.shengyibao.ladingbilling.view;

import com.bangware.shengyibao.activity.BaseView;
import com.bangware.shengyibao.ladingbilling.model.entity.CarBean;
import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenBean;
import com.bangware.shengyibao.ladingbilling.model.entity.LadingbillingQuery;
import com.bangware.shengyibao.model.Product;
import com.bangware.shengyibao.shopcart.model.entity.ShopCart;

import java.util.List;

/**
 * Created by bangware on 2016/8/22.
 */
public interface StockQueryView extends BaseView{
    void doProcuctChanged(DisburdenBean bean);
    void loadProductStockData(List<Product> productstockList);
}
