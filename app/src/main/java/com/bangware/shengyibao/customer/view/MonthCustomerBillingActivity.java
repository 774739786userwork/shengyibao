package com.bangware.shengyibao.customer.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.customer.adapter.CustomerRightSectionedAdapter;
import com.bangware.shengyibao.customer.adapter.MonthCustomerBillingAdapter;
import com.bangware.shengyibao.customer.adapter.LeftMoreAdapter;
import com.bangware.shengyibao.customer.adapter.MonthCustomerLeftListAdapter;
import com.bangware.shengyibao.customer.adapter.OrderConditionSortAdapter;
import com.bangware.shengyibao.customer.model.entity.Customer;
import com.bangware.shengyibao.customer.model.entity.CustomerTypes;
import com.bangware.shengyibao.customer.presenter.CustomerBillingMonthPresenter;
import com.bangware.shengyibao.customer.presenter.RegionalAreaPresenter;
import com.bangware.shengyibao.customer.presenter.impl.CustomerBillingMonthPresenterImpl;
import com.bangware.shengyibao.model.Product;
import com.bangware.shengyibao.returngood.view.ReturnsProcessingActivity;
import com.bangware.shengyibao.returngood.view.ReturnsProductPopupWindow;
import com.bangware.shengyibao.view.OnRefreshListener;
import com.bangware.shengyibao.view.PinnedHeaderListView;
import com.bangware.shengyibao.view.RefreshListView;

/**
 * 主页月开单客户查询
 * @author ccssll
 *
 */
public class MonthCustomerBillingActivity extends BaseActivity implements OnRefreshListener,CustomerBillingMonthSalerRecordView{
	private ImageView Billinglist_back,Search_billingcustomer_img;

	private TextView BillingList_title_txt;			//左侧客户条件查询头部标题设置
	private LinearLayout BillingList_Customertype;//左侧头部选择切换
	private boolean leftToplistview = false;//左侧头部选择
	private ListView Billing_customerlist_toplist;	//左侧头部列表内容
	private ListView coustomer_left_listview;//主内容左侧列表内容
	private LeftMoreAdapter topadapter = null;//左侧头部切换数据适配
	private CustomerRightSectionedAdapter RightsectionedAdapter;//主内容右侧适配器
	private MonthCustomerLeftListAdapter LeftsectionedAdapter;//主界面左侧适配器
	private PinnedHeaderListView BillingCustomer_ListView;//主内容右侧列表内容
	private List<CustomerTypes> mainCustomerBillingList = new ArrayList<CustomerTypes>();
	private boolean isScroll = true;
	private int nPage = 1;
	private int nSpage = 20;
	private static int MaxNumer;
	private static int billingCustomerSum;
	private static int nobillingCustomer;
	private String compositor="";
	public int totalSize = 0;
	private boolean[] flagArray =new boolean[]{};
	private Customer[][] rightCustomerArray = new Customer[][]{};
	private CustomerBillingMonthPresenter billingMonthPresenter;

