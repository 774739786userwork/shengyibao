package com.bangware.shengyibao.returngood.presenter;

import com.bangware.shengyibao.deliverynote.model.entity.DeliveryNote;
import com.bangware.shengyibao.returngood.model.entity.ReturnNote;

/**
 * Created by Administrator on 2016/8/2.
 */
public interface RefundPrestener {
    /**
     * 退货单保存
     * @param returnNote
     */
    public void doSave(ReturnNote returnNote);

    public void destroy();
}
