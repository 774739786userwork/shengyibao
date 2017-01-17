package com.bangware.shengyibao.customervisits.model.entity;

import com.bangware.shengyibao.customer.model.entity.Customer;

import java.io.Serializable;

/**
 * 推荐人实体类
 * Created by bangware on 2016/12/28.
 */

public class RefereeBean implements Serializable {
    private static final long serialVersionUID = -4033663599091826823L;
    private String refereeId;
    private String refereeName;
    private String refereeMobile;
    private int visitId;//拜访ID
    private String visitTime;//拜访时间
    private String visitContent;//拜访内容
    private String contentType;//区分语音或文字
    private String issueType;//出单状态
    private String visitVoiceTime;//语音录制时间
    private Customer customer;//客户信息
    private OwnerBean ownerBean;//业主信息
    private int totalPage;//总数多少条

    public String getRefereeId() {
        return refereeId;
    }

    public void setRefereeId(String refereeId) {
        this.refereeId = refereeId;
    }

    public String getRefereeName() {
        return refereeName;
    }

    public void setRefereeName(String refereeName) {
        this.refereeName = refereeName;
    }

    public String getRefereeMobile() {
        return refereeMobile;
    }

    public void setRefereeMobile(String refereeMobile) {
        this.refereeMobile = refereeMobile;
    }

    public int getVisitId() {
        return visitId;
    }

    public void setVisitId(int visitId) {
        this.visitId = visitId;
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getVisitVoiceTime() {
        return visitVoiceTime;
    }

    public void setVisitVoiceTime(String visitVoiceTime) {
        this.visitVoiceTime = visitVoiceTime;
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

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
