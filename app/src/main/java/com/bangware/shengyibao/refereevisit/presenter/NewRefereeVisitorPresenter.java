package com.bangware.shengyibao.refereevisit.presenter;

import com.bangware.shengyibao.user.model.entity.User;

/**
 * Created by Administrator on 2016/12/26.
 */

public interface NewRefereeVisitorPresenter {
    void addRefereeVisitors(User user,String name, String mobile1,String relation);

    void destory();
}
