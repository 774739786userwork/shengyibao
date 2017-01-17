package com.bangware.shengyibao.refereevisit;

import com.bangware.shengyibao.refereevisit.model.entity.RefereeVisitor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27.
 */

public class RefereeContactsJosnUtils {
    public static List<RefereeVisitor> getRefereeVisitors(String str)
    {
        List<RefereeVisitor> contactslist = new ArrayList<RefereeVisitor>();
        JSONObject jsonObject = null;
        RefereeVisitor visitor=null;
        try {
            jsonObject = new JSONObject(str);
            JSONObject obj=jsonObject.getJSONObject("data");
            JSONArray jsonArray;
            jsonArray = obj.getJSONArray("referee_contact_list");
            int len=jsonArray.length();
            for (int i=0;i<len;i++)
            {
                JSONObject conObject=jsonArray.getJSONObject(i);
               visitor=new RefereeVisitor();
                visitor.setId(conObject.getString("id"));
                visitor.setMobile(conObject.getString("mobile"));
                visitor.setName(conObject.getString("name"));
                visitor.setRelation(conObject.getString("relation"));
                contactslist.add(visitor);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contactslist;
    }
}
