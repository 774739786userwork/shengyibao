package com.bangware.shengyibao.shopcart.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.activity.ShowShopCartPopupWindow;
import com.bangware.shengyibao.customer.adapter.MyContactAdapter;
import com.bangware.shengyibao.customer.model.entity.Contacts;
import com.bangware.shengyibao.customer.model.entity.Customer;
import com.bangware.shengyibao.customercontacts.CustomerContactAdapter;
import com.bangware.shengyibao.customercontacts.presenter.CustomerContactsPresenter;
import com.bangware.shengyibao.customercontacts.presenter.impl.CustomerContactsPresenterImpl;
import com.bangware.shengyibao.customercontacts.view.CustomerContactsView;
import com.bangware.shengyibao.customercontacts.view.QueryQuickBilingActivity;
import com.bangware.shengyibao.deliverynote.model.entity.DeliveryNote;
import com.bangware.shengyibao.deliverynote.model.entity.DeliveryNoteGoods;
import com.bangware.shengyibao.model.Product;
import com.bangware.shengyibao.shopcart.adapter.ShopCartProductListAdapter;
import com.bangware.shengyibao.shopcart.model.entity.ShopCart;
import com.bangware.shengyibao.shopcart.model.entity.ShopCartGoods;
import com.bangware.shengyibao.shopcart.presenter.ShopCartPresenter;
import com.bangware.shengyibao.shopcart.presenter.impl.ShopCartPresenterImpl;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.ClearEditText;
import com.bangware.shengyibao.utils.DensityUtil;
import com.bangware.shengyibao.utils.customdialog.CommonDialog;
import com.bangware.shengyibao.view.OnRefreshListener;
import com.bangware.shengyibao.view.RefreshListView;
import com.jauker.widget.BadgeView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;


public class ShopCartAcitivity extends BaseActivity implements ShopCartView,CustomerContactsView,OnRefreshListener {

	public static String text="";
	String phone="";
	String contactName="";
	int nPage=1;
	int nSpage=10;
	private int pageSize;
	public int totalSize = 0;
	private List<Contacts> customerlist = new ArrayList<Contacts>();//客户联系人列表
	private ListView productListView;//产品列表
	private CustomerContactAdapter adapter;//客户联系人适配器
	private ImageView goBack;//回退按钮
	private RelativeLayout shopCartIcon;
	private RelativeLayout detail_buy_board;
	private LinearLayout DeliveryNoteTitle;
	private Button toSettlementBtn;//结算按钮
	private int height;//屏幕的高度
	private List<Product> list = new ArrayList<Product>();
	private ShopCartProductListAdapter mAdapter= null;
	private ProductPopupWindow mPopupWindow;
	private ShowShopCartPopupWindow mShowShopCartPopupWindow;
	private BadgeView deliveryNoteTotalVolume;
	private TextView deliveryNoteAmountText;
	private CommonDialog customDialog;

	private TextView mCustomerContactQuery_btn;//查询按钮
	private TextView ShopCart_Customer_Name;//查询的店面名称
	private ImageView contactlist_textview;//通讯录按钮
	private RefreshListView mRefreshListView;
	private ClearEditText mClearEditText;//输入框
	private CustomerContactsPresenter CCPresenter;//客户联系人Presenter
	private String username,usernumber;

	private long mExitTime = System.currentTimeMillis();
	private Contacts contact;
	private DeliveryNote deliveryNote;
	private ShopCartGoods shopCartGoods;
	private ShopCartPresenter presenter;
	private List<DeliveryNoteGoods> goodsList;
	private List<String> mycontactlist=new ArrayList<String>();
	private MyContactAdapter myContactAdapter;
	private Customer customer;

	private String voiceName;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		height=wm.getDefaultDisplay().getHeight();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_deliverynote);

		deliveryNoteTotalVolume = new BadgeView(ShopCartAcitivity.this);
		deliveryNoteTotalVolume.setTargetView(findViewById(R.id.shopCartIcon));
		myContactAdapter=new MyContactAdapter(this,mycontactlist);
		findViews();
		setListeners();
		
		Bundle bundle = this.getIntent().getExtras();

		//接收送货单对象
		if (bundle != null) {
			deliveryNote = (DeliveryNote) bundle.getSerializable("deliveryNote");
			//接收客户对象
			customer = (Customer)bundle.getSerializable("customer");
			ShopCart_Customer_Name.setText(customer.getName());
			//接收contacts_id联系人对象
			contact=(Contacts) bundle.getSerializable("contacts");
		}
		//加载产品Presenter
		presenter = new ShopCartPresenterImpl(this);
		//加载客户联系人Presenter
		CCPresenter=new CustomerContactsPresenterImpl(this);
		//设置购物车中的客户信息
		presenter.getShopCart().setCustomer(customer);
