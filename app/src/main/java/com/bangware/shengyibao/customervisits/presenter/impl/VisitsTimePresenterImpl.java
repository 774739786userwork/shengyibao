package com.bangware.shengyibao.customervisits.presenter.impl;

import com.bangware.shengyibao.customervisits.model.CustomerVisitRecordModel;
import com.bangware.shengyibao.customervisits.model.entity.VisitRecordBean;
import com.bangware.shengyibao.customervisits.model.impl.CustomerVisitRecordImpl;
import com.bangware.shengyibao.customervisits.presenter.OnCustomerVisitRecordListener;
import com.bangware.shengyibao.customervisits.presenter.VisitsTimePresenter;
import com.bangware.shengyibao.customervisits.view.CustomerVisitRecordView;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;

import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */

public class VisitsTimePresenterImpl implements VisitsTimePresenter,OnCustomerVisitRecordListener{
    public static final String REQUEST_TAG = "CustomerVisitRecord";
    private String requestTag;
    private CustomerVisitRecordModel recordModel;
    private CustomerVisitRecordView recordView;

    public VisitsTimePresenterImpl( CustomerVisitRecordView recordView) {
        this.requestTag = REQUEST_TAG+System.currentTimeMillis();
        this.recordModel = new CustomerVisitRecordImpl();
        this.recordView = recordView;
    }

    @Override
    public void onLoadDataSuccess(List<VisitRecordBean> recordList) {
        if (recordList.size() > 0){
            recordView.addCustomeVisitReocrd(recordList);
        }
        recordView.hideLoading();
    }

    @Override
    public void onLoadDataFailure(String errorMessage) {
        recordView.hideLoading();
        recordView.loadDataFailure(errorMessage);
    }

    @Override
    public void addVisitTimeRecord(User user, int nPage, int nSpage, String begin_date, String end_date, String employee_id) {
        recordView.showLoading();
        recordModel.loadVisitTimeRecord(requestTag,user,nPage,nSpage,begin_date,end_date,employee_id,this);
    }

    @Override
    public void destory() {
        DataRequest.getInstance().cancelRequestByTag(requestTag);
    }
}
