package com.bangware.shengyibao.customervisits.presenter;

import com.bangware.shengyibao.customervisits.model.entity.VisitsCountBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/21.
 */

public interface OnVisitsCountListener {
    void onLoadDataSuccess(List<VisitsCountBean> countList);
    void onLoadDataFailure(String errorMessage);
}
