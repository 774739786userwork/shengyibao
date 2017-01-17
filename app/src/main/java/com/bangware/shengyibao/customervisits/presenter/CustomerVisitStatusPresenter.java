package com.bangware.shengyibao.customervisits.presenter;


import com.bangware.shengyibao.user.model.entity.User;

/**
 * 获取拜访状态
 * Created by bangware on 2016/12/1.
 */

public interface CustomerVisitStatusPresenter {
    void queryVisitStatus(User user,String customerId);
    void destory();
}
