package com.bangware.shengyibao.customer.presenter;

import com.bangware.shengyibao.customer.model.entity.Customer;
import com.bangware.shengyibao.customer.model.entity.CustomerTypes;

import java.util.List;

/**
 * 主页月开单客户查询响应事件接口
 * @author ccssll
 *
 */
public interface OnMonthCustomerBillingRecordListener {
	void onLoadDataSuccess(List<CustomerTypes> customerTypes);
	void onLoadDataFailure(String errorMessage);
}
