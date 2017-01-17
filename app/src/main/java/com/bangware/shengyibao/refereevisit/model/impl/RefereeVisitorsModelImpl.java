package com.bangware.shengyibao.refereevisit.model.impl;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.customer.model.entity.Contacts;
import com.bangware.shengyibao.customercontacts.CustomerContactsJosnUtils;
import com.bangware.shengyibao.refereevisit.RefereeContactsJosnUtils;
import com.bangware.shengyibao.refereevisit.model.RefereeVisitorsModel;
import com.bangware.shengyibao.refereevisit.model.entity.RefereeVisitor;
import com.bangware.shengyibao.refereevisit.presenter.OnRefereeContactsListener;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27.
 */

public class RefereeVisitorsModelImpl implements RefereeVisitorsModel {
    @Override
    public void loadCustomerContacts(String requestTag, User user, int nPage, int nSpage, String phone, String contactName, String employee_id, final OnRefereeContactsListener contactsListener) {
        String referee_contacts_url;
        try {
            referee_contacts_url = Model.QUERY_REFEREE_URL+"&token="+user.getLogin_token()+"&page="+nPage+"&rows="+nSpage
                    +"&mobile="+phone+"&content="+ URLEncoder.encode(contactName, "UTF-8")+"&employee_id="+employee_id;
            Log.e("TGA",referee_contacts_url);
            DataRequest.getInstance().newJsonObjectGetRequest(requestTag, referee_contacts_url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jsonObject) {
                    if(jsonObject != null){

                        List<RefereeVisitor> contacts = RefereeContactsJosnUtils.getRefereeVisitors(jsonObject.toString());

                        contactsListener.onLoadRefereeContactsSuccess(contacts);
                        Log.d("TAG", "query11111  "+contacts.size());
                    }else{
                        contactsListener.onLoadRefereeContactsFailure("数据传输失败！");
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    contactsListener.onLoadRefereeContactsFailure("请求失败，服务器异常......");
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
