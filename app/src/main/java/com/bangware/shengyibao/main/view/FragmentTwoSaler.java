package com.bangware.shengyibao.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.customervisits.view.CustomerVisitRecordActivity;
import com.bangware.shengyibao.customervisits.view.CustomerVisits;
import com.bangware.shengyibao.main.model.entity.ThisMonthSummary;
import com.bangware.shengyibao.main.model.entity.ToDaySummary;
import com.bangware.shengyibao.main.presenter.MainPresenter;
import com.bangware.shengyibao.main.presenter.impl.MainPresenterImpl;

public class FragmentTwoSaler extends Fragment implements MainView{
    private LinearLayout customer_visit_linearLayout,today_visit_linearLayout,thisweek_visit_linearLayout,thismonth_visit_linearLayout;
    private View view = null;
    private Intent intent;
    private TextView today_date_textview;

    private MainPresenter presenter;

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        setListener();
    }

    private void init(){
        customer_visit_linearLayout = (LinearLayout) getView().findViewById(R.id.customer_visit_linearLayout);
        today_visit_linearLayout = (LinearLayout) getView().findViewById(R.id.today_visit_linearLayout);
        thisweek_visit_linearLayout = (LinearLayout) getView().findViewById(R.id.thisweek_visit_linearLayout);
        thismonth_visit_linearLayout = (LinearLayout) getView().findViewById(R.id.thismonth_visit_linearLayout);
        today_date_textview = (TextView) getView().findViewById(R.id.today_date_textview);

        //初始化Presenter 加载当天时间
        presenter=new MainPresenterImpl(this);
        presenter.loadToDaySummary();
    }

    private void setListener(){
        MyOnClickListener listener=new MyOnClickListener();
        customer_visit_linearLayout.setOnClickListener(listener);
        today_visit_linearLayout.setOnClickListener(listener);
        thisweek_visit_linearLayout.setOnClickListener(listener);
        thismonth_visit_linearLayout.setOnClickListener(listener);
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
