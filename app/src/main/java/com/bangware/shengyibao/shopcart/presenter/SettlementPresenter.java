package com.bangware.shengyibao.shopcart.presenter;


import com.bangware.shengyibao.deliverynote.model.entity.DeliveryNote;

public interface SettlementPresenter {

	/**
	 * 送货单保存
	 * @param deliveryNote
	 */
	public void doSave(DeliveryNote deliveryNote);
	
	public void destroy();
}
