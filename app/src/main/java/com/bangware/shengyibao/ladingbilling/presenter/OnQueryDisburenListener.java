package com.bangware.shengyibao.ladingbilling.presenter;


import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenGoods;
import com.bangware.shengyibao.ladingbilling.model.entity.QueryDisburdenBean;

import java.util.List;

public interface OnQueryDisburenListener {
	public void onSaveSuccess(List<QueryDisburdenBean> queryDisburdenBeanList);
	public void onCancelSuccess(String message);
	public void onError(String message);
}
