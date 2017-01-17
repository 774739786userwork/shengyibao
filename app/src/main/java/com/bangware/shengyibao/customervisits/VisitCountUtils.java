package com.bangware.shengyibao.customervisits;

import android.util.Log;

import com.bangware.shengyibao.customervisits.model.entity.VisitsCountBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/21.
 */

public class VisitCountUtils {
    public static List<VisitsCountBean> getVisitsBeanList(String str)
    {
        List<VisitsCountBean> countBeanList=new ArrayList<VisitsCountBean>();
        try {
            JSONObject obj=new JSONObject(str);
            JSONArray jsonArray=obj.getJSONArray("data");
            VisitsCountBean countBean=null;
            for (int i=0;i<jsonArray.length();i++)
            {
                JSONObject jObject=jsonArray.getJSONObject(i);
                countBean=new VisitsCountBean();
                countBean.setCount_Order_Num(jObject.getInt("COUNT_ORDER_NUM"));
                countBean.setCOUNT_VISITS_NUM(jObject.getInt("COUNT_VISITS_NUM"));
                countBean.setEmploye_id(jObject.getString("EMPLOYEE_ID"));
                countBean.setEmploye_name(jObject.getString("NAME"));
                countBean.setOrder_Num(jObject.getInt("ORDER_NUM"));
                countBean.setOrder_Precent(jObject.getString("ORDER_PRECENT"));
                countBean.setVisits_Num(jObject.getInt("VISITS_NUM"));
                countBean.setVisits_Order_Precent(jObject.getString("VISITS_ORDER_PRECENT"));
                countBean.setVisits_Order_Num(jObject.getInt("VISITS_ORDER_NUM"));
                Log.e("countBean",countBean.getEmploye_id()+countBean.getVisits_Num());
                countBeanList.add(countBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  countBeanList;
    }
}
