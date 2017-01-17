package com.bangware.shengyibao.activity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.bangware.shengyibao.model.CityModel;
import com.bangware.shengyibao.model.DistrictModel;
import com.bangware.shengyibao.model.ProvinceModel;
import com.bangware.shengyibao.net.ConnectionChangeReceiver;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.HomeListener;
import com.bangware.shengyibao.utils.volley.DataRequest;
import com.nostra13.universalimageloader.core.ImageLoader;


import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class BaseActivity extends Activity implements BaseView {
	private Toast mToast;
	protected CustomProgressDialog loadingdialog;
	private  ConnectionChangeReceiver myReceiver;
	private HomeListener homeListener = null;//Home键监听广播

	protected void onCreate(Bundle savedInstanceState) {
		if (savedInstanceState!=null)
		{
			DataRequest.buildRequestQueue(this);
		}
		super.onCreate(savedInstanceState);
		registerReceiver();
		loadingdialog = CustomProgressDialog.createDialog(this,R.drawable.frame);
		setDefaultFont();

		/*initHomeListen();//监听home键退出
		homeListener.start();*/
	}

	/**
	 * onSaveInstanceState和onRestoreInstanceState保存状态与恢复
	 * @param outState
     */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.e("HELLO", "HELLO：当Activity被销毁的时候，不是用户主动按back销毁，例如按了home键");
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Log.e("HELLO", "HELLO:如果应用进程被系统咔嚓，则再次打开应用的时候会进入");
	}

	/**广播注册监听网络*/
	private  void registerReceiver(){
		IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		myReceiver=new ConnectionChangeReceiver();
		this.registerReceiver(myReceiver, filter);
	}

	private  void unregisterReceiver(){
		this.unregisterReceiver(myReceiver);
	}


	@Override
	protected void onDestroy() {
		if (myReceiver != null){
			unregisterReceiver();
		}
		super.onDestroy();
	}

	/** 判断触摸时间派发间隔 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			if (AppContext.isFastDoubleClick()) {
				return true;
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 设置程序字体不会随系统改变而改变
	 */
	public void setDefaultFont(){
		Resources res = super.getResources();
		Configuration config=new Configuration();
		config.setToDefaults();
		res.updateConfiguration(config, res.getDisplayMetrics());
	}

	@Override
	protected void onResume( ) {
		super.onResume();
//        homeListener.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
//        homeListener.stop();
	}

	private void initHomeListen(){
		homeListener = new HomeListener(this);
		homeListener.setOnHomeBtnPressListener( new HomeListener.OnHomeBtnPressLitener( ) {
			@Override
			public void onHomeBtnPress() {
				showToast( "按下Home按键！" );
                /*finish();
                System.exit(0);*/
			}

			@Override
			public void onHomeBtnLongPress() {
				showToast( "长按Home按键！" );
			}
		});
	}

	/**
     * 显示Toast消息
     * @param msg
     */
    public void showToast(String msg){
    	showToast(msg, Toast.LENGTH_SHORT);
    }
    public void showToast(String msg, int length){
        if(mToast == null){
            mToast = Toast.makeText(this, msg, length);
        }else{
            mToast.setText(msg);
            mToast.setDuration(length);
        }
        mToast.show();
    }


    /**
     * 显示Toast消息
     * @param
     */
    public void showToast(int stringId){
    	showToast(stringId, Toast.LENGTH_SHORT);
    }
    public void showToast(int stringId, int length){
        if(mToast == null){
            mToast = Toast.makeText(this, stringId, length);
        }else{
            mToast.setText(stringId);
            mToast.setDuration(length);
        }
        mToast.show();
    }

	protected void showTipsDialog() {
		new AlertDialog.Builder(this).setTitle("提示信息").setMessage("当前应用缺少该功能的使用权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						showToast("该功能未授权，请再次授权");
					}
				})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startAppSettings();
					}
				}).show();
	}


	/**
	 * 启动当前应用设置页面
	 */
	protected void startAppSettings() {
		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.parse("package:" + getPackageName()));
		startActivity(intent);
	}

	public void showLoading(){
    	loadingdialog.show();
    }

	@Override
	public void showLoading(String text) {
		loadingdialog.show();
	}

    public void hideLoading(){
    	loadingdialog.dismiss();
    }
    public void showMessage(String message){
    	showToast(message);
    }
    public void dismissLoading(){
    	loadingdialog.dismiss();
    }

    /**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	/**
	 * key - 区 values - 邮编
	 */
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName;

	/**
	 * 当前区的邮政编码
	 */
	protected String mCurrentZipCode;

	/**
	 * 解析省市区的XML数据
	 */

    protected void initProvinceDatas()
	{
		List<ProvinceModel> provinceList = null;
    	AssetManager asset = getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			provinceList = handler.getDataList();
			//*/ 初始化默认选中的省、市、区
			if (provinceList!= null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList!= null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0).getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentZipCode = districtList.get(0).getZipcode();
				}
			}
			//*/
			mProvinceDatas = new String[provinceList.size()];
        	for (int i=0; i< provinceList.size(); i++) {
        		// 遍历所有省的数据
        		mProvinceDatas[i] = provinceList.get(i).getName();
        		List<CityModel> cityList = provinceList.get(i).getCityList();
        		String[] cityNames = new String[cityList.size()];
        		for (int j=0; j< cityList.size(); j++) {
        			// 遍历省下面的所有市的数据
        			cityNames[j] = cityList.get(j).getName();
        			List<DistrictModel> districtList = cityList.get(j).getDistrictList();
        			String[] distrinctNameArray = new String[districtList.size()];
        			DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
        			for (int k=0; k<districtList.size(); k++) {
        				// 遍历市下面所有区/县的数据
        				DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
        				// 区/县对于的邮编，保存到mZipcodeDatasMap
        				mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
        				distrinctArray[k] = districtModel;
        				distrinctNameArray[k] = districtModel.getName();
					}
        			// 市-区/县的数据，保存到mDistrictDatasMap
        			mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
        		}
        		// 省-市的数据，保存到mCitisDatasMap
        		mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
        	}
        } catch (Throwable e) {
            e.printStackTrace();  
        } finally {
        	
        } 
	}
    
}
