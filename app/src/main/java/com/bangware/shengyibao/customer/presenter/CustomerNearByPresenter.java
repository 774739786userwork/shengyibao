package com.bangware.shengyibao.customer.presenter;


/**
 * Created by ccssll on 2016/8/3.
 */
public interface CustomerNearByPresenter {
    void loadNearByCustomerData(int nPage, int nSpage,String latitude,String longitude);
    void destroy();
}
