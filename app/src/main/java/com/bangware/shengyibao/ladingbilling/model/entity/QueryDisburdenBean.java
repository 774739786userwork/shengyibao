package com.bangware.shengyibao.ladingbilling.model.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/18.
 */

public class QueryDisburdenBean implements Serializable {
    private String disburn_id;
    private String CarId;
    private String Product_name;
    private String Prpduct_count;


    public String getDisburn_id() {
        return disburn_id;
    }

    public void setDisburn_id(String disburn_id) {
        this.disburn_id = disburn_id;
    }

    public String getCarId() {
        return CarId;
    }

    public void setCarId(String carId) {
        CarId = carId;
    }

    public String getProduct_name() {
        return Product_name;
    }

    public void setProduct_name(String product_name) {
        Product_name = product_name;
    }

    public String getPrpduct_count() {
        return Prpduct_count;
    }

    public void setPrpduct_count(String prpduct_count) {
        Prpduct_count = prpduct_count;
    }
}
