package com.bangware.shengyibao.customervisits.model;

/**
 * Created by bangware on 2016/11/26.
 */

import com.bangware.shengyibao.customercontacts.presenter.OnCustomerContactsListener;
import com.bangware.shengyibao.customervisits.presenter.OnCustomerVisitRecordListener;
import com.bangware.shengyibao.customervisits.presenter.OnCustomerVisitStatusListener;
import com.bangware.shengyibao.customervisits.presenter.OnRefereeVisitRecoedListener;
import com.bangware.shengyibao.customervisits.presenter.OnVisitCustomerContactsListener;
import com.bangware.shengyibao.user.model.entity.User;

/**
 * 拜访记录数据接口模型
 */
public interface CustomerVisitRecordModel {
    //获取客户拜访记录请求
    void loadVisitRecord(String requestTag, User user, String show_type, int nPage, int nSpage, OnCustomerVisitRecordListener visitRecordListener);

    //获取推荐人拜访记录请求
    void loadRefereeVisitRecord(String requestTag, User user, String show_type, int nPage, int nSpage, OnRefereeVisitRecoedListener refereeVisitRecoedListener);

    //获取拜访过的客户的联系人请求
    void loadVisitCustomerContacts(String requestTag, User user, int nPage, int nSpage, String phone,String contactName,String employee_id,OnVisitCustomerContactsListener customerContactsListener);

    //获取拜访状态
    void loadVisitStatus(String requestTag, User user, String customerId, OnCustomerVisitStatusListener visitStatusListener);

    void loadVisitTimeRecord(String requestTag, User user, int nPage, int nSpage, String begin_date,String end_date,String employee_id, OnCustomerVisitRecordListener visitRecordListener);
}
