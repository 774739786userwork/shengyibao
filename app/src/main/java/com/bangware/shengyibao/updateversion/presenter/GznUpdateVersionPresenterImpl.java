package com.bangware.shengyibao.updateversion.presenter;

import com.bangware.shengyibao.updateversion.VersionUpdateView;
import com.bangware.shengyibao.updateversion.model.VersionModel;
import com.bangware.shengyibao.updateversion.model.entity.VersionBean;
import com.bangware.shengyibao.updateversion.model.impl.GZNVersionMoedlImpl;
import com.bangware.shengyibao.updateversion.model.impl.VersionModelImpl;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.volley.DataRequest;


/**
 * 硅藻泥版本更新访问后台接口的实现类
 * @author zcb
 *
 */
public class GznUpdateVersionPresenterImpl implements UpdateVersionPresenter,VersionListener{
	private VersionModel vmodel;
	public static final String REQUEST_TAG = "UpdateVersion";
	private String requestTag;
	private VersionUpdateView updateView;

	public GznUpdateVersionPresenterImpl(VersionUpdateView mUpdateView) {
		// TODO Auto-generated constructor stub
		requestTag = REQUEST_TAG+System.currentTimeMillis();
		this.vmodel = new GZNVersionMoedlImpl();
		this.updateView=mUpdateView;
	}
	@Override
	public void versionUpdate(User user) {
		// TODO Auto-generated method stub
		vmodel.versionUpdate(requestTag,this, user);
	}
	
	@Override
	public void onUpdateVersion(VersionBean beaninfo) {
		// TODO Auto-generated method stub
		updateView.doUpdateVersionSuccess(beaninfo);
	}
	@Override
	public void onErrors(String errorMessage) {
		// TODO Auto-generated method stub
		
	}
	
	public void destroy(){
		DataRequest.getInstance().cancelRequestByTag(requestTag);
	}

}
