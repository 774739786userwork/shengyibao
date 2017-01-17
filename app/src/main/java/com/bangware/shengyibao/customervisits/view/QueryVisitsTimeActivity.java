package com.bangware.shengyibao.customervisits.view;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.customervisits.adapter.CustomerVisitRecordAdapter;
import com.bangware.shengyibao.customervisits.model.entity.VisitRecordBean;
import com.bangware.shengyibao.customervisits.model.entity.VisitsCountBean;
import com.bangware.shengyibao.customervisits.presenter.VisitsTimePresenter;
import com.bangware.shengyibao.customervisits.presenter.impl.VisitsTimePresenterImpl;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.view.OnRefreshListener;
import com.bangware.shengyibao.view.RefreshListView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QueryVisitsTimeActivity extends BaseActivity implements OnRefreshListener,CustomerVisitRecordView,CustomerVisitRecordAdapter.Callback {
    private ImageView mVisit_time_imageview;
    private TextView mTitle_visit_time;
    private Handler mHandler = new Handler();
    private RefreshListView mVisit_time_ListView;
    private CustomerVisitRecordAdapter recordAdapter = null;//拜访记录数据适配
    private VisitsTimePresenter presenter=null;

    private int nPage = 1;
    private int nSpage = 10;
    private int pageSize;
    public int totalSize = 0;
    String begin_date;
    String end_date;
    private List<VisitRecordBean> list = new ArrayList<VisitRecordBean>();
    private boolean isPlay = false;//是否播放
    private MediaPlayer mp;
    VisitsCountBean countBean;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_visits_time);
            begin_date=getIntent().getExtras().getString("begin_date");
            end_date=getIntent().getExtras().getString("end_date");
            countBean= (VisitsCountBean) getIntent().getExtras().getSerializable("visitsCountBean");
        findView();
        setListenter();
    }

    private void setListenter() {
        mVisit_time_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void findView() {
        SharedPreferences sharedPreferences=this.getSharedPreferences(User.SHARED_NAME, MODE_PRIVATE);
        user= AppContext.getInstance().readFromSharedPreferences(sharedPreferences);
        mVisit_time_imageview= (ImageView) findViewById(R.id.visit_time_imageview);
        mTitle_visit_time= (TextView) findViewById(R.id.title_visit_time);
        mVisit_time_ListView= (RefreshListView) findViewById(R.id.visit_time_ListView);
        mTitle_visit_time.setText(countBean.getEmploye_name()+"拜访记录");
        recordAdapter = new CustomerVisitRecordAdapter(this,list,this);
        mVisit_time_ListView.setAdapter(recordAdapter);//将数据源添加到listview中
        presenter=new VisitsTimePresenterImpl(this);
        presenter.addVisitTimeRecord(user,nPage,nSpage,begin_date,end_date,countBean.getEmploye_id());
        totalSize=nSpage;
    }

    @Override
    public boolean onTouchEvent(int pos, View v, MotionEvent motionEvent, int which) {
        switch (which){
            case  R.id.text_pictureReplace:
                TextView visit_voice = (TextView) v.findViewById(R.id.text_pictureReplace);
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (isPlay == false){
                        visit_voice.setBackgroundResource(R.drawable.chatto_bg_pressed);
                        mHandler.postDelayed(new Runnable() {
                            public void run() {

                            }
                        }, 300);
                        mp = new MediaPlayer();
                        try {
                            mp.setDataSource(Model.HTTPURL+list.get(pos).getVisitContent());
                            mp.prepare();
                            mp.start();

                            isPlay = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else if (isPlay == true){
                        try {
                            visit_voice.setBackgroundResource(R.drawable.chatto_bg_pressed);
                            isPlay = false;
                            if (mp != null){
                                mp.stop();
                                mp.release();
                                mp = null;
                            }
                        }catch (IllegalStateException ex){
                            ex.printStackTrace();
                        }
                    }
                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    visit_voice.setBackgroundResource(R.drawable.chatto_bg_normal);
                }
                break;
        }
        return true;
    }

    @Override
    public void addCustomeVisitReocrd(List<VisitRecordBean> visitRecordBeanList) {
        if (visitRecordBeanList.size() > 0 ){
            list.addAll(visitRecordBeanList);
            pageSize = list.get(0).getTotalPage();
            Log.e("pageSize"," eeeeeeeeeeeeeee"+pageSize);
            recordAdapter.notifyDataSetChanged();
        }else {
            showToast("无拜访记录！");
            recordAdapter.notifyDataSetChanged();
        }
        mVisit_time_ListView.hideFooterView();
        mVisit_time_ListView.setOnRefreshListener(QueryVisitsTimeActivity.this);
    }

    @Override
    public void loadDataFailure(String failureMessage) {

    }

    @Override
    public void onDownPullRefresh() {

    }

    @Override
    public void onLoadingMore() {
        nPage += 1;
        if (totalSize >= pageSize){
            Log.e("totalSize","wwwwwwwwwwwwww"+totalSize);
            mVisit_time_ListView.hideFooterView();
            showToast("暂无更多数据！");
            return;
        }else {
            presenter.addVisitTimeRecord(user,nPage,nSpage,begin_date,end_date,countBean.getEmploye_id());
        }
        totalSize += nSpage;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        isPlay = false;
        if(mp!=null){
            mp.stop();
            mp.release();
            mp=null;
        }
        if (presenter != null){
            presenter.destory();
        }
    }
}
