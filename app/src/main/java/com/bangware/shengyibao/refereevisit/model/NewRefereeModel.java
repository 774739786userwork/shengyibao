package com.bangware.shengyibao.refereevisit.model;

import com.bangware.shengyibao.refereevisit.presenter.OnNewRefereeLisenter;
import com.bangware.shengyibao.user.model.entity.User;

/**
 * Created by Administrator on 2016/12/26.
 */

public interface NewRefereeModel {
   void addReferee(String requesTag, User user, String name, String mobile1, String relation, OnNewRefereeLisenter refereeLisenter);
}
