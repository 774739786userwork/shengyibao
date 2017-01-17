package com.bangware.shengyibao.customervisits.model.entity;


import com.bangware.shengyibao.customer.model.entity.Customer;

import java.io.Serializable;

/**
 * Created by bangware on 2016/11/26.
 */

public class VisitRecordBean implements Serializable{
    private static final long serialVersionUID = -4033663599091826822L;
    private int visitId;//拜访ID
    private String visitTime;//拜访时间
    private String visitContent;//拜访内容
    private String contentType;//区分语音或文字
    private String visitType;//拜访类型
    private String customer_telephone;
    private String issueType;//出单状态
    private String customerLevel;//客户等级
    private String customerLevelRemark;//客户等级备注
    private String visitVoiceTime;//语音录制时间
    private Customer customer;//客户信息
    private OwnerBean ownerBean;//业主信息
    private int totalPage;//总数多少条

    public String getCustomer_telephone() {
        return customer_telephone;
    }

    public void setCustomer_telephone(String customer_telephone) {
        this.customer_telephone = customer_telephone;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    public String getVisitContent() {
        return visitContent;
    }

    public void setVisitContent(String visitContent) {
        this.visitContent = visitContent;
    }

    public int getVisitId() {
        return visitId;
    }

    public void setVisitId(int visitId) {
        this.visitId = visitId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public OwnerBean getOwnerBean() {
        return ownerBean;
    }

    public void setOwnerBean(OwnerBean ownerBean) {
        this.ownerBean = ownerBean;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
    }

    public String getCustomerLevelRemark() {
        return customerLevelRemark;
    }

    public void setCustomerLevelRemark(String customerLevelRemark) {
        this.customerLevelRemark = customerLevelRemark;
    }

    public String getVisitVoiceTime() {
        return visitVoiceTime;
    }

    public void setVisitVoiceTime(String visitVoiceTime) {
        this.visitVoiceTime = visitVoiceTime;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
