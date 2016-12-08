package com.bangware.shengyibao.salesamount.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.DoubleDatePickerDialog;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.salesamount.adapter.SaleRankingAdapter;
import com.bangware.shengyibao.salesamount.model.entity.SaleRankingBean;
import com.bangware.shengyibao.salesamount.presenter.SaleRankingPresenter;
import com.bangware.shengyibao.salesamount.presenter.impl.SaleRankingPresenterImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SaleRankingActivity extends BaseActivity implements SaleRankingView{
    private ImageView backimage;
    private TextView starttimetext,endtimetext,group_ranking_textview;
    private ListView saleRankingListView;
    private LinearLayout topLinearLayout;
    private SaleRankingAdapter rankingAdapter;
    private List<SaleRankingBean> saleRankingBeanList = new ArrayList<SaleRankingBean>();
    private boolean ranking_topListview = false;

    private String begin_date;
    private String end_date;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar c = Calendar.getInstance();
    private String year=String.valueOf(c.get(Calendar.YEAR));
    private String month=String.valueOf(c.get(Calendar.MONTH));

    private SaleRankingPresenter rankingPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sale_ranking);

        c.set(Integer.parseInt(year), Integer.parseInt(month), c.getActualMinimum(Calendar.DAY_OF_MONTH));
        begin_date=sdf.format(c.getTime());
        c.set(Integer.parseInt(year), Integer.parseInt(month), c.getActualMaximum(Calendar.DAY_OF_MONTH));
        end_date = sdf.format(c.getTime());

        findView();
        setListener();
    }

    public void findView(){
        backimage = (ImageView) findViewById(R.id.saleRanking_backImg);
        starttimetext = (TextView) findViewById(R.id.ranking_start_date);
        endtimetext = (TextView) findViewById(R.id.ranking_end_date);
        group_ranking_textview = (TextView) findViewById(R.id.group_ranking_textview);
        topLinearLayout = (LinearLayout) findViewById(R.id.topLinearLayout);
        saleRankingListView = (ListView)findViewById(R.id.saleRankingListView);

        rankingPresenter = new SaleRankingPresenterImpl(this);
        rankingPresenter.loadSaleRankingData(begin_date,end_date);
        starttimetext.setText(begin_date);
        endtimetext.setText(end_date);

        rankingAdapter = new SaleRankingAdapter(this,saleRankingBeanList);
        saleRankingListView.setAdapter(rankingAdapter);
    }

    public void setListener(){
        MyOnClickListener myOnClickListener = new MyOnClickListener();
        backimage.setOnClickListener(myOnClickListener);
        topLinearLayout.setOnClickListener(myOnClickListener);
        group_ranking_textview.setOnClickListener(myOnClickListener);
    }


    //请求加载下来的数据
    @Override
    public void doSaleRankingLoadSuccess(List<SaleRankingBean> rankingBeanList) {
        if (rankingBeanList.size() > 0){
            saleRankingBeanList.clear();
            saleRankingBeanList.addAll(rankingBeanList);
            rankingAdapter.notifyDataSetChanged();
        }else{
            showMessage("暂无排名");
            rankingAdapter.notifyDataSetChanged();
        }
    }

    private class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.saleRanking_backImg){
                finish();
            }
            if (v.getId() == R.id.group_ranking_textview){
                Intent intent = new Intent(SaleRankingActivity.this,GroupRankingActivity.class);
                startActivity(intent);
            }
            if (v.getId() == R.id.topLinearLayout){
                Drawable drawable = null;
                if (!ranking_topListview) {
                    drawable = getResources().getDrawable(R.drawable.ic_arrow_up_black);
                    ranking_topListview = true;
                } else {
                    drawable = getResources().getDrawable(R.drawable.ic_arrow_down_black);
                    ranking_topListview = false;
                }
                new DoubleDatePickerDialog(SaleRankingActivity.this, 0, new DoubleDatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                          int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear,
                                          int endDayOfMonth) {

                        begin_date = String.format("%d-%d-%d", startYear,startMonthOfYear + 1,1);
                        end_date = String.format("%d-%d-%d", endYear, endMonthOfYear + 1,endDayOfMonth);
                        starttimetext.setText(begin_date);
                        endtimetext.setText(end_date);

                        saleRankingBeanList.clear();
                        rankingPresenter.loadSaleRankingData(begin_date,end_date);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), false).show();
                //动态设置textview drawableRight属性
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//设置边界
                endtimetext.setCompoundDrawables(null, null, drawable, null);//设置图片在文字右边
            } else {
                Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow_down_black);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                endtimetext.setCompoundDrawables(null, null,
                        drawable, null);
                ranking_topListview = false;
            }
        }
    }

}
