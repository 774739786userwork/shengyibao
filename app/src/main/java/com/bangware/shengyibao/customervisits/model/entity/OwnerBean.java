package com.bangware.shengyibao.customervisits.model.entity;

import java.io.Serializable;

/**
 * 业主信息实体类
 * Created by bangware on 2016/12/27.
 */

public class OwnerBean implements Serializable{

    private static final long serialVersionUID = -5033663599091826822L;
    private String ownerId;
    private String ownerName;
    private String ownerMobile;
    private Double acreage;//面积
    private Double unitPrice;//单价

    private boolean is_owner = false;//是否是业主

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerMobile() {
        return ownerMobile;
    }

    public void setOwnerMobile(String ownerMobile) {
        this.ownerMobile = ownerMobile;
    }

    public Double getAcreage() {
        return acreage;
    }

    public void setAcreage(Double acreage) {
        this.acreage = acreage;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public boolean is_owner() {
        return is_owner;
    }

    public void setIs_owner(boolean is_owner) {
        this.is_owner = is_owner;
    }
}
