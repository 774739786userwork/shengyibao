package com.bangware.shengyibao.ladingbilling.presenter;

import com.bangware.shengyibao.user.model.entity.User;

/**
 * Created by Administrator on 2017/1/20.
 */

public interface QueryDisburdenPresenter {

    void OnQueryDisburden(User user);
    void CancellationDisburden(User user,String disburden_id);
    void destroy();
}
