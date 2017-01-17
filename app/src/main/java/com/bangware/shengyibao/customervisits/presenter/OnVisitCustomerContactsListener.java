package com.bangware.shengyibao.customervisits.presenter;

import com.bangware.shengyibao.customer.model.entity.Contacts;

import java.util.List;


public interface OnVisitCustomerContactsListener {
	void onLoadVisitCustomerContactsSuccess(List<Contacts> contacts);
	void onLoadVisitCustomerContactsFailure(String errorMessage);
}
