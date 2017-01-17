package com.bangware.shengyibao.customervisits.view;

import com.bangware.shengyibao.activity.BaseView;
import com.bangware.shengyibao.customervisits.model.entity.VisitsCountBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/21.
 */

public interface VisitsCountView extends BaseView{
    void addCustomeVisitCount(List<VisitsCountBean> visitsCountBeanList);

    void loadDataFailure(String failureMessage);
}
