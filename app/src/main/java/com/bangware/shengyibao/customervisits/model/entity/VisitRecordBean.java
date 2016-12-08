package com.bangware.shengyibao.customervisits.model.entity;


import com.bangware.shengyibao.customer.model.entity.Customer;
import com.bangware.shengyibao.customer.model.entity.CustomerImage;

import java.util.List;

/**
 * Created by bangware on 2016/11/26.
 */

public class VisitRecordBean {
    private int visitId;//拜访ID
    private String visitTime;//拜访时间
    private String visitContent;//拜访内容
    private String contentType;//区分语音或文字
    private String visitType;//拜访类型
    private String issueType;//出单状态
    private String visitVoiceTime;//语音录制时间
    private Customer customer;//客户信息
    private int totalPage;//总数多少条

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

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
