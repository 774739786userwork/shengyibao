package com.bangware.shengyibao.customer.view;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.customer.adapter.CustomerSalerAreaAdapter;
import com.bangware.shengyibao.customer.model.entity.Customer;
import com.bangware.shengyibao.customer.model.entity.CustomerSalerArea;
import com.bangware.shengyibao.customer.presenter.CustomerSalerAreaPresenter;
import com.bangware.shengyibao.customer.presenter.impl.CustomerSalerAreaPresenterImpl;
import com.bangware.shengyibao.main.view.MainActivity;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.CommonUtil;
import com.bangware.shengyibao.utils.CustomerSpinner;
import com.bangware.shengyibao.utils.volley.ImageRequester;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

/**
 * 客户编辑页面
 * @author ccssll
 *
 */
public class TestPhotoActivity extends BaseActivity implements CustomerSalerAreaView{
	private GridView gridView1;                   //网格显示缩略图
	private Bitmap bmp;                               //导入临时图片
	private ArrayList<HashMap<String, Object>> imageItem;
	private SimpleAdapter simpleAdapter;     //适配器
	private static final int CRAEMA_REQUEST_CODE = 0; //拍照请求码
	private String defaultPhotoAddress = null; //拍照生成默认照片的绝对路径
	private String photoFolderAddress = null; //存放照片的文件夹
	private String pztargetPath = null;
	
	private String customer_image_url = null;
	//提交数据到后台的接口
	private String edit_actionUrl = Model.CUSTOMER_EDIT_URL+"&token="+ AppContext.getInstance().getUser().getLogin_token();
	
	private ImageView back_img,edit_img;
	private EditText store_name,code,customer_address,kind_ids_et,link_man,mobile_et,mobile_second_et;
	private EditText provice_editText,city_editText,district_editText,longitude_edit,latitude_edit;
	private TextView administrative_area,type_et,area,saler_area_id;
	private Button btn_submit;
	private CustomerSpinner spinner;
	//对复选框操作
	private final static int DIALOG=1;
	private boolean[] flags;//初始复选情况
	
	private CustomerSalerAreaPresenter salerAreaPresenter;
	private List<CustomerSalerArea> salerArealList = new ArrayList<CustomerSalerArea>();
	private CustomerSalerAreaAdapter spinnerAdapter;
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
		
		setContentView(R.layout.activity_test_photo);
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
		edit_img =  (ImageView) findViewById(R.id.edit_img);
		store_name = (EditText) findViewById(R.id.store_name);
		code = (EditText) findViewById(R.id.code);
		area = (TextView) findViewById(R.id.area);//营销区域
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
		spinner = (CustomerSpinner) findViewById(R.id.spinner);
		saler_area_id = (TextView) findViewById(R.id.saler_area_id);
		
		btn_submit = (Button) findViewById(R.id.btn_submit);
		
		mMapView = (MapView) findViewById(R.id.bmap_edit_view); //获取百度地图控件实例
		
		//营销区域数据请求
		salerAreaPresenter = new CustomerSalerAreaPresenterImpl(this);
		salerAreaPresenter.loadSalerAreaData();
		//把请求数据设置到下拉框当中
		spinner.setList(salerArealList);
		spinnerAdapter = new CustomerSalerAreaAdapter(this, salerArealList);
		spinner.setAdapter(spinnerAdapter);
		
		//接收bundle传递过来的值
		customer = (Customer)getIntent().getExtras().getSerializable("customer");
		store_name.setText(customer.getName());
		code.setText(customer.getCode());
		customer_address.setText(customer.getAddress());
//		customer_address.setText((CharSequence) getIntent().getExtras().getSerializable("marker_address"));
		area.setText(customer.getDistrict());
		saler_area_id.setText(customer.getSalerareaId());
		
		String typeStr = "";
		for (int i = 0; i < customer.getKinds().size(); i++) {
			typeStr += customer.getKinds().get(i).getName()+" ";
		}
		type_et.setText(typeStr);
		
		link_man.setText(customer.getContacts().get(0).getName());
		mobile_et.setText(customer.getContacts().get(0).getMobile1());
		mobile_second_et.setText(customer.getContacts().get(0).getMobile2());
		
