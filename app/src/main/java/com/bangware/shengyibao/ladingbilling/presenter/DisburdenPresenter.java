package com.bangware.shengyibao.ladingbilling.presenter;
import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenGoods;
import com.bangware.shengyibao.user.model.entity.User;

import java.util.List;

public interface DisburdenPresenter {

	/**
	 * 余货卸货保存
	 */
	public void doDisburenSave(User user, List<DisburdenGoods> disburdenGoodsList,String carId);
	
	public void destroy();
}
