package com.bangware.shengyibao.refereevisit.presenter.impl;

import com.bangware.shengyibao.refereevisit.model.NewRefereeModel;
import com.bangware.shengyibao.refereevisit.model.impl.NewRefereeModelImpl;
import com.bangware.shengyibao.refereevisit.presenter.NewRefereeVisitorPresenter;
import com.bangware.shengyibao.refereevisit.presenter.OnNewRefereeLisenter;
import com.bangware.shengyibao.refereevisit.view.NewRefereeVisitorsView;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;

/**
 * Created by Administrator on 2016/12/26.
 */

public class NewRefereePresenterImpl implements NewRefereeVisitorPresenter,OnNewRefereeLisenter {
    public static final String REQUEST_TAG = "NewRefereeVisitor";
    private String requestTag;
    private NewRefereeModel RefereeModel;
    private NewRefereeVisitorsView refereeVisitorsView;

    public NewRefereePresenterImpl( NewRefereeVisitorsView refereeVisitorsView) {
        this.requestTag = REQUEST_TAG+System.currentTimeMillis();
        this.refereeVisitorsView = refereeVisitorsView;
        RefereeModel = new NewRefereeModelImpl();
    }


    @Override
    public void addRefereeVisitors(User user, String name, String mobile1, String relation) {
       RefereeModel.addReferee(requestTag,user,name,mobile1,relation,this);
    }

    @Override
    public void onSaveSuccess() {
        refereeVisitorsView.save();
    }

    @Override
    public void destory() {
        DataRequest.getInstance().cancelRequestByTag(requestTag);
    }

    @Override
    public void onAddRefereeSuccess(String successMessage) {
        refereeVisitorsView.hideLoading();
        refereeVisitorsView.addRefereeSuccessMessage(successMessage);

    }

    @Override
    public void onAddRefereeFailure(String errorMessage) {
       refereeVisitorsView.showMessage(errorMessage);
    }
}
