package com.bangware.shengyibao.practicalprojects.model;

import com.bangware.shengyibao.practicalprojects.presenter.OnQueryPracticalListener;
import com.bangware.shengyibao.user.model.entity.User;

/**
 * Created by Administrator on 2016/12/28.
 */

public interface QueryPracticalProjectsModel {
     void loadPracticalProjects(String requestTag,User user, int nPage, int nSpage,String content, OnQueryPracticalListener practicalListener);
}