	public static String[] CUSTOMERLIST_TOPLIST;
	private int positionTemp = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_monthbilling_customer);
		findViews();
		setListeners();
	}

	//控件初始化绑定
	private void findViews() {
		// TODO Auto-generated method stub
		Billinglist_back = (ImageView) findViewById(R.id.Billinglist_back);
		Search_billingcustomer_img = (ImageView) findViewById(R.id.Search_billingcustomer_img);
		BillingList_Customertype = (LinearLayout) findViewById(R.id.BillingList_Customertype);
		BillingList_title_txt = (TextView) findViewById(R.id.BillingList_title_txt);

		Billing_customerlist_toplist = (ListView) findViewById(R.id.Billing_customerlist_toplist);

		Bundle bundle = this.getIntent().getExtras();
		MaxNumer = bundle.getInt("customerQuantity");
		billingCustomerSum = bundle.getInt("billingcustomerQuantity");
		nobillingCustomer = bundle.getInt("nobillingCustomer");

		CUSTOMERLIST_TOPLIST= new String[] {"本月未开单客户"+nobillingCustomer,"我送过货的客户"+MaxNumer,"本月已开单客户"+billingCustomerSum,"本月的大客户","我的大客户"};

		//默认加载本月未开单的客户
		BillingList_title_txt.setText(CUSTOMERLIST_TOPLIST[positionTemp]);
		billingMonthPresenter = new CustomerBillingMonthPresenterImpl(this);
		billingMonthPresenter.loadCustomerBillingMonthData(nPage, nobillingCustomer,3,"");


		//设置左侧列表数据
		topadapter = new LeftMoreAdapter(MonthCustomerBillingActivity.this,CUSTOMERLIST_TOPLIST,R.layout.search_more_list);
		Billing_customerlist_toplist.setAdapter(topadapter);


	}

	//控件点击监听事件绑定
	private void setListeners() {
		// TODO Auto-generated method stub
		MyOnClickListener clickListener = new MyOnClickListener();
		Billinglist_back.setOnClickListener(clickListener);
		Search_billingcustomer_img.setOnClickListener(clickListener);
		BillingList_Customertype.setOnClickListener(clickListener);

		TopListOnItemclick topListOnItemclick = new TopListOnItemclick();
		Billing_customerlist_toplist.setOnItemClickListener(topListOnItemclick);//左侧列表选中查询item点击

	}

	//左头部条目点击事件绑定
	private class TopListOnItemclick implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			positionTemp = position;
			topadapter.setSelectItem(positionTemp);
			Search_billingcustomer_img.setImageResource(R.drawable.ic_arrow_down_black);
			BillingList_title_txt.setText(CUSTOMERLIST_TOPLIST[positionTemp]);
			Billing_customerlist_toplist.setVisibility(View.GONE);
			leftToplistview = false;
			switch (positionTemp){
				case 0://我送货客户
					mainCustomerBillingList.clear();
					nPage = 1;
					totalSize = nSpage;
					billingMonthPresenter.loadCustomerBillingMonthData(nPage, MaxNumer,3,"");
					RightsectionedAdapter.notifyDataSetChanged();
					break;
				case 1://本月未开单客户
					mainCustomerBillingList.clear();
					nPage = 1;
					totalSize = nSpage;
					billingMonthPresenter.loadCustomerBillingMonthData(nPage, nobillingCustomer, 1,"");
					RightsectionedAdapter.notifyDataSetChanged();
					break;
				case 2://本月已开单客户
					mainCustomerBillingList.clear();
					nPage = 1;
					totalSize = nSpage;
					billingMonthPresenter.loadCustomerBillingMonthData(nPage, billingCustomerSum, 2,"");
					RightsectionedAdapter.notifyDataSetChanged();
					break;
				case 3://本月的大客户
					mainCustomerBillingList.clear();
					nPage=1;
					nSpage=20;
					billingMonthPresenter.loadCustomerBillingMonthData(nPage,nSpage,2,"total_sum");
					RightsectionedAdapter.notifyDataSetChanged();
					break;
				case 4://我的大客户
					mainCustomerBillingList.clear();
					nPage=1;
					nSpage=20;
					billingMonthPresenter.loadCustomerBillingMonthData(nPage,nSpage,3,"total_sum");
					RightsectionedAdapter.notifyDataSetChanged();
					break;
				default:
					break;
			}
		}
	}

	//各控件的点击事件绑定
	private class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int mID = v.getId();
			//返回按钮
			if(mID == R.id.Billinglist_back){
				MonthCustomerBillingActivity.this.finish();
			}
			//头部切换按钮
			if(mID == R.id.BillingList_Customertype){
				if(!leftToplistview){
					Search_billingcustomer_img.setImageResource(R.drawable.ic_arrow_up_black);
					Billing_customerlist_toplist.setVisibility(View.VISIBLE);
					topadapter.notifyDataSetChanged();
					leftToplistview = true;
				} else {
					Search_billingcustomer_img.setImageResource(R.drawable.ic_arrow_down_black);
					Billing_customerlist_toplist.setVisibility(View.GONE);
					leftToplistview = false;
				}
			}else {
				Search_billingcustomer_img.setImageResource(R.drawable.ic_arrow_down_black);
				Billing_customerlist_toplist.setVisibility(View.GONE);
				leftToplistview = false;
			}
		}
	}

	//手机屏幕返回手指按下事件0
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(leftToplistview == true){
				Search_billingcustomer_img.setImageResource(R.drawable.ic_arrow_down_black);
				Billing_customerlist_toplist.setVisibility(View.GONE);
				leftToplistview = false;
			}else {
				MonthCustomerBillingActivity.this.finish();
			}

		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(leftToplistview == true){
				Search_billingcustomer_img.setImageResource(R.drawable.search_customer);
				Billing_customerlist_toplist.setVisibility(View.GONE);
				leftToplistview = false;
			}
			break;

		default:
			break;
		}
		return false;
	}

	@Override
	public void onDownPullRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadingMore() {
		// TODO Auto-generated method stub
//		nPage+=1;
//		if(totalSize >= MaxNumer){
////			BillingCustomer_ListView.hideFooterView();
//			return;
//		}else{
//			switch (positionTemp){
//				case 0://我送货客户
//					billingMonthPresenter.loadCustomerBillingMonthData(nPage, nSpage, 1,"");
//					break;
//				case 1://本月未开单客户
//					billingMonthPresenter.loadCustomerBillingMonthData(nPage, nSpage, 3,"");
//					break;
//				case 2://本月已开单客户
//					billingMonthPresenter.loadCustomerBillingMonthData(nPage, nSpage, 2,"");
//					break;
//				default:
//					break;
//			}
//		}
//		totalSize += nSpage;
	}

	@Override
	public void queryCustomerSalerRecord(List<CustomerTypes> customerTypes) {
		// TODO Auto-generated method stub
			mainCustomerBillingList.addAll(customerTypes);
			int CustomerTypecount=customerTypes.size();
			if (CustomerTypecount>0)
			{
				rightCustomerArray=new Customer[CustomerTypecount][];
				flagArray=new boolean[CustomerTypecount];
				for (int i=0;i<CustomerTypecount;i++)
				{
					CustomerTypes customerTypes1=customerTypes.get(i);
					if (i==0)
					{
						flagArray[i]=true;
					}else
					{
						flagArray[i]=false;
					}
					rightCustomerArray[i]=new Customer[customerTypes1.getCustomerList().size()];
					for (int j = 0; j < customerTypes1.getCustomerList().size(); j++) {
						rightCustomerArray[i][j] = customerTypes1.getCustomerList().get(j);
					}
				}
			}
			initList();
			RightsectionedAdapter.notifyDataSetChanged();
	/*	BillingCustomer_ListView.hideFooterView();
		BillingCustomer_ListView.setOnRefreshListener(MonthCustomerBillingActivity.this);*/
	}

	private void initList() {
		coustomer_left_listview= (ListView) findViewById(R.id.coustomer_left_listview);
		BillingCustomer_ListView = (PinnedHeaderListView) findViewById(R.id.coustomer_pinnedListView);
		RightsectionedAdapter=new CustomerRightSectionedAdapter(this,mainCustomerBillingList,rightCustomerArray);
		BillingCustomer_ListView.setAdapter(RightsectionedAdapter);
		LeftsectionedAdapter=new MonthCustomerLeftListAdapter(this,mainCustomerBillingList,flagArray);
		coustomer_left_listview.setAdapter(LeftsectionedAdapter);
		coustomer_left_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				isScroll = false;
				for (int i = 0; i < rightCustomerArray.length; i++) {
					if (i == position) {
						flagArray[i] = true;
					} else {
						flagArray[i] = false;
					}
				}
				LeftsectionedAdapter.notifyDataSetChanged();
				int rightSection = 0;
				for (int i = 0; i < position; i++) {
					rightSection += RightsectionedAdapter.getCountForSection(i) + 1;
				}
				BillingCustomer_ListView.setSelection(rightSection);
			}
		});

			BillingCustomer_ListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView arg0, int scrollState) {
				switch (scrollState) {
					// 当不滚动时
					case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
						// 判断滚动到底部
						if (BillingCustomer_ListView.getLastVisiblePosition() == (BillingCustomer_ListView.getCount() - 1)) {
							coustomer_left_listview.setSelection(ListView.FOCUS_DOWN);
						}
						// 判断滚动到顶部
						if (BillingCustomer_ListView.getFirstVisiblePosition() == 0) {
							coustomer_left_listview.setSelection(0);
						}
						break;
				}
			}

			int y = 0;
			int x = 0;
			int z = 0;

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (isScroll && rightCustomerArray!=null) {
					for (int i = 0; i < rightCustomerArray.length; i++) {
						if (i == RightsectionedAdapter.getSectionForPosition(BillingCustomer_ListView.getFirstVisiblePosition())) {
							flagArray[i] = true;
							x = i;
						} else {
							flagArray[i] = false;
						}
					}
					if (x != y) {
						LeftsectionedAdapter.notifyDataSetChanged();
						y = x;
						if (y == coustomer_left_listview.getLastVisiblePosition()) {
//                            z = z + 3;
							coustomer_left_listview.setSelection(z);
						}
						if (x == coustomer_left_listview.getFirstVisiblePosition()) {
//                            z = z - 1;
							coustomer_left_listview.setSelection(z);
						}
						if (firstVisibleItem + visibleItemCount == totalItemCount - 1) {
							coustomer_left_listview.setSelection(ListView.FOCUS_DOWN);
						}
					}
				} else {
					isScroll = true;
				}
			}
		});
		BillingCustomer_ListView.setOnItemClickListener(new PinnedHeaderListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int section, int position, long id) {
				Intent intent = new Intent(MonthCustomerBillingActivity.this, CustomerInfoActivity.class);
				//用Bundle传递数据
				Customer customer = rightCustomerArray[section][position];
				Bundle bundle = new Bundle();
				bundle.putSerializable("customer", customer);
				intent.putExtras(bundle);
				startActivity(intent);
			}

			@Override
			public void onSectionClick(AdapterView<?> adapterView, View view, int section, long id) {

			}
		});
	}

	@Override
	public void showLoadFailureMsg(String errorMessage) {
		// TODO Auto-generated method stub

	}

	public void onDestroy(){
		super.onDestroy();
		if(this.billingMonthPresenter!=null)
			this.billingMonthPresenter.destroy();
	}
}
