package com.bangware.shengyibao.refereevisit.presenter.impl;

import com.bangware.shengyibao.customer.model.entity.Contacts;
import com.bangware.shengyibao.customercontacts.model.CustomerContactsModel;
import com.bangware.shengyibao.customercontacts.model.impl.CustomerContactsModelImpl;
import com.bangware.shengyibao.customercontacts.presenter.CustomerContactsPresenter;
import com.bangware.shengyibao.customercontacts.presenter.OnCustomerContactsListener;
import com.bangware.shengyibao.customercontacts.view.CustomerContactsView;
import com.bangware.shengyibao.refereevisit.model.RefereeVisitorsModel;
import com.bangware.shengyibao.refereevisit.model.entity.RefereeVisitor;
import com.bangware.shengyibao.refereevisit.model.impl.RefereeVisitorsModelImpl;
import com.bangware.shengyibao.refereevisit.presenter.OnRefereeContactsListener;
import com.bangware.shengyibao.refereevisit.presenter.RefereeContactsPresenter;
import com.bangware.shengyibao.refereevisit.view.RefereeContactsView;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;

import java.util.List;


public class RefereeContactsPresenterImpl implements RefereeContactsPresenter,OnRefereeContactsListener {

	public  static final String  REQUEST_TAG = "QueryRefereeVisiors";

    private RefereeVisitorsModel visitorsModel;
    private RefereeContactsView contactsView;
    private String requestTag;
	public RefereeContactsPresenterImpl(
			RefereeContactsView contactsView) {
		 this.requestTag=REQUEST_TAG+System.currentTimeMillis();
		this.visitorsModel = new RefereeVisitorsModelImpl();
		this.contactsView = contactsView;
	}
	@Override
	public void loadRefereeContacts(User user, int nPage, int nSpage,
									 String contactName, String phone,String employee_id) {
		contactsView.showLoading();
		visitorsModel.loadCustomerContacts(requestTag,user, nPage, nSpage, contactName, phone,employee_id,this);
	}
	@Override
	public void onLoadRefereeContactsSuccess(List<RefereeVisitor> visitors) {
		if(visitors != null){
			contactsView.doRefereeContactsLoadSuccess(visitors);
			contactsView.hideLoading();
		}
	}
	@Override
	public void onLoadRefereeContactsFailure(String errorMessage) {
		contactsView.showMessage(errorMessage);
	}
	@Override
	public void onDestroy() {
		DataRequest.getInstance().cancelRequestByTag(requestTag);
	}

}
