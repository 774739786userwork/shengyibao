package com.bangware.shengyibao.practicalprojects;

import android.util.Log;

import com.bangware.shengyibao.customer.model.entity.CustomerImage;
import com.bangware.shengyibao.practicalprojects.model.entity.MyBean;
import com.bangware.shengyibao.refereevisit.model.entity.RefereeVisitor;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27.
 */

public class QueryPracticalJosnUtils {
    public static List<MyBean> getQueryPractical(String str)
    {
        List<MyBean> list = new ArrayList<MyBean>();
        JSONObject jsonObject = null;
        MyBean bean=null;
        CustomerImage image=null;
        try {
            jsonObject = new JSONObject(str);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i=0;i<jsonArray.length();i++)
            {
                JSONObject conObject=jsonArray.getJSONObject(i);
                bean=new MyBean();
                bean.setId(conObject.getInt("id"));
                bean.setContent(conObject.getString("content"));
                bean.setEmployee_name(conObject.getString("employee_name"));

                if (conObject.getJSONArray("case_images").length() == 0){
                    JSONObject videoObj =jsonArray.getJSONObject(i).getJSONObject("case_video");
                    bean.setVideo(videoObj.getString("url"));
                    bean.setVideoTime(videoObj.getString("duration"));
                }else {
                    JSONArray array=conObject.getJSONArray("case_images");
                    for (int j=0;j<array.length();j++)
                    {
                        JSONObject imgObj = array.getJSONObject(j);
                        image=new CustomerImage();
                        image.setImg_id(imgObj.getString("id"));
                        image.setImg_url(imgObj.getString("img_src"));
                        bean.addImages(image);
                    }
                }
               bean.setTotal(jsonObject.getInt("total"));
                list.add(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
