package com.bangware.shengyibao.ladingbilling.model.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/18.
 */

public class QueryDisburdenBean implements Serializable {
    private String disburn_time;
    private String disburn_id;
    private String disburn_numer;
    private String Product_name;
    private String create_user_name;
    private CarBean carBean;

    public CarBean getCarBean() {
        return carBean;
    }

    public String getCreate_user_name() {
        return create_user_name;
    }

    public void setCreate_user_name(String create_user_name) {
        this.create_user_name = create_user_name;
    }

    public void setCarBean(CarBean carBean) {
        this.carBean = carBean;
    }

    public String getDisburn_numer() {
        return disburn_numer;
    }

    public void setDisburn_numer(String disburn_numer) {
        this.disburn_numer = disburn_numer;
    }

    public String getDisburn_time() {
        return disburn_time;
    }

    public void setDisburn_time(String disburn_time) {
        this.disburn_time = disburn_time;
    }


    public String getDisburn_id() {
        return disburn_id;
    }

    public void setDisburn_id(String disburn_id) {
        this.disburn_id = disburn_id;
    }


    public String getProduct_name() {
        return Product_name;
    }

    public void setProduct_name(String product_name) {
        Product_name = product_name;
    }

}
