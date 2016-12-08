package com.bangware.shengyibao.customer.view;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.bangware.shengyibao.customer.CustomerUtils;
import com.bangware.shengyibao.customer.HttpUtils;
import com.bangware.shengyibao.customer.adapter.CaremaAdapter;
import com.bangware.shengyibao.customer.adapter.CustomerImageShowAdapter;
import com.bangware.shengyibao.customer.model.entity.Customer;
import com.bangware.shengyibao.customer.model.entity.CustomerShopType;
import com.bangware.shengyibao.manager.shoptypeflowlayout.view.FlowLayoutActivity;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.CommonUtil;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 客户编辑页面
 * @author ccssll
 *
 */
public class CustomerEditActivity extends BaseActivity {
	//图片上传常量定义
	private Context context;
	private GridView carema_edit_gridView,take_photo_gridView; 					  //照片显示区域
	private static final int CRAEMA_REQUEST_CODE = 0; //拍照请求码
	private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
	private boolean candelete = false; //是否可以删除照片
	private String defaultPhotoAddress = null; //拍照生成默认照片的绝对路径
	private String photoFolderAddress = null; //存放照片的文件夹
	private String pztargetPath = null;
	private List<String> listPhotoNames = null;
	private CaremaAdapter cadapter = null;//拍照完显示的adapter
	private int screenWidth = 0; //屏幕宽度
	
	private CustomerImageShowAdapter showImageAdapter;//从后台获取图片的adapter
	//提交数据到后台的接口
	private String edit_actionUrl = Model.CUSTOMER_EDIT_URL+"&token="+ AppContext.getInstance().getUser().getLogin_token();
	
	private ImageView back_img,add_imageview;
	private EditText store_name,code,telephone_et,customer_address,kind_ids_et,link_man,mobile_et,mobile_second_et;
	private EditText provice_editText,city_editText,district_editText,longitude_edit,latitude_edit,saler_area_id;
	private TextView administrative_area,type_et,area;
	private TextView btn_submit;
	private RelativeLayout relative_saler_area,takePictures_rlLayout,relative_EditRegionalArea,editChoice_shop_rllayout;

	private Customer customer;
	//地理定位
	private LocationClient mLocClient;
    private MyLocationData locData;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private boolean isFirstLoc = true; // 是否首次定位
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题
		
		setContentView(R.layout.activity_edit);
		context = this;
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
	
