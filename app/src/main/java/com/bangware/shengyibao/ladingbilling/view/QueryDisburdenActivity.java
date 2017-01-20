package com.bangware.shengyibao.ladingbilling.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.deliverynote.view.DeliveryNoteDetailActivity;
import com.bangware.shengyibao.ladingbilling.adapter.QueryDisburdenAdapter;
import com.bangware.shengyibao.ladingbilling.model.entity.QueryDisburdenBean;
import com.bangware.shengyibao.ladingbilling.presenter.QueryDisburdenPresenter;
import com.bangware.shengyibao.ladingbilling.presenter.impl.QueryDisburdenPresenterImpl;
import com.bangware.shengyibao.main.view.MainActivity;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.customdialog.CommonDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class QueryDisburdenActivity extends BaseActivity implements QueryDisburdenAdapter.ReturnBack,QueryDisburdenView{

    private ImageButton querydisburn_Goback;
    private ListView querydisburn_ListView;
    private QueryDisburdenAdapter queryDisburdenAdapter;
    private List<QueryDisburdenBean> disburdenBeanList=new ArrayList<QueryDisburdenBean>();
    private QueryDisburdenPresenter presenter;
    private CommonDialog customDialog;
    private User user;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_disburden);
        sharedPreferences=this.getSharedPreferences(User.SHARED_NAME, MODE_PRIVATE);
        user= AppContext.getInstance().readFromSharedPreferences(sharedPreferences);
        findView();
        setLisenter();
        presenter=new QueryDisburdenPresenterImpl(this);
        presenter.OnQueryDisburden(user);
        queryDisburdenAdapter=new QueryDisburdenAdapter(disburdenBeanList,this,this);
        querydisburn_ListView.setAdapter(queryDisburdenAdapter);
    }

    private void findView() {
        querydisburn_Goback= (ImageButton) findViewById(R.id.querydisburn_Goback);
        querydisburn_ListView= (ListView) findViewById(R.id.querydisburn_ListView);
    }
    private void setLisenter() {
        querydisburn_Goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QueryDisburdenActivity.this.finish();
            }
        });
    }
    // 作废按钮回调
    @Override
    public void setLisenter(int p, View view, int v_id,String disburden_id) {
          switch (v_id)
          {
              case R.id.disburden_remove:
                  showDialog(disburden_id);
                  break;
          }
    }
    private void showDialog(final String disburden_id){
        customDialog = null;
        int srceenW =  ((BaseActivity)this).getWindowManager().getDefaultDisplay().getWidth();
        //联系人对话框
        customDialog = new CommonDialog(QueryDisburdenActivity.this,srceenW, R.layout.common_dialog_layout,R.style.custom_dialog);
        TextView tv_dialog_login_context = (TextView)customDialog.findViewById(R.id.tv_dialog_common_context);
        TextView tv_dialog_login_go = (TextView)customDialog.findViewById(R.id.tv_dialog_common_go);
        TextView tv_dialog_login_close = (TextView)customDialog.findViewById(R.id.tv_dialog_common_close);
        tv_dialog_login_context.setText("确认作废该单据！");
        tv_dialog_login_go.setText("坚决作废");
        customDialog.setCanceledOnTouchOutside(false);
        tv_dialog_login_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(QueryDisburdenActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        presenter.CancellationDisburden(user,disburden_id);
                        startActivity(intent); //执行
                    }
                };
                timer.schedule(task, 1000 * 1);
                customDialog.dismiss();
            }
        });
        tv_dialog_login_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }
    @Override
    public void doLoadingQueryDisburden(List<QueryDisburdenBean> queryDisburdenBeen) {
        if (queryDisburdenBeen.size()>0)
        {
            disburdenBeanList.addAll(queryDisburdenBeen);
            queryDisburdenAdapter.notifyDataSetChanged();
        }else
        {
            showToast("暂无卸货单信息");
        }
    }

    @Override
    public void doCancellationDisburden(String message) {
        showToast(message);
    }

    @Override
    public void destory() {
        if(presenter!=null){
            presenter.destroy();
        }
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
}
