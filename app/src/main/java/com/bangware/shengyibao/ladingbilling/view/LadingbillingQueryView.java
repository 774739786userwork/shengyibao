package com.bangware.shengyibao.ladingbilling.view;

import com.bangware.shengyibao.ladingbilling.model.entity.CarBean;
import com.bangware.shengyibao.ladingbilling.model.entity.LadingbillingQuery;

import java.util.List;


public interface LadingbillingQueryView {
	void showDialog();
	void hideDialog();
	void addLadingbillingData(List<LadingbillingQuery> ladingbillingList);
	void loadCarBean(List<CarBean> carBeanList);
	void showFailureMsg(String errorMessage);
}
