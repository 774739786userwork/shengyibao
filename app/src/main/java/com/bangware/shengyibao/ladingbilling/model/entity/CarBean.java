package com.bangware.shengyibao.ladingbilling.model.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/17.
 * 
 */

public class CarBean implements Serializable{
    private String Car_id;
    private String Car_Number;

    public String getCar_id() {
        return Car_id;
    }

    public void setCar_id(String car_id) {
        Car_id = car_id;
    }

    public String getCar_Number() {
        return Car_Number;
    }

    public void setCar_Number(String car_Number) {
        Car_Number = car_Number;
    }
}
