package com.bangware.shengyibao.customer.view;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.bangware.shengyibao.activity.ProvinceCityAreaActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.Constants_Camera;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.customer.adapter.CaremaAdapter;
import com.bangware.shengyibao.customer.model.entity.Contacts;
import com.bangware.shengyibao.customercontacts.presenter.CustomerContactsPresenter;
import com.bangware.shengyibao.customercontacts.presenter.impl.CustomerContactsPresenterImpl;
import com.bangware.shengyibao.customercontacts.view.CustomerContactsView;
import com.bangware.shengyibao.main.view.MainActivity;
import com.bangware.shengyibao.manager.shoptypeflowlayout.view.FlowLayoutActivity;
import com.bangware.shengyibao.net.ThreadPoolUtils;
import com.bangware.shengyibao.thread.HttpPostThread;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.CommonUtil;


/**
 * 添加客户页
 * @author ccssll
 *
 */
public class AddCustomerActivity extends BaseActivity implements CustomerContactsView {
	//图片上传常量定义
	private Context context;
	private GridView caremaView; 					  //照片显示区域
	private static final int CRAEMA_REQUEST_CODE = 0; //拍照请求码
	private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
	private boolean candelete = false; //是否可以删除照片
	private String defaultPhotoAddress = null; //拍照生成默认照片的绝对路径
	private String photoFolderAddress = null; //存放照片的文件夹
	private String pztargetPath = null;
	private ArrayList<String> listPhotoNames = null;
	private CaremaAdapter cadapter = null;
	private int screenWidth = 0; //屏幕宽度
	private User user;

	//各控件定义
	private ImageView customer_add_back,upload_image;
	private TextView btn_submit;
	private EditText lng_edit,lat_edit,store_name,link_man,mobile_et,
			cellphone_et,contact_et,mobile_two_et,detailAddr_et,provice_edit,
						city_edit,district_edit,street_edit,kind_ids_et;
	private TextView type_et,add_saler_area,add_saler_area_id,add_regional_area;
	private RelativeLayout relative_AddsalerArea,relative_AddRegionalArea,choice_shop_rllayout;
	private LinearLayout exitis_linearLayout;
	private CustomerContactsPresenter presenter;
	private int nPage = 1;
	private int nSpage = 10;
	String phone = "";
	private List<Contacts> contactslist = new ArrayList<Contacts>();

    // 定位相关
    private LocationClient mLocClient;
    private MyLocationData locData;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private boolean isFirstLoc = true; // 是否首次定位
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题
		setContentView(R.layout.activity_addcustomer);
		context = this;
		SharedPreferences sharedPreferences=this.getSharedPreferences(User.SHARED_NAME, MODE_PRIVATE);

		user=AppContext.getInstance().readFromSharedPreferences(sharedPreferences);
		init();
		initview();
		
		mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setAddrType("all");//返回的定位结果包含地址信息
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);//设置发起定位请求的间隔时间为5000ms
        mLocClient.setLocOption(option);
        mLocClient.start();//	调用此方法开始定位
	}

	//初始化控件
	private void init() {
		caremaView = (GridView)findViewById(R.id.caremaView);
		customer_add_back = (ImageView) findViewById(R.id.customer_add_back);
		upload_image = (ImageView) findViewById(R.id.upload_img);
		btn_submit = (TextView) findViewById(R.id.btn_sub);
//		cus_addr = (EditText) findViewById(R.id.cus_addr);
		lng_edit = (EditText) findViewById(R.id.lng_edit);
		lat_edit = (EditText) findViewById(R.id.lat_edit);
		store_name = (EditText) findViewById(R.id.store_name);
		cellphone_et = (EditText) findViewById(R.id.cellphone_et);
		link_man = (EditText) findViewById(R.id.link_man);
		mobile_et = (EditText) findViewById(R.id.mobile_et);
		contact_et = (EditText) findViewById(R.id.contact_et);
		mobile_two_et = (EditText) findViewById(R.id.mobile_two_et);
		type_et = (TextView) findViewById(R.id.type_et);
		detailAddr_et = (EditText) findViewById(R.id.detailAddr_et);
		provice_edit = (EditText) findViewById(R.id.provice_edit);
		city_edit = (EditText) findViewById(R.id.city_edit);
		district_edit = (EditText) findViewById(R.id.district_edit);
//		street_edit = (EditText) findViewById(R.id.street_edit);
		kind_ids_et = (EditText) findViewById(R.id.kind_ids_et);
		relative_AddsalerArea = (RelativeLayout) findViewById(R.id.relative_AddsalerArea);
		relative_AddRegionalArea = (RelativeLayout) findViewById(R.id.relative_AddRegionalArea);
		choice_shop_rllayout = (RelativeLayout) findViewById(R.id.choice_shop_rllayout);
		add_saler_area = (TextView) findViewById(R.id.add_saler_area);
		add_saler_area_id = (TextView) findViewById(R.id.add_saler_area_id);
		add_regional_area = (TextView) findViewById(R.id.add_regional_area);
		exitis_linearLayout = (LinearLayout) findViewById(R.id.exitis_linearLayout);
		
		mMapView = (MapView) findViewById(R.id.addCustomer_bmapView); //获取百度地图控件实例
		//图片默认地址
		defaultPhotoAddress = CommonUtil.getSDPath() + File.separator + "default.jpg";
		//获取屏幕的分辨率
		DisplayMetrics  dm = new DisplayMetrics();  
	    //取得窗口属性  
	    getWindowManager().getDefaultDisplay().getMetrics(dm);  
	    //窗口的宽度  
	    screenWidth = dm.widthPixels;  
		
		//是否可删除照片
		candelete = getIntent().getBooleanExtra("candelete", true);
		
		//获取文件夹名称
		if(getIntent().getStringExtra("folderName") == null){
			photoFolderAddress = CommonUtil.getSDPath() + File.separator + "TestPhotoFolder";
		}else{
			photoFolderAddress = getIntent().getStringExtra("folderName");
		}

		presenter=new CustomerContactsPresenterImpl(this);
	}
	
	//相机拍照回调函数
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			switch (requestCode) {
			//拍照
			case CRAEMA_REQUEST_CODE:
				
				//文件夹目录是否存在
				File folderAddr = new File(photoFolderAddress);
				if(!folderAddr.exists() || !folderAddr.isDirectory()){
					folderAddr.mkdirs();
				}
				//将原图片压缩拷贝到指定目录
				pztargetPath = photoFolderAddress + File.separator + CommonUtil.getUUID32() + ".jpg";
				
				CommonUtil.dealImage(defaultPhotoAddress,pztargetPath);
				//删除原图
				new File(defaultPhotoAddress).delete();
				//保存照片的绝对路径
				if(listPhotoNames == null){
					listPhotoNames = new ArrayList<String>();
				}
				listPhotoNames.add(pztargetPath);
				
				if(cadapter == null){
					cadapter = new CaremaAdapter(context, screenWidth, listPhotoNames, candelete);
					caremaView.setAdapter(cadapter);
				}else{
					cadapter.notifyDataSetChanged();
				}
				break;
			}
		}
		//营销区域回传值
		if(requestCode == 1000 && resultCode == 1001)
        {
        	String saler_area = data.getStringExtra("salerArea");
        	add_saler_area.setText(saler_area);
        	
        	String salerAreaId = data.getStringExtra("salerAreaId");
        	add_saler_area_id.setText(salerAreaId);
        }
		//行政区域回传值
		if (requestCode == 1100 && resultCode == 1101){
			String regional_area = data.getStringExtra("province_city_area");
			add_regional_area.setText(regional_area);
			add_regional_area.setTextColor(Color.BLACK);

			String province = data.getStringExtra("province");
			provice_edit.setText(province);

			String city = data.getStringExtra("city");
			city_edit.setText(city);

			String district = data.getStringExtra("district");
			district_edit.setText(district);
		}
		//店面类型及位置回传值
		if(requestCode == 1200 && resultCode == 1201)
		{
			String value = data.getStringExtra("myValue");
			type_et.setText(value);

			String kind_id = data.getStringExtra("myKindId");
			kind_ids_et.setText(kind_id);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//初始化点击事件
	private void initview() {
		MyOnClickLinstener clickLinstener = new MyOnClickLinstener();
		customer_add_back.setOnClickListener(clickLinstener);
		upload_image.setOnClickListener(clickLinstener);
		btn_submit.setOnClickListener(clickLinstener);
		choice_shop_rllayout.setOnClickListener(clickLinstener);
		relative_AddsalerArea.setOnClickListener(clickLinstener);
		relative_AddRegionalArea.setOnClickListener(clickLinstener);

		//监听输入框光标焦点的获取
		mobile_et.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
				} else {
					phone = mobile_et.getText().toString();
					presenter.loadCustomerContacts(user, nPage, nSpage, phone, "","");
				}
			}
		});
	}

	//自定义点击事件类实现点击接口
	private class MyOnClickLinstener implements OnClickListener{
		@Override
		public void onClick(View v) {
			//返回键处理
			if(v.getId() == R.id.customer_add_back) {
				AddCustomerActivity.this.finish();
			}
			//营销区域值回调
			if(v.getId() == R.id.relative_AddsalerArea){
				Intent intent = new Intent(AddCustomerActivity.this, CustomerSalerAreaActivity.class);
				startActivityForResult(intent, 1000);
			}
			//行政区域值回调
			if (v.getId() == R.id.relative_AddRegionalArea){
				Intent intent = new Intent(AddCustomerActivity.this, ProvinceCityAreaActivity.class);
				startActivityForResult(intent, 1100);
			}
			//上传图片调用
			if(v.getId() == R.id.upload_img) {
				//申请6.0权限
				if (Build.VERSION.SDK_INT >= 23) {
					int checkSMSPermission;
					try {
						checkSMSPermission = ContextCompat.checkSelfPermission(AddCustomerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
						if (checkSMSPermission != PackageManager.PERMISSION_GRANTED) {
							ActivityCompat.requestPermissions(AddCustomerActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
							return;
						} else {
							takePhoto();
						}
					} catch (RuntimeException e) {
						showTipsDialog();
						return;
					}
				} else {
					takePhoto();
				}
			}
			//店面类型及位置回调
			if(v.getId() == R.id.choice_shop_rllayout) {
				Intent intent = new Intent(AddCustomerActivity.this,FlowLayoutActivity.class);
				startActivityForResult(intent, 1200);
			}
			//提交
			if(v.getId() == R.id.btn_sub){
				uploadFile();
			}
		}
	}

	//通过号码查询客户是否存在
	@Override
	public void doCustomerContactsLoadSuccess(List<Contacts> contacts) {
		if(contacts.size() > 0){
			contactslist.clear();
			contactslist.addAll(contacts);
			String query_mobile = "";
			for (Contacts c: contacts) {
				query_mobile = c.getMobile1();
			}
			if (query_mobile.equals(phone)){
				exitis_linearLayout.setVisibility(View.VISIBLE);
			}else {
				exitis_linearLayout.setVisibility(View.GONE);
			}
		}else{
			exitis_linearLayout.setVisibility(View.GONE);
		}
	}

	//拍照调用
	private void takePhoto(){
		//验证sd卡是否可用
		if(CommonUtil.getSDPath() == null){
			Toast.makeText(context, "请安装SD卡", Toast.LENGTH_SHORT).show();
			return;
		}
		//验证是否超出限制照片数量
		if(listPhotoNames != null && listPhotoNames.size() >= Constants_Camera.MAX_PHOTO_SIZE){
			showToast("最多只允许拍摄" + Constants_Camera.MAX_PHOTO_SIZE + "张照片");
			return;
		}
		//调用系统相机拍照
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(defaultPhotoAddress)));
		startActivityForResult(intent, CRAEMA_REQUEST_CODE);
	}

	//处理权限结果
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_CAMERA:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// Permission Granted
					takePhoto();
				} else {
					// Permission Denied
					showToast("用户取消了权限");
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
	
	//提交数据、图片到server端的方法
	private boolean uploadFile(){
		String cus_store = store_name.getText().toString().trim();
		String customer_cell_phone = cellphone_et.getText().toString().trim();
		String kind_ids = kind_ids_et.getText().toString().trim();
		String detail_address = detailAddr_et.getText().toString().trim();
		String saler_area_id = add_saler_area_id.getText().toString().trim();
		String regional_area = add_regional_area.getText().toString().trim();
		String provice = provice_edit.getText().toString().trim();
		String city = city_edit.getText().toString().trim();
		String district = district_edit.getText().toString().trim();
		String lng = lng_edit.getText().toString().trim();
		String lat = lat_edit.getText().toString().trim();
		String mobile_one = mobile_et.getText().toString().trim();
		String linkman = link_man.getText().toString().trim();
		String contact = contact_et.getText().toString().trim();
		String mobile_two = mobile_two_et.getText().toString().trim();
		System.out.print("正在发送请求！");
		//提交数据到后台的接口
		String actionUrl = Model.CUSTOMER_ADD_URL+"&token="+ user.getLogin_token();
		try {
		    MultipartEntity mulentity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,null,Charset.forName("UTF-8"));
		    int index = (listPhotoNames==null)? 0:listPhotoNames.size();
//		    if(index > 0){
		    	for(int i = 0;i <index; i++){
		            File file = new File(listPhotoNames.get(i));   
				    FileBody filebody = new FileBody(file);
				    mulentity.addPart("file[]",filebody); 
		        }
		    /*} else{
		    	showToast("请至少拍摄一张图片");
		    	return false;
		    }*/

		    if(!"".equals(cus_store)){
		    	mulentity.addPart("shop_name", new StringBody(cus_store,Charset.forName("UTF-8")));
			}else{
				showToast("店名不能为空");
				return false;
			}

			if (!"".equals(regional_area)){
				mulentity.addPart("provice", new StringBody(provice,Charset.forName("UTF-8")));
				mulentity.addPart("city", new StringBody(city,Charset.forName("UTF-8")));
				mulentity.addPart("district", new StringBody(district,Charset.forName("UTF-8")));
			}else {
				showToast("行政区域不能为空！");
				return false;
			}

			if (!"".equals(detail_address)){
				mulentity.addPart("detail_address", new StringBody(detail_address,Charset.forName("UTF-8")));
			}else {
				showToast("详细地址必填！");
				return false;
			}
		    
		    if(!"".equals(saler_area_id)){
		    	mulentity.addPart("sale_area_id", new StringBody(saler_area_id,Charset.forName("UTF-8")));
		    }else{
		    	showToast("营销区域不能为空！");
		    	return false;
		    }

			/*if("".equals(customer_cell_phone)){
				mulentity.addPart("telephone", new StringBody(customer_cell_phone));
			}else if (isTelephoneNumberValid(customer_cell_phone)){
				mulentity.addPart("telephone", new StringBody(customer_cell_phone));
			}else{
				showToast("电话号码格式不正确或者你也可以选择不填！");
				return false;
			}*/
		    
		    if(!"".equals(kind_ids)){
		    	mulentity.addPart("shop_type", new StringBody(kind_ids,Charset.forName("UTF-8")));
		    }else{
		    	showToast("店面类型不能为空！");
		    	return false;
		    }

		    if(!"".equals(linkman)){
		    	mulentity.addPart("contacts1", new StringBody(linkman,Charset.forName("UTF-8")));
			}else{
				showToast("首要联系人姓名不能为空！");
				return false;
			}

			if(!"".equals(mobile_one) && isPhoneNumberValid(mobile_one)){
				mulentity.addPart("mobile1", new StringBody(mobile_one));
			}else{
				showToast("手机号码1不能为空或号码格式不正确!");
				return false;
			}

		    if("".equals(mobile_two)){
		    	mulentity.addPart("mobile2", new StringBody(mobile_two));
			}else if(mobile_two.length() == 11 && !"".equals(mobile_two)){
				mulentity.addPart("mobile2", new StringBody(mobile_two));
			}else{
				showToast("手机号码2不能少于或超出11位，你也可以选择不填！");
				return false;
			}
			mulentity.addPart("telephone", new StringBody(customer_cell_phone));
		    mulentity.addPart("method", new StringBody("addCustomersInfo",Charset.forName("UTF-8")));
		    mulentity.addPart("Longitude", new StringBody(lng));
		    mulentity.addPart("Latitude", new StringBody(lat));
		    mulentity.addPart("contacts2", new StringBody(contact,Charset.forName("UTF-8")));
		    mulentity.addPart("sale_area_id", new StringBody(saler_area_id,Charset.forName("UTF-8")));
		    mulentity.addPart("employee_id", new StringBody(user.getEmployee_id()));
		    mulentity.addPart("organization_id", new StringBody(user.getOrg_id()));
		    mulentity.addPart("user_id",new StringBody(user.getUser_id()));

			ThreadPoolUtils.execute(new HttpPostThread(handler,actionUrl,"utf-8",mulentity));
		}catch (Exception e) {
		}
		return true;
	}

	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what == 404){
				showToast("服务器地址错误");
			}
			if(msg.what == 100){
				showToast("网络传输失败");
			}
			if(msg.what == 200){
				String result = (String) msg.obj;
				if(result != null){
					JSONObject response = null;
					try {
						response = new JSONObject(result);
						int status = response.getInt("result");
						if(response != null){
							if(status == 0){
								showToast(response.getString("msg"));
								Intent intent = new Intent(AddCustomerActivity.this, MainActivity.class);
								startActivity(intent);
								finish();
								//清空表单内容
								store_name.setText("");
								detailAddr_et.setText("");
								add_saler_area.setText("");
								kind_ids_et.setText("");
								link_man.setText("");
								mobile_et.setText("");
								contact_et.setText("");
								mobile_two_et.setText("");
							}else{
								showToast("服务器异常，请稍后再试！");
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else{
					showToast("服务器连接失败！");
				}
			}
		};
	};

	 /*
     * 验证号码 手机号 固话
     * 
     */
    public boolean isPhoneNumberValid(String phoneNumber) {
    	String expression = "((^(13|15|18|17|14)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
		CharSequence inputStr = phoneNumber;
     
        Pattern pattern = Pattern.compile(expression);
     
        Matcher matcher = pattern.matcher(inputStr);
     
        if (matcher.matches()) {
        	return true;
        }else{
//        	showToast("手机或电话号码不正确!格式如：13855558888或01088885555");
			return false;
        }
//		return false;
    }

	public boolean isTelephoneNumberValid(String cellPhoneNumber) {
		String expression = "(^(\\d{3,4}-)?\\d{8})$";
		CharSequence inputStr = cellPhoneNumber;

		Pattern pattern = Pattern.compile(expression);

		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.matches()) {
			return true;
		}else{
			return false;
		}
	}
	
    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            
            lng_edit.setText(String.valueOf(locData.longitude));
            lat_edit.setText(String.valueOf(locData.latitude));

            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
            
             /*cus_addr.setText(location.getAddrStr());
	   		 String provice = location.getProvince();
	   		 provice_edit.setText(provice);
	   		 String city = location.getCity();
	   		 city_edit.setText(city);
	   		 String district = location.getDistrict();
	   		 district_edit.setText(district);
	   		 String street = location.getStreet();
	   		 street_edit.setText(street);*/
        }

        public void onReceivePoi(BDLocation poiLocation) {
        	if (poiLocation == null){
                return ;
            }
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
    	if (mLocClient != null)
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
	
	/**
	 * 
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AddCustomerActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
