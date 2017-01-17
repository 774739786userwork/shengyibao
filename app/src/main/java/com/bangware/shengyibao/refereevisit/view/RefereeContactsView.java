package com.bangware.shengyibao.refereevisit.view;

import com.bangware.shengyibao.activity.BaseView;
import com.bangware.shengyibao.customer.model.entity.Contacts;
import com.bangware.shengyibao.refereevisit.model.entity.RefereeVisitor;

import java.util.List;


public interface RefereeContactsView extends BaseView {
	 
	 void doRefereeContactsLoadSuccess(List<RefereeVisitor> visitors);
}
