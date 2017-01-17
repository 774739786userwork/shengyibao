package com.bangware.shengyibao.customervisits.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.DoubleDatePickerDialog;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.customervisits.adapter.VisitCountAdapter;
import com.bangware.shengyibao.customervisits.model.entity.VisitsCountBean;
import com.bangware.shengyibao.customervisits.presenter.VisitsCountPersenter;
import com.bangware.shengyibao.customervisits.presenter.impl.VisitsCountPersenterImpl;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.view.RefreshListView;

import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class VisitsCountActivity extends BaseActivity implements VisitsCountView{
    private ImageView mVistisCount_backImg;
    private ListView mVistisCount_QueryListView;
    private LinearLayout mVistisCount_LinearLayout;
    private TextView mVistisCount_time;
    private User user;
    private VisitsCountPersenter visitsCountPersenter=null;
    String begin_date;
    String end_date;
    List<VisitsCountBean> VisitsCount_list=new ArrayList<VisitsCountBean>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    String currenttime = sdf.format(new Date());
    private VisitCountAdapter visitCountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_visits_count);
        SharedPreferences sharedPreferences=this.getSharedPreferences(User.SHARED_NAME, MODE_PRIVATE);
        user= AppContext.getInstance().readFromSharedPreferences(sharedPreferences);
        findView();
        setLinstener();
    }

    private void setLinstener() {
        MyOnClickLinstener linstener=new MyOnClickLinstener();
        mVistisCount_LinearLayout.setOnClickListener(linstener);
        mVistisCount_backImg.setOnClickListener(linstener);

    }

    private void findView() {
        mVistisCount_backImg= (ImageView) findViewById(R.id.VistisCount_backImg);
        mVistisCount_LinearLayout= (LinearLayout) findViewById(R.id.VistisCount_LinearLayout);
        mVistisCount_QueryListView= (ListView) findViewById(R.id.VistisCount_QueryListView);
        mVistisCount_time= (TextView) findViewById(R.id.VistisCount_time);
        mVistisCount_time.setText(currenttime);
        begin_date=currenttime;
        end_date=currenttime;
        visitsCountPersenter=new VisitsCountPersenterImpl(this);
        visitsCountPersenter.queryVisitCount(user,user.getEmployee_id(),currenttime,currenttime);
//        data = getData();
        visitCountAdapter = new VisitCountAdapter(this,VisitsCount_list);
        mVistisCount_QueryListView.setAdapter(visitCountAdapter);
        mVistisCount_QueryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                VisitsCountBean visitsCountBean= (VisitsCountBean) adapterView.getItemAtPosition(i);
                Intent intent=new Intent(VisitsCountActivity.this,QueryVisitsTimeActivity.class);
                Bundle bundle =  new Bundle();
                bundle.putSerializable("visitsCountBean",visitsCountBean);
                bundle.putString("begin_date",begin_date);
                Log.e("begin_date",begin_date);
                bundle.putString("end_date",end_date);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    @Override
    public void addCustomeVisitCount(List<VisitsCountBean> visitsCountBeanList) {
            if (visitsCountBeanList.size()>0)
            {
                VisitsCount_list.addAll(visitsCountBeanList);
                Log.e("VisitsCount_list","    "+VisitsCount_list.size());
                visitCountAdapter.notifyDataSetChanged();
            }else
            {
                showToast("暂无统计记录");
                visitCountAdapter.notifyDataSetChanged();
            }
    }

    @Override
    public void loadDataFailure(String failureMessage) {

    }

    private class MyOnClickLinstener implements View.OnClickListener
    {
        Calendar c = Calendar.getInstance();
        @Override
        public void onClick(View view) {
         switch (view.getId())
         {
             case R.id.VistisCount_backImg:
                 loadingdialog.dismiss();
                 VisitsCountActivity.this.finish();
                 break;
             case R.id.VistisCount_LinearLayout:
                 new DoubleDatePickerDialog(VisitsCountActivity.this, 0, new DoubleDatePickerDialog.OnDateSetListener() {

                     @Override
                     public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                           int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear,
                                           int endDayOfMonth) {

                         begin_date = String.format("%d-%d-%d", startYear,startMonthOfYear + 1, startDayOfMonth);
                         end_date = String.format("%d-%d-%d", endYear, endMonthOfYear + 1, endDayOfMonth);

                         if(begin_date.equals(end_date)){
                             mVistisCount_time.setText(begin_date);
                         }else{
                             mVistisCount_time.setText(begin_date+" 至 "+end_date);
                         }

                         if(java.sql.Date.valueOf(begin_date).after(java.sql.Date.valueOf(end_date))){
							showToast("开始日期不能大于结束日期！");
                         }

                         VisitsCount_list.clear();
                         visitsCountPersenter.queryVisitCount(user,user.getEmployee_id(),begin_date,end_date);
                         visitCountAdapter.notifyDataSetChanged();
                     }
                 }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true).show();
                 break;

         }

        }
    }

}
