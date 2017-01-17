package com.bangware.shengyibao.ladingbilling.presenter.impl;

import com.bangware.shengyibao.ladingbilling.model.DisburenModel;
import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenGoods;
import com.bangware.shengyibao.ladingbilling.model.impl.DisburenModelImpl;
import com.bangware.shengyibao.ladingbilling.presenter.DisburdenPresenter;
import com.bangware.shengyibao.ladingbilling.presenter.OnDisburenSaveListener;
import com.bangware.shengyibao.ladingbilling.view.DisburdenView;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;

import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 */

public class DisburenPresentImpl implements DisburdenPresenter,OnDisburenSaveListener {
    public static final String REQUEST_TAG = "Disburen";
    private String requestTag;
    private DisburdenView disburdenView;
    private DisburenModel disburenModel;

    public DisburenPresentImpl( DisburdenView disburdenView) {
        this.requestTag = REQUEST_TAG+System.currentTimeMillis();
        this.disburdenView = disburdenView;
        this.disburenModel = new DisburenModelImpl();
    }

    @Override
    public void doDisburenSave(User user, List<DisburdenGoods> disburdenGoodsList,String carId) {
        disburdenView.showLoading("正在提交,请稍等！");
        disburenModel.save(user,requestTag,disburdenGoodsList,carId,this);
    }

    @Override
    public void destroy() {
            DataRequest.getInstance().cancelRequestByTag(requestTag);
    }

    @Override
    public void onSaveSuccess(List<DisburdenGoods> disburdenGoodsList) {
        disburdenView.hideLoading();
        disburdenView.doSaveDisburdenSuccess(disburdenGoodsList);
    }

    @Override
    public void onError(String message) {
        disburdenView.hideLoading();
        disburdenView.showMessage(message);
    }
}
