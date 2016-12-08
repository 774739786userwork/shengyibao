package com.bangware.shengyibao.customer.view;

import com.bangware.shengyibao.activity.BaseView;
import com.bangware.shengyibao.customer.model.entity.Customer;
import com.bangware.shengyibao.customer.model.entity.CustomerTypes;

import java.util.List;



/**
 * 主页月开单客户、未开单客户及送货客户的数据请求
 * @author ccssll
 *
 */
public interface CustomerBillingMonthSalerRecordView extends BaseView {
	
	void queryCustomerSalerRecord(List<CustomerTypes> customerTypes);
	void showLoadFailureMsg(String errorMessage);

}
