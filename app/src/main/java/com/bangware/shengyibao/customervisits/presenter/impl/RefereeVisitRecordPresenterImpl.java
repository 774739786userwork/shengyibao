package com.bangware.shengyibao.customervisits.presenter.impl;

import com.bangware.shengyibao.customervisits.model.CustomerVisitRecordModel;
import com.bangware.shengyibao.customervisits.model.entity.RefereeBean;
import com.bangware.shengyibao.customervisits.model.impl.CustomerVisitRecordImpl;
import com.bangware.shengyibao.customervisits.presenter.OnRefereeVisitRecoedListener;
import com.bangware.shengyibao.customervisits.presenter.RefereeVisitRecordPresenter;
import com.bangware.shengyibao.customervisits.view.RefereeVisitRecordView;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;

import java.util.List;

/**
 * 推荐人数据实现类
 * Created by bangware on 2016/12/28.
 */

public class RefereeVisitRecordPresenterImpl implements RefereeVisitRecordPresenter,OnRefereeVisitRecoedListener{
    public static final String REQUEST_TAG = "RefereeVisitRecord";
    private String requestTag;
    private CustomerVisitRecordModel recordModel;
    private RefereeVisitRecordView recordView;

    public RefereeVisitRecordPresenterImpl(RefereeVisitRecordView recordView) {
        this.requestTag = REQUEST_TAG+System.currentTimeMillis();
        this.recordModel = new CustomerVisitRecordImpl();
        this.recordView = recordView;
    }
    @Override
    public void onLoadDataSuccess(List<RefereeBean> refereeBeanList) {
        if (refereeBeanList.size() > 0){
            recordView.addRefereeVisitReocrd(refereeBeanList);
        }
        recordView.hideLoading();
    }

    @Override
    public void onLoadDataFailure(String errorMessage) {
        recordView.hideLoading();
        recordView.loadDataFailure(errorMessage);
    }

    @Override
    public void addRefereeRecord(String show_type, User user, int nPage, int nSpage) {
        recordView.showLoading();
        recordModel.loadRefereeVisitRecord(requestTag, user,show_type,nPage,nSpage,this);
    }

    @Override
    public void destory() {
        DataRequest.getInstance().cancelRequestByTag(requestTag);
    }
}
