package com.bangware.shengyibao.ladingbilling.model;

import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenGoods;
import com.bangware.shengyibao.ladingbilling.presenter.OnDisburenSaveListener;
import com.bangware.shengyibao.user.model.entity.User;

import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 */

public interface DisburenModel {
    public void save(User user, String requestTag, List<DisburdenGoods> disburdenGoodsList,String carId,OnDisburenSaveListener listener);
}
