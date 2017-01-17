package com.bangware.shengyibao.customercontacts.view;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.customer.adapter.MyContactAdapter;
import com.bangware.shengyibao.customer.model.entity.Contacts;
import com.bangware.shengyibao.customer.model.entity.Customer;
import com.bangware.shengyibao.customer.view.CustomerInfoActivity;
import com.bangware.shengyibao.customercontacts.QuickBillingAdapter;
import com.bangware.shengyibao.customercontacts.presenter.CustomerContactsPresenter;
import com.bangware.shengyibao.customercontacts.presenter.impl.CustomerContactsPresenterImpl;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.ClearEditText;
import com.bangware.shengyibao.utils.customdialog.CommonDialog;
import com.bangware.shengyibao.view.OnRefreshListener;
import com.bangware.shengyibao.view.RefreshListView;


/**
 * 我的客户联系人界面
 * @author zcb
 *
 */
public class QueryQuickBilingActivity extends BaseActivity implements OnRefreshListener,CustomerContactsView{
	public String text="";
	private ImageView mCustomerConQuery_back;//回退按钮
	private RefreshListView mRefreshListView;
	private TextView mCustomerContactQuery_btn;//查询按钮
	private ImageView contactlist_textview;//通讯录按钮
	private String username,usernumber;
	private CommonDialog customDialog;
	private ClearEditText mClearEditText;
	private CustomerContactsPresenter presenter;
	int nPage=1;
	int nSpage=10;
	private int pageSize;
	public int totalSize = 0;
	String phone="";
	String contactName="";
	private List<Contacts> customerlist = new ArrayList<Contacts>();
	private List<String> mycontactlist=new ArrayList<String>();
	private MyContactAdapter myContactAdapter;
	private QuickBillingAdapter adapter;
	private User user;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_querycustomercontacts);
	SharedPreferences sharedPreferences=this.getSharedPreferences(User.SHARED_NAME, MODE_PRIVATE);

	user=AppContext.getInstance().readFromSharedPreferences(sharedPreferences);
	findView();//控件的初始化
	setListeners();//点击事件的初始化
}
private void setListeners() {
	// TODO Auto-generated method stub
	MyOnclickListener onclickListener=new MyOnclickListener();
	mCustomerConQuery_back.setOnClickListener(onclickListener);
	mCustomerContactQuery_btn.setOnClickListener(onclickListener);
	contactlist_textview.setOnClickListener(onclickListener);
}
private void findView() {
	// TODO Auto-generated method stub
	mCustomerConQuery_back=(ImageView) findViewById(R.id.customercontactquery_back);
	mCustomerContactQuery_btn=(TextView) findViewById(R.id.customercontactquery_btn);
	contactlist_textview=(ImageView) findViewById(R.id.contactlist_textview);
    mClearEditText=(ClearEditText) findViewById(R.id.customercontact_edit);
	mRefreshListView=(RefreshListView) findViewById(R.id.customercontactquery_ListView);

	presenter=new CustomerContactsPresenterImpl(this);
	presenter.loadCustomerContacts(user, nPage, nSpage, phone, contactName,user.getEmployee_id());
	adapter = new QuickBillingAdapter(this, customerlist);
	mRefreshListView.setAdapter(adapter);
	myContactAdapter=new MyContactAdapter(this,mycontactlist);

	mRefreshListView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View arg1, int position,
				long arg3) {
			Intent intent = new Intent(QueryQuickBilingActivity.this, CustomerInfoActivity.class);
			Bundle bundle = new Bundle();
			Contacts contact = (Contacts)adapterView.getItemAtPosition(position);
			Customer customer = contact.getCustomer();
			bundle.putSerializable("customer",  customer);
			//发送contacts_id
			bundle.putSerializable("contacts",contact);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	});
}
private class MyOnclickListener implements View.OnClickListener{

	@Override
	public void onClick(View v) {
		int cid = v.getId();
		//返回键处理
		if(cid == R.id.customercontactquery_back){
			loadingdialog.dismiss();
			finish();
		}
		if(cid==R.id.customercontactquery_btn)
		{
			customerContactsQuickQuery();
		}
		//调取通讯录
		if (cid == R.id.contactlist_textview){
			mycontactlist.clear();
			startActivityForResult(new Intent(
					Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 0);
		}
	}
}

