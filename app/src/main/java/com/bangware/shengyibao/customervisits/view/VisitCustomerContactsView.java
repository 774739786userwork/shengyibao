package com.bangware.shengyibao.customervisits.view;

import com.bangware.shengyibao.activity.BaseView;
import com.bangware.shengyibao.customer.model.entity.Contacts;

import java.util.List;


public interface VisitCustomerContactsView extends BaseView {
	 
	 void doCustomerContactsVisitSuccess(List<Contacts> Contacts);
}
