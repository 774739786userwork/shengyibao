package com.bangware.shengyibao.daysaleaccount.presenter.impl;

import android.util.Log;

import com.bangware.shengyibao.daysaleaccount.model.SaleAccountModel;
import com.bangware.shengyibao.daysaleaccount.model.entity.ChoicePersonBean;
import com.bangware.shengyibao.daysaleaccount.model.impl.SaleAccountModelImpl;
import com.bangware.shengyibao.daysaleaccount.presenter.OnSalerPersonListener;
import com.bangware.shengyibao.daysaleaccount.presenter.SalerPersonPresenter;
import com.bangware.shengyibao.daysaleaccount.view.SalerPersonView;
import com.bangware.shengyibao.ladingbilling.model.entity.LadingbillingQuery;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;

import java.util.List;

import static com.wch.wchusbdriver.CH34xAndroidDriver.TAG;

/**
 * Created by bangware on 2016/12/15.
 */

public class SalerPersonPresenterImpl implements SalerPersonPresenter,OnSalerPersonListener{
    public  static final String  REQUEST_TAG = "ChoiceSalerPerson";
    private SaleAccountModel model;
    private SalerPersonView view;
    private String requestTag;

    public SalerPersonPresenterImpl(
            SalerPersonView view) {
        super();
        this.model = new SaleAccountModelImpl();
        this.view = view;
        this.requestTag = REQUEST_TAG+System.currentTimeMillis();
    }
    @Override
    public void onLoadSalesPersonSuccess(List<ChoicePersonBean> personBeenList) {
        view.hideLoading();
        view.doLoadSalePersonData(personBeenList);
    }


    @Override
    public void onLoadSalesPersonFailure(String errorMessage) {
        view.showMessage(errorMessage);
        view.hideLoading();
    }

    @Override
    public void loadSalerPersonData(User user) {
        view.showLoading();
        model.onloadSalerPerson(requestTag,user,this);
    }

    @Override
    public void ondeStory() {
        DataRequest.getInstance().cancelRequestByTag(requestTag);
    }
}
