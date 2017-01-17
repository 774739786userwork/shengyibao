package com.bangware.shengyibao.customervisits.presenter.impl;

import com.bangware.shengyibao.customervisits.model.VisitsCountModel;
import com.bangware.shengyibao.customervisits.model.entity.VisitsCountBean;
import com.bangware.shengyibao.customervisits.model.impl.VisitsCountModelImpl;
import com.bangware.shengyibao.customervisits.presenter.OnVisitsCountListener;
import com.bangware.shengyibao.customervisits.presenter.VisitsCountPersenter;
import com.bangware.shengyibao.customervisits.view.VisitsCountView;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;

import java.util.List;

/**
 * Created by Administrator on 2016/12/21.
 * 客户拜访统计界面
 */

public class VisitsCountPersenterImpl implements VisitsCountPersenter,OnVisitsCountListener {
    private static final String REQUEST_TAG="VisitsCount";
    private String requestTag;
    private VisitsCountModel visitsCountModel;
    private VisitsCountView countView;

    public VisitsCountPersenterImpl(VisitsCountView countView) {
        this.requestTag=REQUEST_TAG;
        this.countView = countView;
        this.visitsCountModel=new VisitsCountModelImpl();

    }


    @Override
    public void queryVisitCount(User user,String employee_ids, String begin_date, String end_date) {
        countView.hideLoading();
        visitsCountModel.loadVisitCount(requestTag,user,employee_ids,begin_date,end_date,this);
    }

    @Override
    public void destory() {
        DataRequest.getInstance().cancelRequestByTag(requestTag);
    }

    @Override
    public void onLoadDataSuccess(List<VisitsCountBean> countBeanList) {
       if (countBeanList.size()>0)
       {
          countView.addCustomeVisitCount(countBeanList);
       }
        countView.hideLoading();
    }

    @Override
    public void onLoadDataFailure(String errorMessage) {
         countView.hideLoading();
         countView.loadDataFailure(errorMessage);
    }
}
