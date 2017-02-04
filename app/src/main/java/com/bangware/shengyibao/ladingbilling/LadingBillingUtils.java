package com.bangware.shengyibao.ladingbilling;

import android.util.Log;

import com.bangware.shengyibao.ladingbilling.model.entity.CarBean;
import com.bangware.shengyibao.ladingbilling.model.entity.LadingbillingQuery;
import com.bangware.shengyibao.ladingbilling.model.entity.QueryDisburdenBean;
import com.bangware.shengyibao.ladingbilling.model.entity.StockPrinterBean;
import com.bangware.shengyibao.model.Product;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 提货单json工具类
 * @author ccssll
 *
 */
public class LadingBillingUtils {
	//json解析数据方法
	public static List<LadingbillingQuery> getLadingBillingList(String jsonString){
		List<LadingbillingQuery> ladingQueries = new ArrayList<LadingbillingQuery>();
		try {
			JSONObject jsonObject = new JSONObject(jsonString.toString()).getJSONObject("data");
			JSONArray jay = jsonObject.getJSONArray("ladingbill_order_list");
			LadingbillingQuery ladingInfo = null;
			for (int i = 0; i < jay.length(); i++) {
				JSONObject job = jay.getJSONObject(i);
				ladingInfo = new LadingbillingQuery();
				ladingInfo.setLadingbilling_date(job.getString("loadingdate"));
				ladingInfo.setLadingbilling_numer(job.getString("serial_number"));
				ladingInfo.setLoadingcount(job.getString("loadingcount"));
				ladingInfo.setLadingbilling_person(job.getString("create_user_name"));
				ladingInfo.setLadingbilling_product(job.getString("goodsStr"));
				
				ladingQueries.add(ladingInfo);
				ladingInfo.setLadingbilling_total_count(jsonObject.getString("total_count"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ladingQueries;
	}

	public static List<CarBean> getLadingCar(String jsonString)
	{
		List<CarBean> carBeanList=new ArrayList<CarBean>();
		try {
			JSONObject objects=new JSONObject(jsonString);
			JSONArray jsonArray = objects.getJSONArray("data");
			CarBean carBean=null;
			for (int i=0;i<jsonArray.length();i++)
			{
				JSONObject object=jsonArray.getJSONObject(i);
				carBean=new CarBean();
				carBean.setCar_id(object.getString("carbaseinfo_id"));
				carBean.setCar_Number(object.getString("platenumber"));
				carBeanList.add(carBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return carBeanList;
	}


	public static List<QueryDisburdenBean> getLoadingDisburden(String jsonString)
	{
		List<QueryDisburdenBean> list=new ArrayList<QueryDisburdenBean>();
		try {
			JSONObject object=new JSONObject(jsonString);
			JSONObject jsonObject=object.getJSONObject("data");
			JSONArray jsonArray = jsonObject.getJSONArray("disburden_order_list");
			QueryDisburdenBean bean=null;
			CarBean carBean=null;
			for (int i=0;i<jsonArray.length();i++)
			{
				JSONObject obj=jsonArray.getJSONObject(i);
				bean=new QueryDisburdenBean();
				carBean=new CarBean();
				bean.setCreate_user_name(obj.getString("create_user_name"));
				bean.setDisburn_id(obj.getString("disburden_orde_id"));
				bean.setDisburn_numer(obj.getString("serial_numbers"));
				bean.setDisburn_time(obj.getString("disburden_date"));
				bean.setProduct_name(obj.getString("goodsStr"));
				carBean.setCar_id(obj.getString("carbaseinfo_id"));
				carBean.setCar_Number(obj.getString("car_number"));
				bean.setCarBean(carBean);
				list.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<StockPrinterBean> getPrinterData(String jsonString){
		List<StockPrinterBean> stockPrinterBeanList = new ArrayList<StockPrinterBean>();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray jsonArray = jsonObject.getJSONArray("data");
			StockPrinterBean stockPrinterBean = null;
			CarBean carBean = null;
			Product product = null;
			for (int i = 0 ;i < jsonArray.length(); i++){
				JSONObject obj = jsonArray.getJSONObject(i);
				stockPrinterBean = new StockPrinterBean();

				carBean = new CarBean();
				carBean.setCar_id(obj.getString("carbaseinfo_id"));
				carBean.setCar_Number(obj.getString("carnumber"));

				JSONArray productArr = obj.getJSONArray("good_list");
				for (int j = 0; j < productArr.length(); j++){
					JSONObject proObj = productArr.getJSONObject(j);
					product = new Product();
					product.setId(proObj.getString("product_id"));
					product.setName(proObj.getString("product_name"));
					product.setStock(proObj.getInt("quantity"));

					stockPrinterBean.addProducts(product);
				}
				stockPrinterBean.setCarBean(carBean);
				stockPrinterBeanList.add(stockPrinterBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return stockPrinterBeanList;
	}

}
