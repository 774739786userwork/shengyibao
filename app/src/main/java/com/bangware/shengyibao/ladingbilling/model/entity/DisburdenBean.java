package com.bangware.shengyibao.ladingbilling.model.entity;

import android.util.Log;

import com.bangware.shengyibao.deliverynote.model.entity.DeliveryNote;
import com.bangware.shengyibao.deliverynote.model.entity.DeliveryNoteGoods;
import com.bangware.shengyibao.model.Product;
import com.bangware.shengyibao.shopcart.model.entity.ShopCartGoods;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/9.
 */

public class DisburdenBean  implements Serializable{
    private String dateTime;
     private Map<String,DisburdenGoods> goodsList = new HashMap<String,DisburdenGoods>();
    public Map<String, DisburdenGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(Map<String, DisburdenGoods> goodsList) {
        this.goodsList = goodsList;
    }
    public List<DisburdenGoods> getAllGoodsList(){
        List<DisburdenGoods> list = new ArrayList<DisburdenGoods>();
        for(String key : goodsList.keySet()){
            list.add(goodsList.get(key));
        }
        return list;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public DisburdenGoods getGoods(String productId){
        return goodsList.get(productId);
    }

    public void addProductGoods(DisburdenGoods disburdenGoods){
        this.goodsList.put(disburdenGoods.getProduct().getId(), disburdenGoods);
    }
    public void removeProductGoods(Product product){
        this.goodsList.remove(product.getId());
    }
}
