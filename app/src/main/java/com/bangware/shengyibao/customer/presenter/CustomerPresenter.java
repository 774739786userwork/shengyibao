package com.bangware.shengyibao.customer.presenter;
/**
 * presenter 接口控制器，用来协调model和view
 * @author ccssll
 *
 */
public interface CustomerPresenter {
	//客户列表数据加载
	void loadCustomerData(int nPage,int nSpage,String xzqh, String phone,String shopName,String latitude,String longitude,String nearBy,String type,String compositor);
	
	void destroy();
}
