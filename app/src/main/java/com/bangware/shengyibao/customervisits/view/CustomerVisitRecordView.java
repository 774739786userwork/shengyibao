package com.bangware.shengyibao.customervisits.view;

import com.bangware.shengyibao.activity.BaseView;
import com.bangware.shengyibao.customervisits.model.entity.VisitRecordBean;

import java.util.List;

/**
 * 拜访记录数据加载View
 * Created by bangware on 2016/12/1.
 */

public interface CustomerVisitRecordView extends BaseView{
    void addCustomeVisitReocrd(List<VisitRecordBean> visitRecordBeanList);

    void loadDataFailure(String failureMessage);
}
