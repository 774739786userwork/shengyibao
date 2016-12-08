package com.bangware.shengyibao.manager.shoptypeflowlayout.view;

import com.bangware.shengyibao.activity.BaseView;
import com.bangware.shengyibao.manager.shoptypeflowlayout.flowlayout.Flow;
import com.bangware.shengyibao.manager.shoptypeflowlayout.model.entity.FlowEntity;

import java.util.List;

/**
 * Created by bangware on 2016/10/12.
 */
public interface ShopTypeFlowView extends BaseView{
    void doShopTypeLoadSuccess(List<Flow> groupDataList);
}
