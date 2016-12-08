package com.bangware.shengyibao.main.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bangware.shengyibao.activity.CustomProgressDialog;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.customer.view.AddCustomerActivity;
import com.bangware.shengyibao.customer.view.MonthCustomerBillingActivity;
import com.bangware.shengyibao.customercontacts.view.QueryQuickBilingActivity;
import com.bangware.shengyibao.daysaleaccount.view.SaleAccountListActivity;
import com.bangware.shengyibao.deliverynote.view.DeliveryNoteQueryActivity;
import com.bangware.shengyibao.ladingbilling.view.LadingbillingQueryActivity;
import com.bangware.shengyibao.main.model.entity.ThisMonthSummary;
import com.bangware.shengyibao.main.model.entity.ToDaySummary;
import com.bangware.shengyibao.main.presenter.MainPresenter;
import com.bangware.shengyibao.main.presenter.impl.MainPresenterImpl;
import com.bangware.shengyibao.manager.ScreenObserverManager;
import com.bangware.shengyibao.purchaseorder.view.PurchaseOrderActivity;
import com.bangware.shengyibao.shopcart.view.ShopCartAcitivity;
import com.bangware.shengyibao.updateversion.VersionUpdateView;
import com.bangware.shengyibao.updateversion.model.entity.VersionBean;
import com.bangware.shengyibao.updateversion.presenter.UpdateVersionPresenter;
import com.bangware.shengyibao.updateversion.presenter.UpdateVersionPresenterImpl;
import com.bangware.shengyibao.updateversion.service.UpdateVersionService;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.VersionUtil;
import com.jauker.widget.BadgeView;

/**
 * 销售页面
 * @author luming.tang
 *
 */
public class FragmentSaler extends Fragment implements MainView,VersionUpdateView
{
	private UpdateVersionPresenter versionPresenter;
	private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 0;
	private VersionBean newVersionBean = new VersionBean();

