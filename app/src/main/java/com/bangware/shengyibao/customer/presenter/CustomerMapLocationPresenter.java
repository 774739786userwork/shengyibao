package com.bangware.shengyibao.customer.presenter;

public interface CustomerMapLocationPresenter {
	//客户地理位置标注
	void loadMapLocation(String customerId,String longitude,String latitude,String location_address);
		
	void destory();
}
