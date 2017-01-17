package com.bangware.shengyibao.ladingbilling.model.entity;

import com.bangware.shengyibao.model.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/9.
 */

public class DisburdenGoods implements Serializable{
    private String disburden;//卸货
    private String surplus;//余货
    private Product product;

    public String getDisburden() {
        return disburden;
    }

    public void setDisburden(String disburden) {
        this.disburden = disburden;
    }

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
