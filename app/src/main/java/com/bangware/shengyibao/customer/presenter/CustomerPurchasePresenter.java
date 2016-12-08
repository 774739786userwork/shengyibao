package com.bangware.shengyibao.customer.presenter;


public interface CustomerPurchasePresenter {
	//客户进货记录
	void queryCustomerPurchaseData(String customer_id,int nPage,int nSpage,String begin_date,String end_date);
	
	void destory();
}
