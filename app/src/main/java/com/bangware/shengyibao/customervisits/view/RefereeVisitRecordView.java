package com.bangware.shengyibao.customervisits.view;

import com.bangware.shengyibao.activity.BaseView;
import com.bangware.shengyibao.customervisits.model.entity.RefereeBean;

import java.util.List;

/**
 * 推荐人拜访
 * Created by bangware on 2016/12/28.
 */

public interface RefereeVisitRecordView extends BaseView {
    void addRefereeVisitReocrd(List<RefereeBean> visitRefereeList);

    void loadDataFailure(String failureMessage);
}