	private Intent intent;
	protected CustomProgressDialog loadingdialog;
    private MainPresenter presenter;
    private ScreenObserverManager observerManager;
    private TextView todaySaleDate = null;
    private TextView todaySalerMoney = null;
    private TextView monthSalerTime = null;
    private TextView monthSalerTop = null;
    private TextView customerQuantity = null;
    private TextView billing_customer = null;
    private TextView saler_person = null;
	private LinearLayout fragment_title,purchase_linear;
    private BadgeView loadingBillImageView,deliveryNoteImageView,purcharOrderImageView,returnOrderImageView;
    private LinearLayout queryDelivery_layout,loadingbill_layout,query_returnGoodsLayout,saler_mycustomer,sale_ranking_linearLayout;
	private LinearLayout quickBlling_layout,addCustomer_layout,customerContact_layout;
    public static String Date=null;
    private int maxNumber,billingcustomerQuantity,nobillingCustomer;
	private View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		if (view == null){
			view = inflater.inflate(R.layout.fragment_saler, container, false);
			initView(view);
		}
		if(view!=null)
		{
			return view;
		}
		return super.onCreateView(inflater, container, savedInstanceState);

    }

	private void findById(){
		todaySaleDate = (TextView) getView().findViewById(R.id.sale_date);
		todaySalerMoney = (TextView) getView().findViewById(R.id.today_sale_sum);
		monthSalerTime = (TextView) getView().findViewById(R.id.thismonth_saler_time);
		monthSalerTop = (TextView) getView().findViewById(R.id.thismonth_saler_top);
		customerQuantity = (TextView) getView().findViewById(R.id.customer_quantity);
		billing_customer = (TextView) getView().findViewById(R.id.thismonth_customer_summary);
		fragment_title= (LinearLayout) getView().findViewById(R.id.fragment_title);
		purchase_linear= (LinearLayout) getView().findViewById(R.id.purchase_linear);
		//送货单查询
		queryDelivery_layout = (LinearLayout) getView().findViewById(R.id.query_deliveryNote);
		//提货单查询
		loadingbill_layout = (LinearLayout) getView().findViewById(R.id.loadingbill_layout);
		//退货单查询
		query_returnGoodsLayout = (LinearLayout) getView().findViewById(R.id.query_returnGoodsLayout);
		//开单客户查询
		saler_mycustomer = (LinearLayout) getView().findViewById(R.id.saler_mycustomer);
		sale_ranking_linearLayout = (LinearLayout) getView().findViewById(R.id.sale_ranking_linearLayout);
		quickBlling_layout = (LinearLayout) getView().findViewById(R.id.quick_billing);
		addCustomer_layout = (LinearLayout) getView().findViewById(R.id.add_customer);
		customerContact_layout = (LinearLayout) getView().findViewById(R.id.customer_contact);

		//获取业务员姓名
		saler_person = (TextView) getView().findViewById(R.id.saler_person);
		Bundle bundle = getActivity().getIntent().getExtras();
		if (bundle != null){
			String userRealName = bundle.getString("user_real_name");
			saler_person.setText(userRealName);
		}else {
			saler_person.setText(AppContext.getInstance().getUser().getUser_realname());
		}


		//初始化Presenter
		presenter=new MainPresenterImpl(this);
		presenter.loadThisMonthSummary();
		presenter.loadToDaySummary();

		//版本更新调用
		versionPresenter = new UpdateVersionPresenterImpl(this);
		versionPresenter.versionUpdate();

		//锁屏监听回调方法
		observerManager = new ScreenObserverManager(getActivity());
		observerManager.requestScreenStateUpdate(new ScreenObserverManager.ScreenStateListener() {

			//屏幕开启状态
			@Override
			public void onScreenOn() {
//				loadingdialog.show();
				presenter.loadThisMonthSummary();
				presenter.loadToDaySummary();
			}

			//屏幕锁屏状态
			@Override
			public void onScreenOff() {
				// TODO Auto-generated method stub

			}
		});
	}

	private void setListener() {
		MyOnClickListener listener=new MyOnClickListener();
		queryDelivery_layout.setOnClickListener(listener);
		loadingbill_layout.setOnClickListener(listener);
		query_returnGoodsLayout.setOnClickListener(listener);
		saler_mycustomer.setOnClickListener(listener);
		purchase_linear.setOnClickListener(listener);
		sale_ranking_linearLayout.setOnClickListener(listener);
		quickBlling_layout.setOnClickListener(listener);
		addCustomer_layout.setOnClickListener(listener);
		customerContact_layout.setOnClickListener(listener);
	}

	public class MyOnClickListener implements View.OnClickListener
	{

		@Override
		public void onClick(View view) {
			switch (view.getId())
			{
				case R.id.query_deliveryNote:
					intent = new Intent(getActivity(), DeliveryNoteQueryActivity.class);
					startActivity(intent);
					break;
				case R.id.loadingbill_layout:
					intent=new Intent(getActivity(),LadingbillingQueryActivity.class);
					startActivity(intent);
					break;
				case R.id.purchase_linear:
					intent=new Intent(getActivity(), PurchaseOrderActivity.class);
					startActivity(intent);
					break;
				case R.id.query_returnGoodsLayout:
					intent = new Intent(getActivity(), SaleAccountListActivity.class);
					startActivity(intent);
					break;
				/*case R.id.sale_ranking_linearLayout:
					intent=new Intent(getActivity(), SaleRankingActivity.class);
					startActivity(intent);
					break;*/
				case R.id.quick_billing:
					intent = new Intent(getActivity(), ShopCartAcitivity.class);
					startActivity(intent);
					break;
				case R.id.saler_mycustomer:
					intent = new Intent(getActivity(), MonthCustomerBillingActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("customerQuantity", maxNumber);
					bundle.putInt("billingcustomerQuantity", billingcustomerQuantity);
					bundle.putInt("nobillingCustomer", nobillingCustomer);
					intent.putExtras(bundle);
					startActivity(intent);
					break;
				case R.id.add_customer:
					intent = new Intent(getActivity(), AddCustomerActivity.class);
					startActivity(intent);
					break;
				case R.id.customer_contact:
					intent = new Intent(getActivity(), QueryQuickBilingActivity.class);
					startActivity(intent);
					break;
			}
		}
	}
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

		findById();
		setListener();
    }

    private void initView(View view) {
        loadingBillImageView = new BadgeView(view.getContext());
        loadingBillImageView.setTargetView(view.findViewById(R.id.loadingbill_icon));
        
        deliveryNoteImageView = new BadgeView(view.getContext());
        deliveryNoteImageView.setTargetView(view.findViewById(R.id.deliverynote_icon));
        
        purcharOrderImageView = new BadgeView(view.getContext());
        purcharOrderImageView.setTargetView(view.findViewById(R.id.purchaseorder_icon));

        returnOrderImageView = new BadgeView(view.getContext());
        returnOrderImageView.setTargetView(view.findViewById(R.id.returnorder_icon));
 	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//停止监听screen状态
		observerManager.stopScreenStateUpdate();
		if(presenter!=null){
			presenter.destroy();
		}
		/*if (versionPresenter != null){
			versionPresenter.destroy();
		}*/
	}

	@Override
	public void showLoading(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showLoading() {
		// TODO Auto-generated method stub
	}

	@Override
	public void hideLoading() {
		// TODO Auto-generated method stub
	}

	@Override
	public void showMessage(String errorMessage) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
	}

	@Override
	public void doTodaySummaryLoadSuccess(ToDaySummary bean) {
		// TODO Auto-generated method stub
		if(bean != null){
//			loadingdialog.dismiss();
			Date=bean.getTodaytime();
			todaySaleDate.setText(Date);  //当天时间
			todaySalerMoney.setText("销售金额：¥"+bean.getTodaysaler()+"元");//当天销售额
			loadingBillImageView.setBadgeCount(bean.getLadingbill_sum()); //提货单记录数
			deliveryNoteImageView.setBadgeCount(bean.getDeliverynote_sum());//送货单记录数
	        returnOrderImageView.setBadgeCount(bean.getReturngood_sum());  //退货单记录数
			purcharOrderImageView.setBadgeCount(bean.getOrdernote_sum());//订货单记录数
		}
		else{
			Toast.makeText(getActivity(), "暂无数据！", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void doThisMonthSummaryLoadSuccess(ThisMonthSummary bean) {
		// TODO Auto-generated method stub
		if(bean != null){
			monthSalerTime.setText(bean.getMonthtime().substring(0, 7));	 //获取本月时间
			monthSalerTop.setText(bean.getMonthtop());      //本月的销售排名
			customerQuantity.setText("共计"+bean.getCustomersum()+"家");//我的总客户数
			billing_customer.setText("开单客户"+bean.getBillingcustomer()+"家");//开单客户数

			maxNumber = Integer.valueOf(bean.getCustomersum());
			billingcustomerQuantity = Integer.valueOf(bean.getBillingcustomer());
			
			nobillingCustomer = maxNumber - billingcustomerQuantity;
		}
		else{
			Toast.makeText(getActivity(), "暂无数据！", Toast.LENGTH_SHORT).show();
		}
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

}
