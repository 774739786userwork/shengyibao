package com.bangware.shengyibao.customervisits.presenter;

import com.bangware.shengyibao.user.model.entity.User;


public interface VisitCustomerContactsPresenter {
	public void loadCustomerVisitContacts(User user, int nPage, int nSpage, String contactName, String phone, String employee_id);
	void onDestroy();
}
