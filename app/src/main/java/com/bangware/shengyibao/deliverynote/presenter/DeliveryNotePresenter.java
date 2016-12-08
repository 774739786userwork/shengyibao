package com.bangware.shengyibao.deliverynote.presenter;


import com.bangware.shengyibao.deliverynote.model.entity.DeliveryNote;

/**
 * 送货单Presenter类
 * @author luming.tang
 *
 */
public interface DeliveryNotePresenter {
	/**
	 * 执行送货单查询
	 * @param salerId
	 * @param startDate
	 * @param endDate
	 */
	public void doLoad(String begin_date, String end_date, int nPage, int nSpage, int show_type);
	
	public DeliveryNote getDeliveryNote();
	
	/**
	 * 执行送货单保存
	 * @param deliveryNote
	 */
	public void doSave(DeliveryNote deliveryNote);
	
	/**
	 * 执行送货单作废
	 * @param deliveryNoteId
	 */
	public void doAbort(String deliveryNoteId);
	
	/**
	 * 加载送货单单个产品
	 */
	public void doLoadDetail(String deliveryNoteId);
	
	public void destroy();

}
