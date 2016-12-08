package com.bangware.shengyibao.customervisits;
/**
 * Created by Administrator on 2016/11/25.
 */

import com.bangware.shengyibao.customer.model.entity.Customer;
import com.bangware.shengyibao.customer.model.entity.CustomerImage;
import com.bangware.shengyibao.customervisits.model.entity.VisitRecordBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户拜访数据json解析
 */
public class CustomerVisitsUtils {
    public static List<VisitRecordBean> getCustomersList(String str){
        List<VisitRecordBean> datalist = new ArrayList<VisitRecordBean>();
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            VisitRecordBean visitRecordBean = null;
            Customer customer = null;
            CustomerImage customerImage = null;

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject visitObj = jsonArray.getJSONObject(i);
                visitRecordBean = new VisitRecordBean();
                visitRecordBean.setVisitId(visitObj.getInt("id"));
                visitRecordBean.setVisitTime(visitObj.getString("visit_date"));
                visitRecordBean.setContentType(visitObj.getString("content_type"));
                visitRecordBean.setVisitContent(visitObj.getString("content"));
                visitRecordBean.setVisitType(visitObj.getString("visit_status"));
                visitRecordBean.setIssueType(visitObj.getString("visit_type"));
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

                visitRecordBean.setCustomer(customer);

                datalist.add(visitRecordBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datalist;
    }
}
