package com.bangware.shengyibao.refereevisit.presenter;

import com.bangware.shengyibao.customer.model.entity.Contacts;
import com.bangware.shengyibao.refereevisit.model.entity.RefereeVisitor;

import java.util.List;


public interface OnRefereeContactsListener {
	void onLoadRefereeContactsSuccess(List<RefereeVisitor> visitors);
	void onLoadRefereeContactsFailure(String errorMessage);
}
