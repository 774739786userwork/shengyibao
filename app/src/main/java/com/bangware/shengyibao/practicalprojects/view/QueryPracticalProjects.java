package com.bangware.shengyibao.practicalprojects.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.practicalprojects.model.entity.MyBean;
import com.bangware.shengyibao.practicalprojects.presenter.QueryPracticalProjectsPresenter;
import com.bangware.shengyibao.practicalprojects.presenter.impl.QueryPracticalProjectsPresenterImpl;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.OnScrollGridView.MyListAdapter;
import com.bangware.shengyibao.view.CasesRefreshListView;
import com.bangware.shengyibao.view.OnCasesRefreshListener;
import com.bangware.shengyibao.view.OnRefreshListener;
import com.bangware.shengyibao.view.RefreshListView;

import java.util.ArrayList;
import java.util.List;

public class QueryPracticalProjects extends BaseActivity implements QueryPracticalProjectsView,OnCasesRefreshListener{
    private ImageButton query_practicalprojects_Goback;
    private TextView query_practicalprojects_content;
    private CasesRefreshListView prctical_queryListView;
    private MyListAdapter adapter;
    private List<MyBean> list = new ArrayList<MyBean>();
    private QueryPracticalProjectsPresenter projectsPresenter;
    private int nPage=1;
    private int nSpage=10;
    public int totalSize = 0;
    private int MaxDateNum;
    private String content="";
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_practical_projects);
        SharedPreferences sharedPreferences=this.getSharedPreferences(User.SHARED_NAME, MODE_PRIVATE);
        user= AppContext.getInstance().readFromSharedPreferences(sharedPreferences);
        projectsPresenter=new QueryPracticalProjectsPresenterImpl(this);
        findView();
        setLisenter();
    }

    private void setLisenter() {
        query_practicalprojects_Goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        query_practicalprojects_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(QueryPracticalProjects.this,FuzzyQueryPractical.class);
                startActivityForResult(intent,899);
            }
        });
    }

    private void findView() {
        query_practicalprojects_Goback= (ImageButton) findViewById(R.id.query_practicalprojects_Goback);
        query_practicalprojects_content= (TextView) findViewById(R.id.query_practicalprojects_content);
        prctical_queryListView= (CasesRefreshListView) findViewById(R.id.prctical_queryListView);
        projectsPresenter.loadPracticalProjects(user,nPage,nSpage,content);
        adapter=new MyListAdapter(this,list);
        prctical_queryListView.setAdapter(adapter);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 899 && resultCode == 900) {
            content=data.getStringExtra("content");
            if (content==null||content.equals(""))
            {
                return;
            }else
            {
                list.clear();
                nPage=1;
                totalSize=nSpage;
                projectsPresenter.loadPracticalProjects(user,nPage,nSpage,content);

                Log.e("回调","我是返回的值");
            }
        }
    }
    @Override
    public void doLoadQueryPracticalSuccess(List<MyBean> Bean) {
          if (Bean.size()>0)
          {
              list.addAll(Bean);
              MaxDateNum = list.get(0).getTotal();
              adapter.notifyDataSetChanged();
          }else
          {
              adapter.notifyDataSetChanged();
          }
        prctical_queryListView.hideHeaderView();
        prctical_queryListView.hideFooterView();
        prctical_queryListView.setOnRefreshListener(QueryPracticalProjects.this);
    }

    @Override
    public void onDownPullRefresh() {
        nPage+=1;
        if(totalSize >= MaxDateNum){
            showToast("暂无更多数据！请不要重复刷新！");
            prctical_queryListView.hideHeaderView();
            return;
        }else{
            projectsPresenter.loadPracticalProjects(user,nPage,nSpage,content);
        }
        totalSize += nSpage;
    }

    @Override
    public void onLoadingMore() {
        nPage+=1;
        if(totalSize >= MaxDateNum){
			showToast("暂无更多数据！请不要重复刷新！");
            prctical_queryListView.hideFooterView();
            return;
        }else{
            projectsPresenter.loadPracticalProjects(user,nPage,nSpage,content);
        }
        totalSize += nSpage;
    }

    @Override
    protected void onDestroy() {
        if(projectsPresenter!=null){
            projectsPresenter.onDestroy();
        }
        super.onDestroy();
    }
}
