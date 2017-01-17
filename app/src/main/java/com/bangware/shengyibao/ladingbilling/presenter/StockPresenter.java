package com.bangware.shengyibao.ladingbilling.presenter;

import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenBean;
import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenGoods;
import com.bangware.shengyibao.model.Product;
import com.bangware.shengyibao.shopcart.model.entity.ShopCart;
import com.bangware.shengyibao.user.model.entity.User;

/**
 * Created by bangware on 2016/8/22.
 */
public interface StockPresenter {

    void onLoadStock(User user);
    public void addProductGoods(DisburdenGoods disburdenGoods);
    public void removeProductGoods(Product product);
    public DisburdenBean getDisburdenBean();
    void destroy();
}
