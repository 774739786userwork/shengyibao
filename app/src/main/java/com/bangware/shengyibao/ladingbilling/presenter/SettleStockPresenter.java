package com.bangware.shengyibao.ladingbilling.presenter;

import com.bangware.shengyibao.ladingbilling.model.entity.CarBean;
import com.bangware.shengyibao.user.model.entity.User;

import java.util.List;

/**
 * 结算余货调用接口
 * Created by bangware on 2017/1/23.
 */

public interface SettleStockPresenter {
    void onLoadSettleStock(User user, List<CarBean> carBeanList);
    void destroy();
}
