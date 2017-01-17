package com.bangware.shengyibao.customervisits.presenter;

import com.bangware.shengyibao.customervisits.model.entity.RefereeBean;

import java.util.List;

/**
 * 推荐人拜访
 * Created by bangware on 2016/12/28.
 */

public interface OnRefereeVisitRecoedListener {
    void onLoadDataSuccess(List<RefereeBean> refereeBeanList);
    void onLoadDataFailure(String errorMessage);
}