	public void init(){
		back_img = (ImageView) findViewById(R.id.customer_edit_back);
		add_imageview =  (ImageView) findViewById(R.id.add_imageview);
		carema_edit_gridView = (GridView)findViewById(R.id.carema_edit_gridView);
		take_photo_gridView = (GridView)findViewById(R.id.take_photo_gridView);
		store_name = (EditText) findViewById(R.id.store_name);
		code = (EditText) findViewById(R.id.code);
		telephone_et = (EditText) findViewById(R.id.telephone_et);
		area = (TextView) findViewById(R.id.area);//营销区域
		saler_area_id = (EditText) findViewById(R.id.saler_area_id);
		customer_address = (EditText) findViewById(R.id.cus_address);
		type_et = (TextView) findViewById(R.id.type_et);
		kind_ids_et = (EditText) findViewById(R.id.kind_ids_et);
		link_man = (EditText) findViewById(R.id.link_man);
		mobile_et = (EditText) findViewById(R.id.mobile_et);
		mobile_second_et = (EditText) findViewById(R.id.mobile_second_et);
		administrative_area = (TextView) findViewById(R.id.administrative_area);//行政区域
		provice_editText = (EditText) findViewById(R.id.provice_editText);
		city_editText = (EditText) findViewById(R.id.city_editText);
		district_editText = (EditText) findViewById(R.id.district_editText);
		longitude_edit = (EditText) findViewById(R.id.longitude_edit);
		latitude_edit = (EditText) findViewById(R.id.latitude_edit);
		relative_saler_area = (RelativeLayout) findViewById(R.id.relative_saler_area);
		relative_EditRegionalArea = (RelativeLayout) findViewById(R.id.relative_EditRegionalArea);
		editChoice_shop_rllayout = (RelativeLayout) findViewById(R.id.editChoice_shop_rllayout);
		takePictures_rlLayout = (RelativeLayout) findViewById(R.id.takePictures_rlLayout);
		btn_submit = (TextView) findViewById(R.id.btn_submit);
		
		mMapView = (MapView) findViewById(R.id.bmap_edit_view); //获取百度地图控件实例
		
		//接收bundle传递过来的值
		customer = (Customer)getIntent().getExtras().getSerializable("customer");
		store_name.setText(customer.getName());
		code.setText(customer.getCode());
		telephone_et.setText(customer.getTelephone());
		customer_address.setText(customer.getAddress());
		area.setText(customer.getDistrict());
		saler_area_id.setText(customer.getSalerareaId());
		
		String typeStr = "";
		for (int i = 0; i < customer.getKinds().size(); i++) {
			typeStr += customer.getKinds().get(i).getName()+" ";
		}
		type_et.setText(typeStr);

		if (customer.getContacts().size() > 0) {
			link_man.setText(customer.getContacts().get(0).getName());
			mobile_et.setText(customer.getContacts().get(0).getMobile1());
			mobile_second_et.setText(customer.getContacts().get(0).getMobile2());
		}
		administrative_area.setText(customer.getCity());
		
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
		
		//初始化加载客户图片
		if(customer.getImages().size() > 0){
			showImageAdapter = new CustomerImageShowAdapter(this, customer.getImages(),candelete);
			carema_edit_gridView.setAdapter(showImageAdapter);
			carema_edit_gridView.setVisibility(View.VISIBLE);
		}else{
			carema_edit_gridView.setVisibility(View.GONE);
		}
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
					take_photo_gridView.setAdapter(cadapter);
					takePictures_rlLayout.setVisibility(View.VISIBLE);//照片显示区域
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
        	area.setText(saler_area);
        	
        	String salerAreaId = data.getStringExtra("salerAreaId");
        	saler_area_id.setText(salerAreaId);
        }
		//行政区域回传值
		if (requestCode == 1100 && resultCode == 1101){
			String regional_area = data.getStringExtra("province_city_area");
			administrative_area.setText(regional_area);
			administrative_area.setTextColor(Color.BLACK);

			String province = data.getStringExtra("province");
			provice_editText.setText(province);

			String city = data.getStringExtra("city");
			city_editText.setText(city);

			String district = data.getStringExtra("district");
			district_editText.setText(district);
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
	
	public void initview() {
		// TODO Auto-generated method stub
		MyOnClickLinstener clickLinstener = new MyOnClickLinstener();
		back_img.setOnClickListener(clickLinstener);
		btn_submit.setOnClickListener(clickLinstener);
		editChoice_shop_rllayout.setOnClickListener(clickLinstener);
		add_imageview.setOnClickListener(clickLinstener);
		relative_saler_area.setOnClickListener(clickLinstener);
		relative_EditRegionalArea.setOnClickListener(clickLinstener);
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
            
            longitude_edit.setText(String.valueOf(locData.longitude));
            latitude_edit.setText(String.valueOf(locData.latitude));
            
            /*provice_editText.setText(location.getProvince());
            city_editText.setText(location.getCity());
            district_editText.setText(location.getDistrict());*/
            
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
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
	
	private class MyOnClickLinstener implements OnClickListener{

		@Override
		public void onClick(View v) {
			//返回键
			if(v.getId() == R.id.customer_edit_back){
				CustomerEditActivity.this.finish();
			}
			//营销区域选择
			if(v.getId() == R.id.relative_saler_area){
				Intent intent = new Intent(CustomerEditActivity.this, CustomerSalerAreaActivity.class);
				intent.putExtra("salerAreaId", saler_area_id.getText().toString());
				intent.putExtra("salerArea", area.getText().toString());
				startActivityForResult(intent, 1000);
			}
			//行政区域选择
			if (v.getId() == R.id.relative_EditRegionalArea){
				Intent intent = new Intent(CustomerEditActivity.this, ProvinceCityAreaActivity.class);
				intent.putExtra("province_city_area",administrative_area.getText().toString());
				intent.putExtra("province",provice_editText.getText().toString());
				intent.putExtra("city",city_editText.getText().toString());
				intent.putExtra("district",district_editText.getText().toString());
				startActivityForResult(intent, 1100);
			}
			//店面类型
			if(v.getId() == R.id.editChoice_shop_rllayout){
				Intent intent = new Intent(CustomerEditActivity.this,FlowLayoutActivity.class);
				intent.putExtra("myValue", type_et.getText().toString());
				intent.putExtra("myKindId",kind_ids_et.getText().toString());
				startActivityForResult(intent, 1200);
			}
			//重新拍摄
			if (v.getId() == R.id.add_imageview) {
				//申请6.0权限
				if (Build.VERSION.SDK_INT >= 23) {
					int checkSMSPermission = ContextCompat.checkSelfPermission(CustomerEditActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
					if (checkSMSPermission != PackageManager.PERMISSION_GRANTED) {
						ActivityCompat.requestPermissions(CustomerEditActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
						return;
					} else {
						getTakePhoto();
					}
				} else {
					getTakePhoto();
				}
			}
			//修改客户资料事件
			if(v.getId() == R.id.btn_submit){
				submit_uploadFile();
			}
		}
	}

	//拍照调用
	private void getTakePhoto(){
		//验证sd卡是否可用
		if(CommonUtil.getSDPath() == null){
			showToast("请安装SD卡");
			return;
		}
		//验证是否超出限制照片数量
		if(listPhotoNames != null && listPhotoNames.size() >= Constants_Camera.MAX_PHOTO_SIZE || customer.getImages().size() >= Constants_Camera.MAX_PHOTO_SIZE){
			showToast("最多只允许拍摄" + Constants_Camera.MAX_PHOTO_SIZE + "张照片。");
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
					getTakePhoto();
				} else {
					// Permission Denied
					showToast("用户取消了权限");
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
	
	//封装数据到后台
	private boolean submit_uploadFile(){
		String shop_name = store_name.getText().toString().trim();
		String telephone_number = telephone_et.getText().toString().trim();
		String saler_areaId = saler_area_id.getText().toString().trim();
//		String saler_area = area.getText().toString().trim();
		String address = customer_address.getText().toString().trim();
		String kind_ids = kind_ids_et.getText().toString().trim();
		String contact = link_man.getText().toString().trim();
		String mobile = mobile_et.getText().toString().trim();
		String mobile_second = mobile_second_et.getText().toString().trim();
		String longitude = longitude_edit.getText().toString().trim();
		String latitude = latitude_edit.getText().toString().trim();
		String province = provice_editText.getText().toString();
		String city = city_editText.getText().toString();
		String district = district_editText.getText().toString();
		Log.d("TAG", "正在发送请求......");
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(edit_actionUrl);
			MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
		
			int index = (listPhotoNames==null)? 0:listPhotoNames.size();
			for (int i = 0; i < index; i++) {
				File file = new File(listPhotoNames.get(i));
				FileBody filebody = new FileBody(file);
				multipartEntity.addPart("file[]", filebody);
			}
			
			if(!"".equals(shop_name)){
				multipartEntity.addPart("shop_name", new StringBody(shop_name,Charset.forName("UTF-8")));
			}else{
				showToast("店名不能为空");
				return false;
			}

			/*if("".equals(telephone_number)){
				multipartEntity.addPart("telephone", new StringBody(telephone_number));
			}else if (isTelephoneNumberValid(telephone_number)){
				multipartEntity.addPart("telephone", new StringBody(telephone_number));
			}else{
				showToast("电话号码格式不正确或者你也可以选择不填！");
				return false;
			}*/
			if(!"".equals(saler_areaId)){
				multipartEntity.addPart("sale_area_id",new StringBody(saler_areaId,Charset.forName("UTF-8")));
			}else{
				showToast("营销区域不能为空");
				return false;
			}
			
			if(!"".equals(contact)){
				multipartEntity.addPart("contact_name", new StringBody(contact,Charset.forName("UTF-8")));
			}else{
				showToast("联系人姓名不能为空！");
				return false;
			}
			
			if(!"".equals(mobile) && isPhoneNumberValid(mobile)){
				multipartEntity.addPart("mobile1", new StringBody(mobile));
			}else{
				showToast("手机号码1不能为空或号码格式不正确!");
				return false;
			}
			
			if("".equals(mobile_second)){
				multipartEntity.addPart("mobile2", new StringBody(mobile_second));
			}else if(mobile_second.length() == 11 && !"".equals(mobile_second)){
				multipartEntity.addPart("mobile2", new StringBody(mobile_second));
			}else{
				showToast("手机号码2不能少于或超出11位，你也可以选择不填！");
				return false;
			}

			multipartEntity.addPart("method", new StringBody("edit_customer_info",Charset.forName("UTF-8")));
			multipartEntity.addPart("customer_id", new StringBody(customer.getId()));
			multipartEntity.addPart("telephone", new StringBody(telephone_number));
			multipartEntity.addPart("provice", new StringBody(province,Charset.forName("UTF-8")));
			multipartEntity.addPart("Longitude", new StringBody(longitude));
			multipartEntity.addPart("Latitude", new StringBody(latitude));
			multipartEntity.addPart("city", new StringBody(city,Charset.forName("UTF-8")));
			multipartEntity.addPart("district", new StringBody(district,Charset.forName("UTF-8")));
			multipartEntity.addPart("detail_address", new StringBody(address,Charset.forName("UTF-8")));
			multipartEntity.addPart("shop_type",new StringBody(kind_ids));
			if(customer.getContacts().size() > 0){
				multipartEntity.addPart("contact_id", new StringBody(customer.getContacts().get(0).getId()));
			}else{
				multipartEntity.addPart("customer_id", new StringBody(customer.getId()));
			}
			httpPost.setEntity(multipartEntity);
			
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				String strResult = EntityUtils.toString(httpResponse.getEntity());

				JSONObject objresult = new JSONObject(strResult);
				if (objresult != null){
					switch (objresult.getInt("result")){
						case 0:
							showToast(objresult.getString("msg"));
							Intent intent = new Intent(CustomerEditActivity.this, CustomerInfoActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							Bundle bundle = new Bundle();
							bundle.putSerializable("customer", customer);
							intent.putExtras(bundle);
							startActivity(intent);

							store_name.setText("");
							customer_address.setText("");
							link_man.setText("");
							mobile_et.setText("");
							mobile_second_et.setText("");
							break;
						case 1:
							showToast(objresult.getString("msg"));
							break;
						case 2:
							showToast(objresult.getString("msg"));
							break;
					}
				}else {
					showToast("返回内容为空！");
				}
			}else{
				showToast("请求失败！");
				return false;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			showToast("请求出错！服务器异常......");
			e.printStackTrace();
		}
		return true;
	}
	
	 
     // 验证号码 手机号 固话
    public boolean isPhoneNumberValid(String phoneNumber) {
    	String expression = "((^(13|15|18|17|14)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
        CharSequence inputStr = phoneNumber;
     
        Pattern pattern = Pattern.compile(expression);
     
        Matcher matcher = pattern.matcher(inputStr);
     
        if (matcher.matches()) {
        	return true;
        }else{
        	showToast("手机号码格式不正确!格式如：13855558888");
        }
		return false;
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
}