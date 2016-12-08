package com.bangware.shengyibao.customer.presenter;

public interface CustomerBillingMonthPresenter {
	//客户月开单、未开单数据加载
	void loadCustomerBillingMonthData(int nPage,int nSpage,int show_type,String compositor);
	
	void destroy();
}
