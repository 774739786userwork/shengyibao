package com.bangware.shengyibao.customervisits.fragment;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.customervisits.adapter.RefereeVisitRecordAdapter;
import com.bangware.shengyibao.customervisits.model.entity.RefereeBean;
import com.bangware.shengyibao.customervisits.presenter.RefereeVisitRecordPresenter;
import com.bangware.shengyibao.customervisits.presenter.impl.RefereeVisitRecordPresenterImpl;
import com.bangware.shengyibao.customervisits.view.RefereeVisitRecordView;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.volley.DataRequest;
import com.bangware.shengyibao.view.OnRefreshListener;
import com.bangware.shengyibao.view.RefreshListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * 推荐人页面
 * Created by bangware on 2016/12/28.
 */

public class RefereeFragment extends Fragment implements OnRefreshListener,RefereeVisitRecordView,RefereeVisitRecordAdapter.Callback {

    private Handler mHandler = new Handler();
    private RefreshListView lv;//列表listview
    private RefereeVisitRecordAdapter recordAdapter = null;//拜访记录数据适配

    /**
     * 请求参数及数据接口访问
     */
    private RefereeVisitRecordPresenter visitRecordPresenter = null;
    private int nPage = 1;
    private int nSpage = 10;
    private int pageSize;
    public int totalSize = 0;
    private List<RefereeBean> list = new ArrayList<RefereeBean>();
    public static String[] Visit_DATE_SELECT = new String[]{"day", "week","month"};
    private String time;
    private boolean isPlay = false;//是否播放
    private MediaPlayer mp;
    private User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        View chatView = null;
        if (chatView == null){
            chatView = inflater.inflate(R.layout.referee_fragment, container, false);
        }
        if(chatView!=null)
        {
            return chatView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init(){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(User.SHARED_NAME, MODE_PRIVATE);
        user= AppContext.getInstance().readFromSharedPreferences(sharedPreferences);
        lv = (RefreshListView) getView().findViewById(R.id.visit_referee_ListView);
        //调P的实现类发起后台请求
        visitRecordPresenter = new RefereeVisitRecordPresenterImpl(this);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null){
            time = bundle.getString("time");

            if (time.equals(Visit_DATE_SELECT[0])){
                list.clear();
                nPage = 1;
                totalSize = nSpage;
                visitRecordPresenter.addRefereeRecord(time,user,nPage,nSpage);

            }else if (time.equals(Visit_DATE_SELECT[1])){
                list.clear();
                nPage = 1;
                totalSize = nSpage;
                visitRecordPresenter.addRefereeRecord(time,user,nPage,nSpage);
            }else if (time.equals(Visit_DATE_SELECT[2])){
                list.clear();
                nPage = 1;
                totalSize = nSpage;
                visitRecordPresenter.addRefereeRecord(time,user,nPage,nSpage);
            }

            recordAdapter = new RefereeVisitRecordAdapter(getActivity(),list,this);
            lv.setAdapter(recordAdapter);//将数据源添加到listview中
        }
    }

    @Override
    public void addRefereeVisitReocrd(List<RefereeBean> visitRefereeList) {
        if (visitRefereeList.size() > 0 ){
            list.addAll(visitRefereeList);
            pageSize = list.get(0).getTotalPage();
            recordAdapter.notifyDataSetChanged();
        }else {
            Toast.makeText(getActivity(),"无拜访记录！", Toast.LENGTH_LONG).show();
            recordAdapter.notifyDataSetChanged();
        }
        lv.hideFooterView();
        lv.setOnRefreshListener(this);
    }

    @Override
    public void loadDataFailure(String failureMessage) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showLoading(String text) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

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
    public void onDownPullRefresh() {

    }

    @Override
    public void onLoadingMore() {
        nPage += 1;
        if (totalSize >= pageSize){
            lv.hideFooterView();
            Toast.makeText(getActivity(),"暂无更多数据！",Toast.LENGTH_LONG).show();
            return;
        }else {
            if (time.equals(Visit_DATE_SELECT[0])){
                visitRecordPresenter.addRefereeRecord(time,user,nPage,nSpage);//今日
            }else if (time.equals(Visit_DATE_SELECT[1])){
                visitRecordPresenter.addRefereeRecord(time,user,nPage,nSpage);//本周
            }else if (time.equals(Visit_DATE_SELECT[2])){
                visitRecordPresenter.addRefereeRecord(time,user,nPage,nSpage);//本月
            }
        }
        totalSize += nSpage;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isPlay = false;
        if(mp!=null){
            mp.stop();
            mp.release();
            mp=null;
        }
        if (visitRecordPresenter != null){
            visitRecordPresenter.destory();
        }
    }
}
