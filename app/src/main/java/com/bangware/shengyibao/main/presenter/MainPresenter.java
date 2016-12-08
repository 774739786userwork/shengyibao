package com.bangware.shengyibao.main.presenter;


public interface MainPresenter {
	/**
	 * 加载首页数据
	 */
	public void loadToDaySummary();
	
	
	public void loadThisMonthSummary();
	
	public void destroy();
}
