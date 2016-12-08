package com.bangware.shengyibao.manager.shoptypeflowlayout.presenter;

import com.bangware.shengyibao.manager.shoptypeflowlayout.flowlayout.Flow;
import com.bangware.shengyibao.manager.shoptypeflowlayout.model.entity.FlowEntity;

import java.util.List;

/**
 * Created by bangware on 2016/10/12.
 */
public interface ShopFlowListener {
    void onLoadGroupSuccess(List<Flow> dataList);
    void onLoadGroupFailure(String errorMessage);
}
