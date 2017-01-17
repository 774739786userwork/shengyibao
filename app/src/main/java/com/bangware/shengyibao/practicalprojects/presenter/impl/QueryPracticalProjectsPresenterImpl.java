package com.bangware.shengyibao.practicalprojects.presenter.impl;

import com.bangware.shengyibao.practicalprojects.model.QueryPracticalProjectsModel;
import com.bangware.shengyibao.practicalprojects.model.entity.MyBean;
import com.bangware.shengyibao.practicalprojects.model.impl.QueryPracticalProjectsModelImpl;
import com.bangware.shengyibao.practicalprojects.presenter.OnQueryPracticalListener;
import com.bangware.shengyibao.practicalprojects.presenter.QueryPracticalProjectsPresenter;
import com.bangware.shengyibao.practicalprojects.view.QueryPracticalProjectsView;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;

import java.util.List;

/**
 * Created by Administrator on 2016/12/28.
 */

public class QueryPracticalProjectsPresenterImpl implements QueryPracticalProjectsPresenter,OnQueryPracticalListener {
    public  static final String  REQUEST_TAG = "QueryPracticalProjects";
    private String requestTag;
    private QueryPracticalProjectsModel model;
    private QueryPracticalProjectsView projectsView;

    public QueryPracticalProjectsPresenterImpl( QueryPracticalProjectsView projectsView) {
        this.requestTag = requestTag+System.currentTimeMillis();
        this.model = new QueryPracticalProjectsModelImpl();
        this.projectsView = projectsView;
    }

    @Override
    public void onLoadQueryPracticalSuccess(List<MyBean> Bean) {
        if(Bean != null){
            projectsView.doLoadQueryPracticalSuccess(Bean);
            projectsView.hideLoading();
        }
    }

    @Override
    public void onLoadQueryPracticalFailure(String errorMessage) {
        projectsView.showMessage(errorMessage);
    }

    @Override
    public void loadPracticalProjects(User user, int nPage, int nSpage, String content) {
        projectsView.showLoading();
        model.loadPracticalProjects(requestTag,user,nPage,nSpage,content,this);
    }


    @Override
    public void onDestroy() {
        DataRequest.getInstance().cancelRequestByTag(requestTag);
    }
}
