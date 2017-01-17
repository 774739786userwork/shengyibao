package com.bangware.shengyibao.ladingbilling.view;

import com.bangware.shengyibao.activity.BaseView;
import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenGoods;

import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 */

public interface DisburdenView extends BaseView {
    void doSaveDisburdenSuccess(List<DisburdenGoods> disburdenGoodsList);
}
