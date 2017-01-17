package com.bangware.shengyibao.main.view;

import android.Manifest;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.customervisits.view.CustomerVisitRecordActivity;
import com.bangware.shengyibao.customervisits.view.CustomerVisits;
import com.bangware.shengyibao.customervisits.view.ManagerHtmlActivity;
import com.bangware.shengyibao.customervisits.view.QueryVisitCustomerContactActivity;
import com.bangware.shengyibao.customervisits.view.VisitsCountActivity;
import com.bangware.shengyibao.main.model.entity.ThisMonthSummary;
import com.bangware.shengyibao.main.model.entity.ToDaySummary;
import com.bangware.shengyibao.main.presenter.MainPresenter;
import com.bangware.shengyibao.main.presenter.impl.MainPresenterImpl;
import com.bangware.shengyibao.net.pickpicture.PickPictureActivity;
import com.bangware.shengyibao.practicalprojects.view.NewPracticalProjects;
import com.bangware.shengyibao.practicalprojects.view.QueryPracticalProjects;
import com.bangware.shengyibao.refereevisit.view.RefereeVisitActivity;
import com.bangware.shengyibao.updateversion.VersionUpdateView;
import com.bangware.shengyibao.updateversion.model.entity.VersionBean;
import com.bangware.shengyibao.updateversion.presenter.GznUpdateVersionPresenterImpl;
import com.bangware.shengyibao.updateversion.presenter.UpdateVersionPresenter;
import com.bangware.shengyibao.updateversion.presenter.UpdateVersionPresenterImpl;
import com.bangware.shengyibao.updateversion.service.UpdateVersionService;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.VersionUtil;
import com.bangware.shengyibao.utils.volley.DataRequest;


public class FragmentTwoSaler extends Fragment implements MainView,VersionUpdateView{
    private LinearLayout customer_visit_linearLayout,today_visit_linearLayout,thisweek_visit_linearLayout,thismonth_visit_linearLayout;
    private LinearLayout referee_visit,new_practical_projects,query_practical_projects,my_customer_visitor;
    private View view = null;
    private Intent intent;
    private TextView today_date_textview;
    private TextView mVisits_Count,mVisits_Analysis;
    private UpdateVersionPresenter versionPresenter;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 0;

