package com.bangware.shengyibao.activity;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.bangware.shengyibao.model.CityModel;
import com.bangware.shengyibao.model.DistrictModel;
import com.bangware.shengyibao.model.ProvinceModel;
import com.bangware.shengyibao.net.ConnectionChangeReceiver;
import com.bangware.shengyibao.utils.AppContext;


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

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerReceiver();
		loadingdialog = CustomProgressDialog.createDialog(this,R.drawable.frame);
		setDefaultFont();

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
