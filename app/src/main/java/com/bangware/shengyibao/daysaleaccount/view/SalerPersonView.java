package com.bangware.shengyibao.daysaleaccount.view;

import com.bangware.shengyibao.activity.BaseView;
import com.bangware.shengyibao.daysaleaccount.model.entity.ChoicePersonBean;

import java.util.List;


/**
 * Created by bangware on 2016/12/15.Ch
 */

public interface SalerPersonView extends BaseView{
    void doLoadSalePersonData(List<ChoicePersonBean> personBeanList);
}
