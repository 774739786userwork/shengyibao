package com.bangware.shengyibao.customervisits.presenter.impl;

import com.bangware.shengyibao.customervisits.model.CustomerVisitRecordModel;
import com.bangware.shengyibao.customervisits.model.entity.VisitRecordBean;
import com.bangware.shengyibao.customervisits.model.impl.CustomerVisitRecordImpl;
import com.bangware.shengyibao.customervisits.presenter.CustomerVisitRecordPresenter;
import com.bangware.shengyibao.customervisits.presenter.OnCustomerVisitRecordListener;
import com.bangware.shengyibao.customervisits.view.CustomerVisitRecordView;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.volley.DataRequest;

import java.util.List;

/**
 * Created by bangware on 2016/12/1.
 */

/**
 * 整个请求调用实现类
 */
public class CustomerVisitRecordPresenterImpl implements CustomerVisitRecordPresenter,OnCustomerVisitRecordListener{
    public static final String REQUEST_TAG = "CustomerVisitRecord";
    private String requestTag;
    private CustomerVisitRecordModel recordModel;
    private CustomerVisitRecordView recordView;

    public CustomerVisitRecordPresenterImpl(CustomerVisitRecordView recordView) {
        this.requestTag = REQUEST_TAG+System.currentTimeMillis();
        this.recordModel = new CustomerVisitRecordImpl();
        this.recordView = recordView;
    }

    //调用接口请求数据
    @Override
    public void addVisitRecord(String show_type, User user, int nPage, int nSpage) {
        recordView.showLoading();
        recordModel.loadVisitRecord(requestTag, user,show_type,nPage,nSpage,this);
    }

    //销毁请求队列
    @Override
    public void destory() {
        DataRequest.getInstance().cancelRequestByTag(requestTag);
    }

    //返回数据成功
    @Override
    public void onLoadDataSuccess(List<VisitRecordBean> recordList) {
        recordView.addCustomeVisitReocrd(recordList);
        recordView.hideLoading();
    }

    //返回数据失败
    @Override
    public void onLoadDataFailure(String errorMessage) {
        recordView.hideLoading();
        recordView.loadDataFailure(errorMessage);
    }
}
