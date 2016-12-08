package com.bangware.shengyibao.manager.shoptypeflowlayout.model.entity;

import com.bangware.shengyibao.manager.shoptypeflowlayout.flowlayout.Flow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bangware on 2016/10/8.
 */
public class FlowEntity extends Flow {
    public String id;
    public String name;

    public FlowEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getFlowId() {
        return id;
    }

    @Override
    public String getFlowName() {
        return name;
    }
}
