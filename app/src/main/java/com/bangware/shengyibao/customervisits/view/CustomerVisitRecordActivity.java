package com.bangware.shengyibao.customervisits.view;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.customervisits.adapter.CustomerVisitRecordAdapter;
import com.bangware.shengyibao.customervisits.model.entity.VisitRecordBean;
import com.bangware.shengyibao.customervisits.presenter.CustomerVisitRecordPresenter;
import com.bangware.shengyibao.customervisits.presenter.impl.CustomerVisitRecordPresenterImpl;
import com.bangware.shengyibao.view.OnRefreshListener;
import com.bangware.shengyibao.view.RefreshListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/**
 * 客户拜访记录
 */
public class CustomerVisitRecordActivity extends BaseActivity implements OnRefreshListener,CustomerVisitRecordView,CustomerVisitRecordAdapter.Callback{
    private ImageView back_img;
    private TextView title_visits;
    private RefreshListView lv;//列表listview
    private CustomerVisitRecordAdapter recordAdapter = null;//拜访记录数据适配
    /**
     * 请求参数及数据接口访问
     */
    private CustomerVisitRecordPresenter visitRecordPresenter = null;
    private int nPage = 1;
    private int nSpage = 10;
    private int pageSize;
    public int totalSize = 0;
    private List<VisitRecordBean> list = new ArrayList<VisitRecordBean>();
    public static String[] Visit_DATE_SELECT = new String[]{"day", "week","month"};

    private String day = "";
    private String week = "";
    private String month = "";
    private boolean isPlay = false;//是否播放
    private MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_visit_record);

        init();
        setListener();
    }

    private void init(){
        back_img = (ImageView) findViewById(R.id.visit_record_imageview);
        lv = (RefreshListView) findViewById(R.id.visit_record_ListView);
        title_visits= (TextView) findViewById(R.id.title_visits);

        //调P的实现类发起后台请求
        visitRecordPresenter = new CustomerVisitRecordPresenterImpl(this);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null){
            day = bundle.getString("time");
            week = bundle.getString("time");
            month = bundle.getString("time");

            if (day.equals(Visit_DATE_SELECT[0])){
                title_visits.setText("今日拜访记录");
                list.clear();
                nPage = 1;
                totalSize = nSpage;
                visitRecordPresenter.addVisitRecord(day,nPage,nSpage);
            }else if (week.equals(Visit_DATE_SELECT[1])){
                title_visits.setText("本周拜访记录");
                list.clear();
                nPage = 1;
                totalSize = nSpage;
                visitRecordPresenter.addVisitRecord(week,nPage,nSpage);
            }else if (month.equals(Visit_DATE_SELECT[2])){
                title_visits.setText("本月拜访记录");
                list.clear();
                nPage = 1;
                totalSize = nSpage;
                visitRecordPresenter.addVisitRecord(month,nPage,nSpage);
            }

            recordAdapter = new CustomerVisitRecordAdapter(this,list,this);
            lv.setAdapter(recordAdapter);//将数据源添加到listview中
        }
    }

    private void setListener(){
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //给item设置点击事件
        /*lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showToast("listview点击事件"+i);
            }
        });*/
    }

    //获取请求下来的数据
    @Override
    public void addCustomeVisitReocrd(List<VisitRecordBean> visitRecordBeanList) {
        if (visitRecordBeanList.size() > 0 ){
            list.addAll(visitRecordBeanList);
            pageSize = list.get(0).getTotalPage();
            recordAdapter.notifyDataSetChanged();
        }else {
            showToast("无拜访记录！");
            recordAdapter.notifyDataSetChanged();
        }
        lv.hideFooterView();
        lv.setOnRefreshListener(CustomerVisitRecordActivity.this);
    }

    @Override
    public void loadDataFailure(String failureMessage) {

    }

    @Override
    public void onDownPullRefresh() {

    }

    /**
     * 下拉加载更多
     */
    @Override
    public void onLoadingMore() {
        nPage += 1;
        if (totalSize >= pageSize){
            lv.hideFooterView();
            showToast("暂无更多数据！");
            return;
        }else {
            if (day.equals(Visit_DATE_SELECT[0])){
                visitRecordPresenter.addVisitRecord(day,nPage,nSpage);//今日
            }else if (week.equals(Visit_DATE_SELECT[1])){
                visitRecordPresenter.addVisitRecord(week,nPage,nSpage);//本周
            }else if (month.equals(Visit_DATE_SELECT[2])){
                visitRecordPresenter.addVisitRecord(month,nPage,nSpage);//本月
            }
        }
        totalSize += nSpage;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (visitRecordPresenter != null){
            visitRecordPresenter.destory();
        }
    }

    /**
     * 回调语音播放
     * @param pos
     * @param v
     * @param motionEvent
     * @param which
     * @return
     */
    @Override
    public boolean onTouchEvent(int pos, View v, MotionEvent motionEvent,int which) {
        switch (which){
            case  R.id.text_pictureReplace:
                TextView visit_voice = (TextView) v.findViewById(R.id.text_pictureReplace);
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (isPlay == false){
                        visit_voice.setBackgroundResource(R.drawable.chatto_bg_pressed);
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
                        visit_voice.setBackgroundResource(R.drawable.chatto_bg_pressed);
                        isPlay = false;
                        if(mp!=null){
                            mp.stop();
                            mp.release();
                            mp=null;
                        }
                    }
                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    visit_voice.setBackgroundResource(R.drawable.chatto_bg_normal);
                }
                break;
        }
        return true;
    }
}
