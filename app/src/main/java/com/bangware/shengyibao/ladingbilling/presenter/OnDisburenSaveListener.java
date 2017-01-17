package com.bangware.shengyibao.ladingbilling.presenter;


import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenGoods;

import java.util.List;

public interface OnDisburenSaveListener {
	public void onSaveSuccess(List<DisburdenGoods> disburdenGoodsList);
	
	public void onError(String message);
}
