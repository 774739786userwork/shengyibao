package com.bangware.shengyibao.customervisits.view;

import com.bangware.shengyibao.activity.BaseView;
import com.bangware.shengyibao.customervisits.model.entity.VisitRecordBean;

/**
 * Created by bangware on 2016/12/13.
 */

public interface CustomerVisitStatusView extends BaseView {
    void addCustomeVisitStatus(VisitRecordBean visitStatus);

    void loadDataFailure(String failureMessage);
}
