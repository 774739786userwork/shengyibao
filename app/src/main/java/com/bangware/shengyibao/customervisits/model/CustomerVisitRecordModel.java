package com.bangware.shengyibao.customervisits.model;

/**
 * Created by bangware on 2016/11/26.
 */

import com.bangware.shengyibao.customervisits.presenter.OnCustomerVisitRecordListener;
import com.bangware.shengyibao.user.model.entity.User;

/**
 * 拜访记录数据接口模型
 */
public interface CustomerVisitRecordModel {
    void loadVisitRecord(String requestTag, User user, String show_type, int nPage, int nSpage, OnCustomerVisitRecordListener visitRecordListener);
}
