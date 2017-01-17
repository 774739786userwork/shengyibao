package com.bangware.shengyibao.practicalprojects.presenter;

import com.bangware.shengyibao.user.model.entity.User;


/**
 * Created by Administrator on 2016/12/28.
 */

public interface QueryPracticalProjectsPresenter {
    void loadPracticalProjects( User user, int nPage, int nSpage, String content);
    void onDestroy();
}
