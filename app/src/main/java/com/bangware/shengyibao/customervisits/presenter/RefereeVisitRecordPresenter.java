package com.bangware.shengyibao.customervisits.presenter;

import com.bangware.shengyibao.user.model.entity.User;

/**
 * 推荐人拜访
 * Created by bangware on 2016/12/28.
 */

public interface RefereeVisitRecordPresenter {
    void addRefereeRecord(String show_type, User user, int nPage, int nSpage);
    void destory();
}
