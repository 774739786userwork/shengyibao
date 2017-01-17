package com.bangware.shengyibao.refereevisit.model.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/26.
 */

public class RefereeVisitor implements Serializable {
    private String id;
    private String mobile;
    private String name;
    private String relation;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
