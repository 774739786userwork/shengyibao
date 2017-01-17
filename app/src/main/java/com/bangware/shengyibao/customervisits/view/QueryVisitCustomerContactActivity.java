package com.bangware.shengyibao.customervisits.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.customer.model.entity.Contacts;
import com.bangware.shengyibao.customercontacts.QuickBillingAdapter;
import com.bangware.shengyibao.customervisits.presenter.VisitCustomerContactsPresenter;
import com.bangware.shengyibao.customervisits.presenter.impl.VisitCustomerContactsPresenterImpl;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.ClearEditText;
import com.bangware.shengyibao.view.OnRefreshListener;
import com.bangware.shengyibao.view.RefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 查看拜访过的客户联系人
 */
public class QueryVisitCustomerContactActivity extends BaseActivity implements OnRefreshListener,VisitCustomerContactsView {
    public String text="";
    private ImageView mCustomerConQuery_back;//回退按钮
    private RefreshListView mRefreshListView;
    private TextView mCustomerContactQuery_btn;//查询按钮
    private ClearEditText mClearEditText;
    private VisitCustomerContactsPresenter presenter;
    int nPage=1;
    int nSpage=10;
    private int pageSize;
    public int totalSize = 0;
    String phone="";
    String contactName="";
    private List<Contacts> customerlist = new ArrayList<Contacts>();
    private QuickBillingAdapter adapter;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_visit_customer_contact);

        SharedPreferences sharedPreferences=this.getSharedPreferences(User.SHARED_NAME, MODE_PRIVATE);
        user= AppContext.getInstance().readFromSharedPreferences(sharedPreferences);

        findView();
        setListeners();

        adapter = new QuickBillingAdapter(this, customerlist);
        mRefreshListView.setAdapter(adapter);
    }

    private void findView() {
        // TODO Auto-generated method stub
        mCustomerConQuery_back=(ImageView) findViewById(R.id.customercontactVisit_back);
        mCustomerContactQuery_btn=(TextView) findViewById(R.id.customercontactVisitquery_btn);
        mClearEditText=(ClearEditText) findViewById(R.id.customercontactVisit_edit);
        mRefreshListView=(RefreshListView) findViewById(R.id.customercontactVisitquery_ListView);
        presenter=new VisitCustomerContactsPresenterImpl(this);
        presenter.loadCustomerVisitContacts(user, nPage, nSpage, phone, contactName,user.getEmployee_id());
    }

    private void setListeners() {
        // TODO Auto-generated method stub
        MyOnclickListener onclickListener=new MyOnclickListener();
        mCustomerConQuery_back.setOnClickListener(onclickListener);
        mCustomerContactQuery_btn.setOnClickListener(onclickListener);
    }

    private class MyOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int cid = v.getId();
            //返回键处理
            if (cid == R.id.customercontactVisit_back) {
                loadingdialog.dismiss();
                finish();
            }
            if (cid == R.id.customercontactVisitquery_btn) {
                customerContactsQuickQuery();
            }
        }
    }

    public void customerContactsQuickQuery(){
        text = mClearEditText.getText().toString();

        if(TextUtils.isEmpty(text)){
            showToast("查询条件不能为空！");
            return;
        }

        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(text);
        if (m.matches()) {
            phone = text;
            customerlist.clear();
            nPage = 1;
            totalSize = nSpage;
            contactName = "";
            presenter.loadCustomerVisitContacts(user, nPage, nSpage, phone, "",user.getEmployee_id());
        }

        p = Pattern.compile("[\u4e00-\u9fa5]*");
        m = p.matcher(text);
        if (m.matches()) {
            contactName = text;
            customerlist.clear();
            nPage = 1;
            totalSize = nSpage;
            phone = "";
            presenter.loadCustomerVisitContacts(user, nPage, nSpage, "", contactName,user.getEmployee_id());
        }

    }

    @Override
    public void doCustomerContactsVisitSuccess(List<Contacts> Contacts) {
        if(Contacts.size() > 0){
            customerlist.addAll(Contacts);
            pageSize = customerlist.get(0).getCustomer().getTotal_record_sum();
            adapter.notifyDataSetChanged();
        }else{
            showToast("暂无客户数据！");
            adapter.notifyDataSetChanged();
        }
        mRefreshListView.hideFooterView();
        mRefreshListView.setOnRefreshListener(QueryVisitCustomerContactActivity.this);
    }

    @Override
    public void onDownPullRefresh() {

    }

    @Override
    public void onLoadingMore() {
        nPage+=1;
        if(totalSize >= pageSize){
            mRefreshListView.hideFooterView();
            return;
        }else{
            presenter.loadCustomerVisitContacts(user, nPage, nSpage, phone, contactName,user.getEmployee_id());
        }
        totalSize += nSpage;
    }

    @Override
    protected void onDestroy() {
        if(presenter!=null){
            presenter.onDestroy();
        }
        super.onDestroy();
    }
}
