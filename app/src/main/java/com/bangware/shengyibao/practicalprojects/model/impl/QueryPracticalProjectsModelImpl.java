package com.bangware.shengyibao.practicalprojects.model.impl;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.practicalprojects.QueryPracticalJosnUtils;
import com.bangware.shengyibao.practicalprojects.model.QueryPracticalProjectsModel;
import com.bangware.shengyibao.practicalprojects.model.entity.MyBean;
import com.bangware.shengyibao.practicalprojects.presenter.OnQueryPracticalListener;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;


/**
 * Created by Administrator on 2016/12/28.
 */

public class QueryPracticalProjectsModelImpl implements QueryPracticalProjectsModel {
    @Override
    public void loadPracticalProjects(String requestTag, User user, int nPage, int nSpage, String content, final OnQueryPracticalListener practicalListener) {
        final String query_practical_url;
        try {
            query_practical_url = Model.NEW_PRACTICAL_PROJECT_URL+"?token="+user.getLogin_token()+"&page="+nPage+"&content="+
                    URLEncoder.encode(content, "UTF-8")+"&rows="+nSpage;

        Log.e("query_practical_url",query_practical_url);
        DataRequest.getInstance().newJsonObjectGetRequest(requestTag, query_practical_url, null, new Response.Listener() {

            @Override
            public void onResponse(Object o) {
                if (o != null) {
                    List<MyBean> list= QueryPracticalJosnUtils.getQueryPractical(o.toString());
                    practicalListener.onLoadQueryPracticalSuccess(list);
                }else
                {
                   practicalListener.onLoadQueryPracticalFailure("数据传输失败");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
             practicalListener.onLoadQueryPracticalFailure("请求失败，服务器异常.....");
            }
        });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
