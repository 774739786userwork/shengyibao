package com.bangware.shengyibao.ladingbilling.model;


import com.bangware.shengyibao.ladingbilling.presenter.OnLadingBillingListener;
import com.bangware.shengyibao.ladingbilling.presenter.OnQueryDisburenListener;
import com.bangware.shengyibao.user.model.entity.User;


public interface QueryDisburdenModel {
	void LoadQueryDisburden(String requestTag, User user, OnQueryDisburenListener disburenListener);
	void OnCancellationDisburden(String requestTag,User user,String disburden_id,OnQueryDisburenListener disburenListener);
}
