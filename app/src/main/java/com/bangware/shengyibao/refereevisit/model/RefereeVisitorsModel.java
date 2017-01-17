package com.bangware.shengyibao.refereevisit.model;

import com.bangware.shengyibao.refereevisit.presenter.OnRefereeContactsListener;
import com.bangware.shengyibao.user.model.entity.User;

/**
 * Created by Administrator on 2016/12/27.
 */

public interface RefereeVisitorsModel {
    public void loadCustomerContacts(String requestTag, User user, int nPage, int nSpage, String phone,
                                     String contactName,String employee_id,
                                     OnRefereeContactsListener contactsListener);
}