//		updateCustomerInfo(customer);
		presenter.loadStocks();
		mAdapter = new ShopCartProductListAdapter(presenter.getShopCart(),list, this);
		productListView.setAdapter(mAdapter);
		adapter = new CustomerContactAdapter(this, customerlist);
		mRefreshListView.setAdapter(adapter);
		mRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View arg1, int position,
									long arg3) {
				// TODO Auto-generated method stub
				//用Bundle传递数据到联系人详情界面
				contact = (Contacts)adapterView.getItemAtPosition(position);
				ShopCart_Customer_Name.setText(contact.getCustomer().getName());
				customer=contact.getCustomer();
				//设置购物车中的客户信息
				presenter.getShopCart().setCustomer(customer);
			}
		});
	}


	private void findViews(){
		deliveryNoteAmountText = (TextView)findViewById(R.id.ShopCart_Amount);
		goBack = (ImageView)findViewById(R.id.DeliveryNote_Goback);
		shopCartIcon=(RelativeLayout) findViewById(R.id.shopCartIcon);
		detail_buy_board=(RelativeLayout) findViewById(R.id.detail_buy_board);
		DeliveryNoteTitle=(LinearLayout) findViewById(R.id.DeliveryNoteTitle);
		toSettlementBtn =(Button)findViewById(R.id.ShopCart_toSettlementBtn);
		productListView = (ListView)findViewById(R.id.ProductListView);

		mCustomerContactQuery_btn= (TextView) findViewById(R.id.customercontactquery_btns);
		contactlist_textview= (ImageView) findViewById(R.id.contactlist_textviews);
		mRefreshListView= (RefreshListView) findViewById(R.id.customercontact_query_ListView);
		mClearEditText= (ClearEditText) findViewById(R.id.customercontact_edits);
		ShopCart_Customer_Name= (TextView) findViewById(R.id.ShopCart_Customer_Names);
	}
	
	private void setListeners(){
		MyOnClickLietener myonclick = new MyOnClickLietener();
		goBack.setOnClickListener(myonclick);
		toSettlementBtn.setOnClickListener(myonclick);
		productListView.setOnItemClickListener(new ItemClickListener());
		shopCartIcon.setOnClickListener(myonclick);
		contactlist_textview.setOnClickListener(myonclick);
		mCustomerContactQuery_btn.setOnClickListener(myonclick);
	}


   //加载客户联系人列表
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
		mRefreshListView.setOnRefreshListener(ShopCartAcitivity.this);
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
			CCPresenter.loadCustomerContacts(AppContext.getInstance().getUser(), nPage, nSpage, phone, contactName,"");
		}
		totalSize += nSpage;
	}


	private class MyOnClickLietener implements View.OnClickListener {
		public void onClick(View view) {
			int mID = view.getId();
			switch(mID){
				case R.id.DeliveryNote_Goback:
					dismissLoading();
					ShopCartAcitivity.this.finish();
					break;
					//点击购物篮图标显示已选的产品
				case R.id.shopCartIcon:
					if (Integer.parseInt((String)(deliveryNoteTotalVolume.getText()))>0) {
						mShowShopCartPopupWindow=new ShowShopCartPopupWindow(ShopCartAcitivity.this,presenter);
						//mShowShopCartPopupWindow.getContentView().getHeight();
						int goodsCount = presenter.getShopCart().getAllGoodsList().size();
						int popWindowHeight= DensityUtil.dip2px(ShopCartAcitivity.this, (goodsCount*49)+40+58);
						int d = popWindowHeight ;
						int h=detail_buy_board.getHeight()+productListView.getHeight()+DensityUtil.dip2px(ShopCartAcitivity.this,0);
						
						if (d>h) {
							mShowShopCartPopupWindow.setHeight(productListView.getHeight());
							mShowShopCartPopupWindow.showAsDropDown(view, 0, -h);
							
						}else{
						mShowShopCartPopupWindow.showAsDropDown(view, 0,-d);
						}
						backgroundAlpha(0.4f);
						mShowShopCartPopupWindow.setOnDismissListener(new OnDismissListener() {
							
							@Override
							public void onDismiss() {
								// TODO Auto-generated method stub
								backgroundAlpha(1f);
							}
						});
					}
					
					break;
				case R.id.ShopCart_toSettlementBtn:
					if(presenter.getShopCart().getCustomer()!=null){
						//通过bundle传递对象数据到下级页面
						Intent intent = new Intent(ShopCartAcitivity.this, SettlementActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("deliveryNote",  (Serializable)deliveryNote);
						bundle.putSerializable("shopCart",  (Serializable)presenter.getShopCart());
//						bundle.putString("customer_id", Customer_Id.getText().toString());
						bundle.putSerializable("contacts", contact);
						intent.putExtras(bundle);
						startActivity(intent);
					}else{
						Toast.makeText(ShopCartAcitivity.this, "请选择开单客户！", Toast.LENGTH_SHORT).show();
					}
					break;
				case R.id.customercontactquery_btns:
					mRefreshListView.setVisibility(View.VISIBLE);
					customerContactsQuickQuery();
					break;
				case R.id.contactlist_textviews:
					mycontactlist.clear();
					startActivityForResult(new Intent(
							Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 0);
					break;
			}
		}
	}
	//读取手机通讯录的回调函数方法
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
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
			if (phone!=null){
				String phoneNumber=null;
				while (phone.moveToNext()) {
					if(phone.moveToFirst())
					{
						do
						{
							//遍历所有的联系人下面所有的电话号码
							phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							//使用Toast技术显示获得的
							Log.e("phone",phoneNumber);
							usernumber = phoneNumber;
							// 对手机号码进行预处理（去掉号码前的+86、首尾空格、“-”号等）
							usernumber = usernumber.replaceAll("^(\\+86)", "");
							usernumber = usernumber.replaceAll("^(86)", "");
							usernumber = usernumber.replaceAll("-", "");
							usernumber = usernumber.replaceAll(" ", "");
							usernumber = usernumber.trim();
							mycontactlist.add(usernumber);

						}while(phone.moveToNext());
						if (mycontactlist.size() > 1) {
							int screenView = ShopCartAcitivity.this.getWindowManager().getDefaultDisplay().getWidth();
							customDialog = new CommonDialog(ShopCartAcitivity.this,screenView, R.layout.show_mycontact_dialog_layout,R.style.custom_dialog);
							customDialog.setCanceledOnTouchOutside(false);
							final ListView my_contact_list= (ListView) customDialog.findViewById(R.id.my_contact_listview);
							TextView my_contact_close= (TextView) customDialog.findViewById(R.id.my_contact_close);
							Log.e("mycontactlist",mycontactlist.size()+"");
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
									usernumber=mycontactlist.get(i);
									mClearEditText.setText(usernumber);
									mycontactlist.clear();
									customDialog.dismiss();
								}
							});
						}else if (mycontactlist.size()==1){
							usernumber = usernumber.trim();
							mClearEditText.setText(usernumber);
						}
					}
				}
			}
				/*if (!phone.isClosed()){
					phone.close();
				}*/
		}

	}

	public void customerContactsQuickQuery(){
		text = mClearEditText.getText().toString();
		if(TextUtils.isEmpty(text)){
			showToast("查询条件不能为空！");
			mRefreshListView.setVisibility(View.GONE);
			return;
		}
       if (text.length()==8||text.length()>=11){
		Pattern p = Pattern.compile("[0-9]*");
		Matcher m = p.matcher(text);
		if (m.matches()) {
			phone = text;
			customerlist.clear();
			nPage = 1;
			totalSize = nSpage;
			contactName = "";
			CCPresenter.loadCustomerContacts(AppContext.getInstance().getUser(), nPage, nSpage, phone, contactName,"");
		}

		p = Pattern.compile("[\u4e00-\u9fa5]*");
		m = p.matcher(text);
		if (m.matches()) {
			contactName = text;
			customerlist.clear();
			nPage = 1;
			totalSize = nSpage;
			phone = "";
			CCPresenter.loadCustomerContacts(AppContext.getInstance().getUser(), nPage, nSpage, "", contactName,"");
		}
	}else
	   {
		   mRefreshListView.setVisibility(View.GONE);
		   showToast("号码格式不正确，请重新输入！");
	   }
	}
	/** 
     * 设置添加屏幕的背景透明度 
     * @param bgAlpha 
     */  
	 private void backgroundAlpha(float bgAlpha)  
	    {  
	        WindowManager.LayoutParams lp = getWindow().getAttributes();  
	        lp.alpha = bgAlpha; //0.0-1.0  
	        getWindow().setAttributes(lp);  
	    }  
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			dismissLoading();
			this.finish();
		}
		return true;
	}
	
	/**
	 * ListView中Item的点击事件处理
	 * 当dialog窗口点击确认关闭时，
	 * 读取购物车中的商品清单，更新listView的数据源并执行notifyDataSetChanged刷新页面
	 * @author luming.tang
	 *
	 */
	class  ItemClickListener implements OnItemClickListener     
	{     
		public void onItemClick(AdapterView<?> adapterView,View view,final int position,long row) 
		{     
			if ((System.currentTimeMillis() - mExitTime) > 1000) {
				Product item=(Product) adapterView.getItemAtPosition(position);  
				if(item.getStock()>0){
					mPopupWindow = new ProductPopupWindow(ShopCartAcitivity.this, item, presenter);
					mPopupWindow.showPopupWindow(view);
					backgroundAlpha(0.4f);  
					mPopupWindow.setOnDismissListener(new OnDismissListener(){
						@Override
						public void onDismiss() {
							backgroundAlpha(1f);
						}
					});
					mExitTime = System.currentTimeMillis();
				}
			}
		}     
		
	}    
	
    public void showMessage(String message){
    	showToast(message);
    }
	public void doChanged(ShopCart shopCart) {
		// TODO Auto-generated method stub

		double total_sum = shopCart.getTotalAmount();
		deliveryNoteAmountText.setText("¥"+total_sum+"元");
		deliveryNoteTotalVolume.setBadgeCount(shopCart.getTotalVolumes());
		if(shopCart.getTotalVolumes()>0){
			toSettlementBtn.setVisibility(View.VISIBLE);
		}else{
			toSettlementBtn.setVisibility(View.GONE);
		}
		mAdapter.notifyDataSetChanged();
	}
	private Product getProductFromList(List<Product> list, String id){
		for(Product p: list){
			if(p.getId().equals(id)){
				return p;
			}
		}
		return null;
	}
	/**
	 * 加载库存产品数据
	 */
	public void loadProductStock(List<Product> products){
		if(products.size() == 0){
			showAlertDialog();
		}else{
			list.clear();
			//根据库存排序
			Collections.sort(products,new Comparator<Product>(){
			   public int compare(Product p1, Product p2) {
			         return Integer.valueOf(p2.getStock()).compareTo(Integer.valueOf(p1.getStock()));
			    }
			});
			list.addAll(products);
			mAdapter.notifyDataSetChanged();
			
			if(deliveryNote!=null){
				goodsList = deliveryNote.getGoodsList();
				for(DeliveryNoteGoods g:goodsList){
					shopCartGoods=new ShopCartGoods();
					shopCartGoods.setProduct(g.getProduct());
					shopCartGoods.setPrice(g.getPrice());
					shopCartGoods.setSalesVolume(g.getSalesVolume());
					shopCartGoods.setGiftsVolume(g.getGiftsVolume());
					
					shopCartGoods.setAmount(g.getSalesVolume()+g.getGiftsVolume());
					Product product = getProductFromList(products, g.getProduct().getId());
					if(product!=null){
                        shopCartGoods.setGiftsVolume(g.getGiftsVolume());
                        shopCartGoods.setSalesVolume(g.getSalesVolume());
						shopCartGoods.setP_totalforegift(g.getP_totalforegift());
						product.setStock(g.getProduct().getStock());
						product.setPrice(shopCartGoods.getPrice());
						shopCartGoods.setProduct(product);
					}
					presenter.addGoods(shopCartGoods);
				}
			}
		}
	}

	private void showAlertDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(ShopCartAcitivity.this,R.style.dialog);
		builder.setTitle("提示");
        builder.setMessage("当前暂无提货单！");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
       
        builder.setPositiveButton("确定！", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	dismissLoading();
            	finish();
                dialog.cancel();
            }
       });
       builder.create().show();
	}
	
	public void onDestroy(){
		super.onDestroy();
		if(this.mAdapter!=null)
			this.mAdapter.destory();
		if(presenter!=null)
			presenter.destroy();
	}



}