	//读取手机通讯录的回调函数方法
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (resultCode == Activity.RESULT_OK) {
				ContentResolver reContentResolverol = getContentResolver();
				Uri contactData = data.getData();
				@SuppressWarnings("deprecation")
				Cursor cursor = managedQuery(contactData, null, null, null, null);
				cursor.moveToFirst();
				//获得DATA表中的名字
				username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				//条件为联系人ID
				String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

				// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
				Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
						null,
						null);
				if (phone != null) {
					String phoneNumber = null;
					while (phone.moveToNext()) {
						if (phone.moveToFirst()) {
							do {
								//遍历所有的联系人下面所有的电话号码
								phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
								//使用Toast技术显示获得的
								Log.e("phone", phoneNumber);
								// 对手机号码进行预处理（去掉号码前的+86、首尾空格、“-”号等）
								usernumber = phoneNumber;
								usernumber = usernumber.replaceAll("^(\\+86)", "");
								usernumber = usernumber.replaceAll("^(86)", "");
								usernumber = usernumber.replaceAll("-", "");
								usernumber = usernumber.replaceAll(" ", "");
								usernumber = usernumber.trim();
								mycontactlist.add(usernumber);
							} while (phone.moveToNext());
							if (mycontactlist.size() > 1) {
								int screenView = QueryQuickBilingActivity.this.getWindowManager().getDefaultDisplay().getWidth();
								customDialog = new CommonDialog(QueryQuickBilingActivity.this, screenView, R.layout.show_mycontact_dialog_layout, R.style.custom_dialog);
								customDialog.setCanceledOnTouchOutside(false);
								final ListView my_contact_list = (ListView) customDialog.findViewById(R.id.my_contact_listview);
								TextView my_contact_close = (TextView) customDialog.findViewById(R.id.my_contact_close);
								Log.e("mycontactlist", mycontactlist.size() + "");
								my_contact_list.setAdapter(myContactAdapter);
								customDialog.show();
								my_contact_close.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View view) {
										mycontactlist.clear();
										customDialog.dismiss();
									}
								});
								my_contact_list.setOnItemClickListener(new OnItemClickListener() {
									@Override
									public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
										usernumber = mycontactlist.get(i);
										mClearEditText.setText(usernumber);
										mycontactlist.clear();
										customDialog.dismiss();
									}
								});
							} else if (mycontactlist.size() == 1) {

								mClearEditText.setText(usernumber);
							}
						}
					}
				}
				/*if (!phone.isClosed()){
					phone.close();
				}*/
			}
		}catch (Exception e)
		{
			showTipsDialog();
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
			presenter.loadCustomerContacts(user, nPage, nSpage, phone, "",user.getEmployee_id());
		}

		p = Pattern.compile("[\u4e00-\u9fa5]*");
		m = p.matcher(text);
		if (m.matches()) {
			contactName = text;
			customerlist.clear();
			nPage = 1;
			totalSize = nSpage;
			phone = "";
			presenter.loadCustomerContacts(user, nPage, nSpage, "", contactName,user.getEmployee_id());
		}
	}

@Override
public void onDownPullRefresh() {
	// TODO Auto-generated method stub
	
}
@Override
public void onLoadingMore() {
	// TODO Auto-generated method stub
	nPage+=1;
	if(totalSize >= pageSize){
		mRefreshListView.hideFooterView();
		return;
	}else{
		presenter.loadCustomerContacts(user, nPage, nSpage, phone, contactName,user.getEmployee_id());
	}
	totalSize += nSpage;
}
@Override
public void doCustomerContactsLoadSuccess(List<Contacts> Contacts) {
	if(Contacts.size() > 0){
		customerlist.addAll(Contacts);
		pageSize = customerlist.get(0).getCustomer().getTotal_record_sum();
		adapter.notifyDataSetChanged();
	}else{
		showToast("暂无客户数据！");
		mRefreshListView.setVisibility(View.GONE);
		adapter.notifyDataSetChanged();
	}
	mRefreshListView.hideFooterView();
	mRefreshListView.setOnRefreshListener(QueryQuickBilingActivity.this);
}
@Override
protected void onDestroy() {
	if(presenter!=null){
	presenter.onDestroy();
	}
	super.onDestroy();
}
}
