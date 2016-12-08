package com.bangware.shengyibao.ladingbilling.presenter;

public interface LadingbillingPresenter {
	void loadLadingBilling(String begin_date, String end_date, int nPage, int nSpage);
	void destroy();
}
