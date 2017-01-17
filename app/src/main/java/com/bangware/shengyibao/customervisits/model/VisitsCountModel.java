package com.bangware.shengyibao.customervisits.model;

import com.bangware.shengyibao.customervisits.presenter.OnVisitsCountListener;
import com.bangware.shengyibao.user.model.entity.User;

/**
 * Created by Administrator on 2016/12/21.
 */

public interface VisitsCountModel {
    void loadVisitCount(String requestTag, User user, String employee_ids, String begin_date, String end_date, OnVisitsCountListener listener);
}