		administrative_area.setText(customer.getCity());
		//加载客户图片
		ImageRequester imageRequester = new ImageRequester(getApplicationContext(), true);
		if(customer.getImages().size() > 0){
			customer_image_url = customer.getImages().get(0).getImg_url();
		}
		if(customer_image_url!=null && !"".equals(customer_image_url)){
	    	imageRequester.load(edit_img, Model.HTTPURL+ customer_image_url, R.drawable.no_pic_300, R.drawable.no_pic_300);
	    }
	    
	    
	    //图片默认地址
  		defaultPhotoAddress = CommonUtil.getSDPath() + File.separator + "default.jpg";
  		//获取屏幕的分辨率
  		DisplayMetrics  dm = new DisplayMetrics();  
  	    //取得窗口属性  
  	    getWindowManager().getDefaultDisplay().getMetrics(dm);  
  		
  		//获取文件夹名称
  		if(getIntent().getStringExtra("folderName") == null){
  			photoFolderAddress = CommonUtil.getSDPath() + File.separator + "TestPhotoFolder";
  		}else{
  			photoFolderAddress = getIntent().getStringExtra("folderName");
  		}
	    
	    gridView1 = (GridView) findViewById(R.id.carema_edit_gridView);

        /*
         * 载入默认图片添加图片加号
         * 通过适配器实现
         */
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.gridview_addpic); //加号
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this, 
        		imageItem, R.layout.griditem_addpic, 
                new String[] { "itemImage"}, new int[] { R.id.imageView1}); 
        /*
         * HashMap载入bmp图片在GridView中不显示,但是如果载入资源ID能显示 如
         * map.put("itemImage", R.drawable.img);
         * 解决方法:
         *              1.自定义继承BaseAdapter实现
         *              2.ViewBinder()接口实现
         */
        simpleAdapter.setViewBinder(new ViewBinder() {  
		    @Override  
		    public boolean setViewValue(View view, Object data,  
		            String textRepresentation) {  
		        // TODO Auto-generated method stub  
		        if(view instanceof ImageView && data instanceof Bitmap){  
		            ImageView i = (ImageView)view;  
		            i.setImageBitmap((Bitmap) data);  
		            return true;  
		        }  
		        return false;  
		    }
        });  
        gridView1.setAdapter(simpleAdapter);
        
        /*
         * 监听GridView点击事件
         * 报错:该函数必须抽象方法 故需要手动导入import android.view.View;
         */
        gridView1.setOnItemClickListener(new OnItemClickListener() {
  			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
  				if( imageItem.size() == 4) { //第一张为默认图片
  					showToast("图片不能超过三张");
  				}
  				else if(position == 0) { 
  					//验证sd卡是否可用
					if(CommonUtil.getSDPath() == null){
						showToast("请安装SD卡");
						return;
					}
  					//调用系统相机拍照
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(new File(defaultPhotoAddress)));
					startActivityForResult(intent, CRAEMA_REQUEST_CODE);
  				}
  				else {
  					dialog(position);
  				}
				
			}
  		});
	    
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
				
				Bitmap addbmp=BitmapFactory.decodeFile(pztargetPath);
				HashMap<String, Object> map = new HashMap<String, Object>();
		        map.put("itemImage", addbmp);
		        imageItem.add(map);
				
				simpleAdapter = new SimpleAdapter(this, 
						imageItem, R.layout.griditem_addpic, 
		                new String[] { "itemImage"}, new int[] { R.id.imageView1}); 
		        simpleAdapter.setViewBinder(new ViewBinder() {  
				    @Override  
				    public boolean setViewValue(View view, Object data,  
				            String textRepresentation) {  
				        // TODO Auto-generated method stub  
				        if(view instanceof ImageView && data instanceof Bitmap){  
				            ImageView i = (ImageView)view;  
				            i.setImageBitmap((Bitmap) data);  
				            return true;  
				        }  
				        return false;  
				    }
		        }); 
		        gridView1.setAdapter(simpleAdapter);
		        simpleAdapter.notifyDataSetChanged();
		      //刷新后释放防止手机休眠后自动添加
		        pztargetPath = null;
				break;
			}
		}
	}
	
    /*
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */
    protected void dialog(final int position) {
    	AlertDialog.Builder builder = new Builder(TestPhotoActivity.this);
    	builder.setMessage("确认删除已添加图片吗？");
    	builder.setTitle("提示");
    	builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			dialog.dismiss();
    			imageItem.remove(position);
    	        simpleAdapter.notifyDataSetChanged();
    		}
    	});
    	builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    				dialog.dismiss();
    			}
    		});
    	builder.create().show();
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
		type_et.setOnClickListener(clickLinstener);
		edit_img.setOnClickListener(clickLinstener);
		
		spinner.setSelection(0, true);//初始化时不改变spinner的值
		MyOnItemSelectedListener selectListener = new MyOnItemSelectedListener();
		spinner.setOnItemSelectedListener(selectListener);
	}
	
	//下拉选择事件
	private class MyOnItemSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView <?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			if(position == 0){
				return;
			}else{
				String area_name = salerArealList.get(position).getName();
				area.setText(area_name);
				
				String area_id = salerArealList.get(position).getId();
				saler_area_id.setText(area_id);
				
				spinner.setVisibility(View.GONE);
			}
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
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
            
            longitude_edit.setText(String.valueOf(locData.longitude));
            latitude_edit.setText(String.valueOf(locData.latitude));
            
            provice_editText.setText(location.getProvince());
            city_editText.setText(location.getCity());
            district_editText.setText(location.getDistrict());
            
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
				TestPhotoActivity.this.finish();
			}
			//店面类型
			if(v.getId() == R.id.type_et){
//				showDialog(DIALOG);
			}
			//删除图片
			if(v.getId() == R.id.edit_img){
				
			}
			
			//修改客户资料事件
			if(v.getId() == R.id.btn_submit){
				submit_uploadFile();
			}
		}
	}
	
	//封装数据到后台
	private boolean submit_uploadFile(){
		String shop_name = store_name.getText().toString().trim();
		String saler_area = saler_area_id.getText().toString().trim();
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
//		Log.d("TAG", "-------------------------------->"+province+","+city+","+district+","+longitude+","+latitude);
		Log.d("TAG", "正在发送请求......");
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(edit_actionUrl);
			MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
		
//			int index = (imageItem==null)? 0:imageItem.size();
			
			Log.d("TAG", "======================>:::::-------"+imageItem.size());
			for (HashMap<String, Object> i : imageItem) {
				File file = new File(i.toString());
				FileBody filebody = new FileBody(file);
				multipartEntity.addPart("file[]", filebody);
			}
			/*for (int i = 0; i < index; i++) {
				
			}*/
			
			if(!"".equals(shop_name)){
				multipartEntity.addPart("shop_name", new StringBody(shop_name,Charset.forName("UTF-8")));
			}else{
				showToast("店名不能为空");
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
			multipartEntity.addPart("sale_area_id",new StringBody(saler_area));
			multipartEntity.addPart("provice", new StringBody(province,Charset.forName("UTF-8")));
			multipartEntity.addPart("Longitude", new StringBody(longitude));
			multipartEntity.addPart("Latitude", new StringBody(latitude));
			multipartEntity.addPart("city", new StringBody(city,Charset.forName("UTF-8")));
			multipartEntity.addPart("district", new StringBody(district,Charset.forName("UTF-8")));
			multipartEntity.addPart("detail_address", new StringBody(address,Charset.forName("UTF-8")));
			multipartEntity.addPart("shop_type",new StringBody(kind_ids));
			multipartEntity.addPart("contact_id", new StringBody(customer.getContacts().get(0).getId()));
			httpPost.setEntity(multipartEntity);
			
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				String strResult = EntityUtils.toString(httpResponse.getEntity());
				Log.d("TAG", "=================>"+strResult);
				Intent intent = new Intent(TestPhotoActivity.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				
				store_name.setText("");
				customer_address.setText("");
				link_man.setText("");
				mobile_et.setText("");
				mobile_second_et.setText("");
				showToast("请求成功！");
			}else{
				showToast("请求失败！");
				return false;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			showToast("请求出错！");
			e.printStackTrace();
		}
		return true;
	}
	
	 
     // 验证号码 手机号 固话
    public boolean isPhoneNumberValid(String phoneNumber) {
    	String expression = "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
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

	@Override
	public void loadSalerAreaData(List<CustomerSalerArea> salerAreas) {
		// TODO Auto-generated method stub
		if(salerAreas.size() > 0 ){
			salerArealList.clear();
			salerArealList.addAll(salerAreas);
			spinnerAdapter.notifyDataSetChanged();
		}else{
			showToast("暂无数据");
			spinnerAdapter.notifyDataSetChanged();
		}
	}
}