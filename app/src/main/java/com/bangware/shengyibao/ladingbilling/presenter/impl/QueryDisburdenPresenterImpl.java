package com.bangware.shengyibao.ladingbilling.presenter.impl;

import com.bangware.shengyibao.ladingbilling.model.QueryDisburdenModel;
import com.bangware.shengyibao.ladingbilling.model.entity.QueryDisburdenBean;
import com.bangware.shengyibao.ladingbilling.model.impl.QueryDisburdenModelImpl;
import com.bangware.shengyibao.ladingbilling.presenter.OnQueryDisburenListener;
import com.bangware.shengyibao.ladingbilling.presenter.QueryDisburdenPresenter;
import com.bangware.shengyibao.ladingbilling.view.QueryDisburdenView;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;

import java.util.List;

/**
 * Created by Administrator on 2017/1/20.
 */

public class QueryDisburdenPresenterImpl implements QueryDisburdenPresenter,OnQueryDisburenListener {
    public static final String REQUEST_TAG = "QueryDisburden";
    private String requestTag;
    private QueryDisburdenView disburdenView;
    private QueryDisburdenModel model;

    public QueryDisburdenPresenterImpl(QueryDisburdenView disburdenView) {
        this.requestTag = REQUEST_TAG+System.currentTimeMillis();
        this.disburdenView = disburdenView;
        this.model = new QueryDisburdenModelImpl();
    }

    @Override
    public void OnQueryDisburden(User user) {
         disburdenView.showLoading();
        model.LoadQueryDisburden(requestTag,user,this);
    }

    @Override
    public void CancellationDisburden(User user, String disburden_id) {
        disburdenView.hideLoading();
        model.OnCancellationDisburden(requestTag,user,disburden_id,this);
    }


    @Override
    public void destroy() {
        DataRequest.getInstance().cancelRequestByTag(requestTag);
    }

    @Override
    public void onSaveSuccess(List<QueryDisburdenBean> queryDisburdenBeanList) {
         disburdenView.hideLoading();
        disburdenView.doLoadingQueryDisburden(queryDisburdenBeanList);
    }

    @Override
    public void onCancelSuccess(String message) {
        disburdenView.hideLoading();
        disburdenView.doCancellationDisburden(message);
    }

    @Override
    public void onError(String message) {
        disburdenView.hideLoading();
        disburdenView.showMessage(message);
    }
}
