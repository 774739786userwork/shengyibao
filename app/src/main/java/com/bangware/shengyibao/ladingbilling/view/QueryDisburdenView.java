package com.bangware.shengyibao.ladingbilling.view;

import com.bangware.shengyibao.activity.BaseView;
import com.bangware.shengyibao.ladingbilling.model.entity.QueryDisburdenBean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/20.
 */

public interface QueryDisburdenView extends BaseView {
    void doLoadingQueryDisburden(List<QueryDisburdenBean> queryDisburdenBeen);
    void doCancellationDisburden(String message);
    void destory();
}
