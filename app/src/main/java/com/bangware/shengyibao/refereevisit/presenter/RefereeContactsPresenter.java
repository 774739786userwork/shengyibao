package com.bangware.shengyibao.refereevisit.presenter;

import com.bangware.shengyibao.customer.model.entity.Contacts;
import com.bangware.shengyibao.refereevisit.model.entity.RefereeVisitor;
import com.bangware.shengyibao.user.model.entity.User;

import java.util.List;


public interface RefereeContactsPresenter {
	public void loadRefereeContacts(User user, int nPage, int nSpage, String contactName, String phone, String employee_id);
	void onDestroy();
	void onLoadRefereeContactsSuccess(List<RefereeVisitor> visitors);
}
