package com.bangware.shengyibao.customervisits.presenter.impl;

import com.bangware.shengyibao.customer.model.entity.Contacts;
import com.bangware.shengyibao.customervisits.model.CustomerVisitRecordModel;
import com.bangware.shengyibao.customervisits.model.impl.CustomerVisitRecordImpl;
import com.bangware.shengyibao.customervisits.presenter.OnVisitCustomerContactsListener;
import com.bangware.shengyibao.customervisits.presenter.VisitCustomerContactsPresenter;
import com.bangware.shengyibao.customervisits.view.VisitCustomerContactsView;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;

import java.util.List;


public class VisitCustomerContactsPresenterImpl implements VisitCustomerContactsPresenter,OnVisitCustomerContactsListener {
	public  static final String  REQUEST_TAG = "VisitCustomerContacts";
    private CustomerVisitRecordModel visitRecordModel;
    private String requestTag;
	private VisitCustomerContactsView visitCustomerContactsView;

	public VisitCustomerContactsPresenterImpl(
			VisitCustomerContactsView contactsView) {
		 this.requestTag=REQUEST_TAG+System.currentTimeMillis();
		this.visitRecordModel = new CustomerVisitRecordImpl();
		this.visitCustomerContactsView = contactsView;
	}

	@Override
	public void loadCustomerVisitContacts(User user, int nPage, int nSpage, String contactName, String phone, String employee_id) {
		visitCustomerContactsView.showLoading();
		visitRecordModel.loadVisitCustomerContacts(requestTag,user, nPage, nSpage, contactName, phone,employee_id,this);
	}

	@Override
	public void onDestroy() {
		DataRequest.getInstance().cancelRequestByTag(requestTag);
	}

	@Override
	public void onLoadVisitCustomerContactsSuccess(List<Contacts> contacts) {
		if(contacts.size() > 0){
			visitCustomerContactsView.doCustomerContactsVisitSuccess(contacts);
		}
		visitCustomerContactsView.hideLoading();
	}

	@Override
	public void onLoadVisitCustomerContactsFailure(String errorMessage) {
		visitCustomerContactsView.showMessage(errorMessage);
	}
}
