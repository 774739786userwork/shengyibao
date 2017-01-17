package com.bangware.shengyibao.customervisits;
/**
 * Created by Administrator on 2016/11/25.
 */


import com.bangware.shengyibao.customer.model.entity.Contacts;
import com.bangware.shengyibao.customer.model.entity.Customer;
import com.bangware.shengyibao.customer.model.entity.CustomerImage;
import com.bangware.shengyibao.customervisits.model.entity.OwnerBean;
import com.bangware.shengyibao.customervisits.model.entity.RefereeBean;
import com.bangware.shengyibao.customervisits.model.entity.VisitRecordBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CustomerVisitsUtils {
    /**
     * 客户拜访数据json解析
     */
    public static List<VisitRecordBean> getCustomersList(String str){
        List<VisitRecordBean> datalist = new ArrayList<VisitRecordBean>();
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            VisitRecordBean visitRecordBean = null;
            Customer customer = null;
            CustomerImage customerImage = null;
            Contacts contact = null;
            OwnerBean ownerBean = null;
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject visitObj = jsonArray.getJSONObject(i);
                visitRecordBean = new VisitRecordBean();
                visitRecordBean.setVisitId(visitObj.getInt("id"));
                visitRecordBean.setVisitTime(visitObj.getString("visit_date"));
                visitRecordBean.setContentType(visitObj.getString("content_type"));
                visitRecordBean.setVisitContent(visitObj.getString("content"));
                visitRecordBean.setVisitType(visitObj.getString("visit_status"));
                visitRecordBean.setIssueType(visitObj.getString("visit_type"));
                visitRecordBean.setCustomer_telephone(visitObj.getString("customer_telephone"));
                if (!visitObj.isNull("customer_level")){
                    visitRecordBean.setCustomerLevel(visitObj.getString("customer_level"));
                }else {
                    visitRecordBean.setCustomerLevel("");
                }

                ownerBean = new OwnerBean();
                ownerBean.setIs_owner(visitObj.getBoolean("is_owner"));
                if (ownerBean.is_owner() == true){
                    JSONObject ownerObj = visitObj.getJSONObject("owner");
                    ownerBean.setOwnerId(ownerObj.getString("id"));
                    ownerBean.setOwnerName(ownerObj.getString("owner_name"));
                    ownerBean.setOwnerMobile(ownerObj.getString("owner_phone"));

                    if (!ownerObj.isNull("acreage") && !ownerObj.isNull("unit_price")){
                        ownerBean.setAcreage(ownerObj.getDouble("acreage"));
                        ownerBean.setUnitPrice(ownerObj.getDouble("unit_price"));
                    }else {
                        ownerBean.setAcreage(0.0);
                        ownerBean.setUnitPrice(0.0);
                    }
                }

                visitRecordBean.setCustomerLevelRemark(visitObj.getString("customer_level_remark"));
                visitRecordBean.setVisitVoiceTime(visitObj.getString("audio_duration"));
                visitRecordBean.setTotalPage(jsonObject.getInt("total"));

                customer = new Customer();
                customer.setId(visitObj.getString("customer_id"));
                customer.setName(visitObj.getString("customer_name"));
                customer.setAddress(visitObj.getString("customer_address"));

                JSONArray imgArray = visitObj.getJSONArray("customer_images");
                for (int j = 0; j < imgArray.length(); j++){
                    JSONObject imgObj = imgArray.getJSONObject(j);
                    customerImage = new CustomerImage();
                    customerImage.setImg_id(imgObj.getString("id"));
                    customerImage.setImg_url(imgObj.getString("img_src"));

                    customer.addImages(customerImage);
                }
                JSONArray jsoncontactArray = visitObj.getJSONArray("contacts");
                for(int k = 0; k<jsoncontactArray.length();k++){
                    JSONObject kindObject = jsoncontactArray.getJSONObject(k);
                    contact = new Contacts();
                    contact.setName(kindObject.getString("name"));
                    contact.setMobile1(kindObject.getString("mobile1"));
                    contact.setMobile2(kindObject.getString("mobile2"));
                    contact.setPosition(kindObject.getString("position"));
                    customer.addContacts(contact);
                }
                visitRecordBean.setCustomer(customer);
                visitRecordBean.setOwnerBean(ownerBean);
                datalist.add(visitRecordBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datalist;
    }

    /**
     * 推荐人拜访数据json解析
     */
    public static List<RefereeBean> getRefereeDataList(String str){
        List<RefereeBean> datalist = new ArrayList<RefereeBean>();
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            RefereeBean refereeBean = null;
            Customer customer = null;
            CustomerImage customerImage = null;
            OwnerBean ownerBean = null;
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject visitObj = jsonArray.getJSONObject(i);
                refereeBean = new RefereeBean();
                refereeBean.setVisitId(visitObj.getInt("id"));
                refereeBean.setVisitTime(visitObj.getString("visit_date"));
                refereeBean.setContentType(visitObj.getString("content_type"));
                refereeBean.setVisitContent(visitObj.getString("content"));
                refereeBean.setIssueType(visitObj.getString("visit_type"));

                ownerBean = new OwnerBean();
                ownerBean.setIs_owner(visitObj.getBoolean("is_owner"));
                if (ownerBean.is_owner() == true){
                    JSONObject ownerObj = visitObj.getJSONObject("owner");
                    ownerBean.setOwnerId(ownerObj.getString("id"));
                    ownerBean.setOwnerName(ownerObj.getString("owner_name"));
                    ownerBean.setOwnerMobile(ownerObj.getString("owner_phone"));

                    if (!ownerObj.isNull("acreage") && !ownerObj.isNull("unit_price")){
                        ownerBean.setAcreage(ownerObj.getDouble("acreage"));
                        ownerBean.setUnitPrice(ownerObj.getDouble("unit_price"));
                    }else {
                        ownerBean.setAcreage(0.0);
                        ownerBean.setUnitPrice(0.0);
                    }
                }

                JSONObject refereeObj = visitObj.getJSONObject("referee_contacts");
                refereeBean.setRefereeId(refereeObj.getString("id"));
                refereeBean.setRefereeName(refereeObj.getString("name"));
                refereeBean.setRefereeMobile(refereeObj.getString("mobile"));

                refereeBean.setVisitVoiceTime(visitObj.getString("audio_duration"));
                refereeBean.setTotalPage(jsonObject.getInt("total"));

                customer = new Customer();
                JSONArray imgArray = visitObj.getJSONArray("customer_images");
                for (int j = 0; j < imgArray.length(); j++){
                    JSONObject imgObj = imgArray.getJSONObject(j);
                    customerImage = new CustomerImage();
                    customerImage.setImg_id(imgObj.getString("id"));
                    customerImage.setImg_url(imgObj.getString("img_src"));

                    customer.addImages(customerImage);
                }

                refereeBean.setCustomer(customer);
                refereeBean.setOwnerBean(ownerBean);
                datalist.add(refereeBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datalist;
    }

    //客户拜访类型及级别状态获取
    public static VisitRecordBean getVisitStatus(String key){
        VisitRecordBean vb = new VisitRecordBean();
        try {
            JSONObject jsonObject = new JSONObject(key);
            vb.setVisitType(jsonObject.getString("visit_status"));
            if (jsonObject.isNull("customer_level")){
                vb.setCustomerLevel("0");
            }else {
                vb.setCustomerLevel(jsonObject.getString("customer_level"));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return vb;
    }
}