    private VersionBean newVersionBean = new VersionBean();
    private MainPresenter presenter;
    private User user;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null){
            view = inflater.inflate(R.layout.fragment_fragment_two_saler, container, false);
        }
        if(view!=null)
        {
            return view;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("user", AppContext.getInstance().getUser());
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState!=null) {
            DataRequest.buildRequestQueue(getActivity());
        }

        init();
        setListener();
        sharedPreferences=getActivity().getSharedPreferences(User.SHARED_NAME, getActivity().MODE_PRIVATE);
        user=AppContext.getInstance().readFromSharedPreferences(sharedPreferences);
        versionPresenter = new GznUpdateVersionPresenterImpl(this);
        versionPresenter.versionUpdate(user);
        presenter=new MainPresenterImpl(this);
        presenter.loadToDaySummary(user);

        if (user.getApp_id().equals("2")){
            mVisits_Analysis.setVisibility(View.VISIBLE);
        }else {
            mVisits_Analysis.setVisibility(View.GONE);
        }
    }


    private void init(){
        customer_visit_linearLayout = (LinearLayout) getView().findViewById(R.id.customer_visit_linearLayout);
        today_visit_linearLayout = (LinearLayout) getView().findViewById(R.id.today_visit_linearLayout);
        thisweek_visit_linearLayout = (LinearLayout) getView().findViewById(R.id.thisweek_visit_linearLayout);
        thismonth_visit_linearLayout = (LinearLayout) getView().findViewById(R.id.thismonth_visit_linearLayout);
        referee_visit= (LinearLayout) getView().findViewById(R.id.referee_visit);
        new_practical_projects= (LinearLayout) getView().findViewById(R.id.new_practical_projects);
        query_practical_projects= (LinearLayout) getView().findViewById(R.id.query_practical_projects);
        my_customer_visitor= (LinearLayout) getView().findViewById(R.id.my_customer_visitor);
        today_date_textview = (TextView) getView().findViewById(R.id.today_date_textview);
        mVisits_Count= (TextView) getView().findViewById(R.id.visits_count);
        mVisits_Analysis= (TextView) getView().findViewById(R.id.visits_analysis);
    }

    private void setListener(){
        MyOnClickListener listener=new MyOnClickListener();
        customer_visit_linearLayout.setOnClickListener(listener);
        today_visit_linearLayout.setOnClickListener(listener);
        thisweek_visit_linearLayout.setOnClickListener(listener);
        thismonth_visit_linearLayout.setOnClickListener(listener);
        referee_visit.setOnClickListener(listener);
        new_practical_projects.setOnClickListener(listener);
        query_practical_projects.setOnClickListener(listener);
        my_customer_visitor.setOnClickListener(listener);
        mVisits_Count.setOnClickListener(listener);
        mVisits_Analysis.setOnClickListener(listener);
    }

    @Override
    public void doUpdateVersionSuccess(VersionBean versionBean) {
        this.newVersionBean = versionBean;
        if(VersionUtil.getVersionCode(getActivity()) < newVersionBean.getVersion()){
            if(Build.VERSION.SDK_INT >= 23) {
                int checkSMSPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(checkSMSPermission != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                    return;
                }else{
                    showVersionUpdateConfirmDialog(newVersionBean.getLink());
                }
            }else{
                showVersionUpdateConfirmDialog(newVersionBean.getLink());
            }
        }else{
//            Toast.makeText(MainActivity.this, "当前是最新版本", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    showVersionUpdateConfirmDialog(newVersionBean.getLink());
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(),"用户取消了权限",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    //确认更新dialog
    private void showVersionUpdateConfirmDialog(final String link)
    {
        AlertDialog.Builder builer = new AlertDialog.Builder(getActivity());
        builer.setTitle("版本有更新，您是否需要现在更新？");
        builer.setCancelable(false);
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //拿到从后台解析的apk文件链接地址并传至服务类进行下载更新
                Intent intent = new Intent(getActivity(), UpdateVersionService.class);
                intent.putExtra("link",link);
                getActivity().startService(intent);
                Toast.makeText(getActivity(), "正在更新......", Toast.LENGTH_SHORT).show();
            }
        });
        builer.setNegativeButton("退出", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                getActivity().finish();
            }
        });
        builer.show();
    }

    public class MyOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            String tempStr = "";
            Bundle bundle;
            switch (view.getId()){
                //客户回访跳转
                case R.id.customer_visit_linearLayout:
                    intent = new Intent(getActivity(), CustomerVisits.class);
                    startActivity(intent);
                    break;
                //今日回访跳转
                case R.id.today_visit_linearLayout:
                    tempStr = "day";
                    intent = new Intent(getActivity(), CustomerVisitRecordActivity.class);
                    bundle =  new Bundle();
                    bundle.putString("time",tempStr);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                //本周回访跳转
                case R.id.thisweek_visit_linearLayout:
                    tempStr = "week";
                    intent = new Intent(getActivity(), CustomerVisitRecordActivity.class);
                    bundle =  new Bundle();
                    bundle.putString("time",tempStr);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                //本月回访跳转
                case R.id.thismonth_visit_linearLayout:
                    tempStr = "month";
                    intent = new Intent(getActivity(), CustomerVisitRecordActivity.class);
                    bundle =  new Bundle();
                    bundle.putString("time",tempStr);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                //推荐人拜访
                case R.id.referee_visit:
                    intent=new Intent(getActivity(), RefereeVisitActivity.class);
                    startActivity(intent);

                    break;
                //新建案例
                case R.id.new_practical_projects:
                    intent=new Intent(getActivity(), NewPracticalProjects.class);
                    startActivity(intent);

                    break;
                //查询案例
                case R.id.query_practical_projects:
                     intent=new Intent(getActivity(), QueryPracticalProjects.class);
                    startActivity(intent);
                    break;
                //我的拜访客户
                case R.id.my_customer_visitor:
                    intent=new Intent(getActivity(), QueryVisitCustomerContactActivity.class);
                    startActivity(intent);
                    break;
                //拜访统计查询
                case R.id.visits_count:
                    intent=new Intent(getActivity(), VisitsCountActivity.class);
                    startActivity(intent);
                    break;
                //拜访统计分析查询
                case R.id.visits_analysis:
                    intent=new Intent(getActivity(), ManagerHtmlActivity.class);
                    startActivity(intent);

                    break;
            }
        }
    }


    @Override
    public void doTodaySummaryLoadSuccess(ToDaySummary bean) {
        if(bean != null){
            today_date_textview.setText(bean.getTodaytime());
        }
    }

    @Override
    public void doThisMonthSummaryLoadSuccess(ThisMonthSummary bean) {

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(presenter!=null){
            presenter.destroy();
        }
    }
}
