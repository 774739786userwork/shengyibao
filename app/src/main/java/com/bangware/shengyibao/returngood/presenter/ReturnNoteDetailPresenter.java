package com.bangware.shengyibao.returngood.presenter;

public interface ReturnNoteDetailPresenter {
	/**
	 * 加载退货单产品数据
	 */
	public void doLoadReturnDetail(String returnNoteId);
	
	/**
	 * 作废产品单据
	 */
	public void doAbort(String returnNoteId);
	
	public void destroy();
}
