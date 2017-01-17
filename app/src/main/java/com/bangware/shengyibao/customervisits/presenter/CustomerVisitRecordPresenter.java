package com.bangware.shengyibao.customervisits.presenter;


import com.bangware.shengyibao.user.model.entity.User;

/**
 * Created by bangware on 2016/12/1.
 */

public interface CustomerVisitRecordPresenter {
    void addVisitRecord(String show_type, User user, int nPage, int nSpage);
    void destory();
}
