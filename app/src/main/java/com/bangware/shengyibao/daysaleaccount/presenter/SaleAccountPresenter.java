package com.bangware.shengyibao.daysaleaccount.presenter;


/**
 * Created by ccssll on 2016/8/11.
 */
public interface SaleAccountPresenter {
    void loadSalesAccountData(String begin_date, String end_date, int nPage, int nSpage);
    void destroy();
}
