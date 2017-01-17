package com.bangware.shengyibao.daysaleaccount.presenter;

import com.bangware.shengyibao.daysaleaccount.model.entity.ChoicePersonBean;
import com.bangware.shengyibao.ladingbilling.model.entity.LadingbillingQuery;

import java.util.List;

/**
 * 业余员查询
 * Created by bangware on 2016/12/15.
 */

public interface OnSalerPersonListener {
    void onLoadSalesPersonSuccess(List<ChoicePersonBean> personBeenList);
    void onLoadSalesPersonFailure(String errorMessage);
}
