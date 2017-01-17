package com.bangware.shengyibao.customervisits.presenter;

import com.bangware.shengyibao.user.model.entity.User;

/**
 * Created by Administrator on 2016/12/21.
 */

public interface VisitsCountPersenter {
    void queryVisitCount(User user, String employee_ids, String begin_date, String end_date);
    void destory();
}
