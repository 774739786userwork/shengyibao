package com.bangware.shengyibao.returngood.presenter;

public interface ReturngoodPresenter {
	void loadreturnGood(String begin_date, String end_date, int nPage, int nSpage);
	void destroy();
}
