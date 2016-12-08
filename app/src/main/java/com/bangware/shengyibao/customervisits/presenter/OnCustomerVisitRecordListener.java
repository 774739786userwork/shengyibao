package com.bangware.shengyibao.customervisits.presenter;

import com.bangware.shengyibao.customervisits.model.entity.VisitRecordBean;

import java.util.List;

/**
 * Created by bangware on 2016/12/1.
 */

public interface OnCustomerVisitRecordListener {
    void onLoadDataSuccess(List<VisitRecordBean> recordList);
    void onLoadDataFailure(String errorMessage);
}
