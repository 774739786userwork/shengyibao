package com.bangware.shengyibao.refereevisit.model.impl;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.refereevisit.model.NewRefereeModel;
import com.bangware.shengyibao.refereevisit.presenter.OnNewRefereeLisenter;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/12/26.
 */

public class NewRefereeModelImpl implements NewRefereeModel {
    @Override
    public void addReferee(String requesTag, User user, String name, String mobile1, String relation, final OnNewRefereeLisenter refereeLisenter) {
        String add_referee_url= Model.ADDREFEREE_VISITOR_URL+"token="+user.getLogin_token()+"&method=edit_referee_contacts"+"&flag=save";
        try {
            JSONObject refereeDate=new JSONObject();
            refereeDate.put("name",name);
            refereeDate.put("mobile",mobile1);
            refereeDate.put("relation",relation);
            refereeDate.put("employee_id",user.getEmployee_id());
            Log.e("refereeDate",name+mobile1+user.getEmployee_id());
            JSONObject object=new JSONObject();
            object.put("refereeDate",refereeDate);
            DataRequest.getInstance().newJsonObjectPostRequest(requesTag, add_referee_url, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    if (jsonObject!=null)
                    {
                        try {
                            switch (jsonObject.getInt("result"))
                            {
                                case 0:
                                refereeLisenter.onAddRefereeSuccess("添加成功");
                                    refereeLisenter.onSaveSuccess();
                                break;
                                case 2:
                                    refereeLisenter.onAddRefereeFailure(jsonObject.getString("msg"));
                                    break;
                                default:
                                    refereeLisenter.onAddRefereeFailure("添加失败");
                                    break;
                            }
                        } catch (JSONException e) {
                           refereeLisenter.onAddRefereeFailure("数据解析失败");
                        }
                    }else
                    {
                        refereeLisenter.onAddRefereeFailure("返回内容为空");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    refereeLisenter.onAddRefereeFailure("请求失败，服务器异常......");
                }
            });
        } catch (JSONException e) {
           refereeLisenter.onAddRefereeFailure("请求参数设置失败");
        }
    }
}
