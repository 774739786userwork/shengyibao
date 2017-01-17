package com.bangware.shengyibao.customervisits.presenter;

import com.bangware.shengyibao.customervisits.model.entity.VisitRecordBean;

import java.util.List;

/**
 * Created by bangware on 2016/12/1.
 */

public interface OnCustomerVisitStatusListener {
    void onLoadStatus(VisitRecordBean status);
    void onLoadDataFailure(String errorMessage);
}
