package com.bangware.shengyibao.refereevisit.view;

import com.bangware.shengyibao.activity.BaseView;

/**
 * Created by Administrator on 2016/12/26.
 */

public interface NewRefereeVisitorsView extends BaseView{
    void addRefereeSuccessMessage(String successMessage);
    void save();
    void showMessage(String message);
}
