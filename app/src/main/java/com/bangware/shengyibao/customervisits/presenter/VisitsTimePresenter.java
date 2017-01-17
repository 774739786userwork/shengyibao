package com.bangware.shengyibao.customervisits.presenter;

import com.bangware.shengyibao.user.model.entity.User;

/**
 * Created by Administrator on 2016/12/22.
 */

public interface VisitsTimePresenter {
    void addVisitTimeRecord(User user, int nPage, int nSpage, String begin_date, String end_date, String employee_id);
    void destory();
}
