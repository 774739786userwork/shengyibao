package com.bangware.shengyibao.customervisits.presenter.impl;

import com.bangware.shengyibao.customervisits.model.CustomerVisitRecordModel;
import com.bangware.shengyibao.customervisits.model.entity.VisitRecordBean;
import com.bangware.shengyibao.customervisits.model.impl.CustomerVisitRecordImpl;
import com.bangware.shengyibao.customervisits.presenter.CustomerVisitStatusPresenter;
import com.bangware.shengyibao.customervisits.presenter.OnCustomerVisitStatusListener;
import com.bangware.shengyibao.customervisits.view.CustomerVisitStatusView;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.volley.DataRequest;

/**
 * Created by bangware on 2016/12/13.
 */

public class CustomerVisitStatusPresenterImpl implements CustomerVisitStatusPresenter,OnCustomerVisitStatusListener {
    public static final String REQUEST_TAG = "CustomerVisit";
    private String requestTag;
    private CustomerVisitRecordModel recordModel;
    private CustomerVisitStatusView statusView;

    public CustomerVisitStatusPresenterImpl(CustomerVisitStatusView statusView) {
        this.requestTag = REQUEST_TAG+System.currentTimeMillis();
        this.recordModel = new CustomerVisitRecordImpl();
        this.statusView = statusView;
    }
    @Override
    public void queryVisitStatus(User user,String customerId) {
        statusView.showLoading();
        recordModel.loadVisitStatus(requestTag, user,customerId,this);
    }

    @Override
    public void destory() {
        DataRequest.getInstance().cancelRequestByTag(requestTag);
    }

    @Override
    public void onLoadStatus(VisitRecordBean status) {
        if (status != null){
            statusView.addCustomeVisitStatus(status);
        }
        statusView.hideLoading();
    }

    @Override
    public void onLoadDataFailure(String errorMessage) {
        statusView.hideLoading();
        statusView.loadDataFailure(errorMessage);
    }
}
