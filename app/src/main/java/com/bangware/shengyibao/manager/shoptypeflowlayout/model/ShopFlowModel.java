package com.bangware.shengyibao.manager.shoptypeflowlayout.model;

import com.bangware.shengyibao.manager.shoptypeflowlayout.presenter.ShopFlowListener;
import com.bangware.shengyibao.user.model.entity.User;

/**
 * Created by bangware on 2016/10/12.
 */
public interface ShopFlowModel {
    void onLoadShopType(String requestTag, User user, ShopFlowListener typeListener);
}
