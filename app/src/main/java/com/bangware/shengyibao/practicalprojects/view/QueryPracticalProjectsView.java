package com.bangware.shengyibao.practicalprojects.view;

import com.bangware.shengyibao.activity.BaseView;
import com.bangware.shengyibao.practicalprojects.model.entity.MyBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/28.
 */

public interface QueryPracticalProjectsView extends BaseView{
    void doLoadQueryPracticalSuccess(List<MyBean> Bean);
}
