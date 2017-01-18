package com.bangware.shengyibao.ladingbilling.presenter.impl;

import com.bangware.shengyibao.ladingbilling.model.LadingbillingQueryModel;
import com.bangware.shengyibao.ladingbilling.model.entity.CarBean;
import com.bangware.shengyibao.ladingbilling.model.entity.LadingbillingQuery;
import com.bangware.shengyibao.ladingbilling.model.impl.LadingbillingQueryModelImpl;
import com.bangware.shengyibao.ladingbilling.presenter.LadingbillingPresenter;
import com.bangware.shengyibao.ladingbilling.presenter.OnLadingBillingListener;
import com.bangware.shengyibao.ladingbilling.view.LadingbillingQueryView;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.volley.DataRequest;

import java.util.List;

public class LadingbillingPresenterImpl implements LadingbillingPresenter,OnLadingBillingListener {
	
	public static final String REQUEST_TAG = "Ladingbill";
	private LadingbillingQueryModel queryModel;
	private LadingbillingQueryView queryView;
	private String requestTag;
	
	public LadingbillingPresenterImpl(LadingbillingQueryView queryView) {
		this.requestTag = REQUEST_TAG+System.currentTimeMillis();
		this.queryView=queryView;
		this.queryModel = new LadingbillingQueryModelImpl();
	}
	
	@Override
	public void loadLadingBilling(User user,String begin_date, String end_date,
								  int nPage, int nSpage) {
		// TODO Auto-generated method stub
		queryView.showDialog();
		queryModel.LoadLadingBillingData(requestTag, begin_date, end_date, nPage, nSpage, user, this);
	}

	@Override
	public void loadCarBean(User user) {
		queryView.showDialog();
		queryModel.onLoadCarBean(requestTag,user,this);
	}


	@Override
	public void onLoadDataSuccess(List<LadingbillingQuery> ladingbillinglist) {
		// TODO Auto-generated method stub
		queryView.hideDialog();
		queryView.addLadingbillingData(ladingbillinglist);
	}

	@Override
	public void onLoadDataFailure(String errorMessage) {
		// TODO Auto-generated method stub
		queryView.hideDialog();
		queryView.showFailureMsg(errorMessage);
	}

	@Override
	public void onCarBeanLoaded(List<CarBean> carBeanList) {
		queryView.hideDialog();
		queryView.loadCarBean(carBeanList);
	}


	public void destroy(){
		DataRequest.getInstance().cancelRequestByTag(requestTag);
	}
}
