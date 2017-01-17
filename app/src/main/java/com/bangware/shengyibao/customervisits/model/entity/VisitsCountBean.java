package com.bangware.shengyibao.customervisits.model.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/15.
 */

public class VisitsCountBean implements Serializable {
    public String employe_name;//业务员名字
    public String employe_id;//业务员id
    public int Visits_Num;//拜访次数
    public int Order_Num;//出单次数
    public int Visits_Order_Num;//初访出单次数
    public String Order_Precent;//出单率
    public String Visits_Order_Precent;//初访出单率
    public int COUNT_VISITS_NUM;//累计拜访次数
    public int Count_Order_Num;//累计出单次数

    public String getEmploye_name() {
        return employe_name;
    }

    public void setEmploye_name(String employe_name) {
        this.employe_name = employe_name;
    }

    public String getEmploye_id() {
        return employe_id;
    }

    public void setEmploye_id(String employe_id) {
        this.employe_id = employe_id;
    }

    public int getVisits_Num() {
        return Visits_Num;
    }

    public void setVisits_Num(int visits_Num) {
        Visits_Num = visits_Num;
    }

    public int getOrder_Num() {
        return Order_Num;
    }

    public void setOrder_Num(int order_Num) {
        Order_Num = order_Num;
    }

    public int getVisits_Order_Num() {
        return Visits_Order_Num;
    }

    public void setVisits_Order_Num(int visits_Order_Num) {
        Visits_Order_Num = visits_Order_Num;
    }

    public String getOrder_Precent() {
        return Order_Precent;
    }

    public void setOrder_Precent(String order_Precent) {
        Order_Precent = order_Precent;
    }

    public String getVisits_Order_Precent() {
        return Visits_Order_Precent;
    }

    public void setVisits_Order_Precent(String visits_Order_Precent) {
        Visits_Order_Precent = visits_Order_Precent;
    }

    public int getCount_Order_Num() {
        return Count_Order_Num;
    }

    public void setCount_Order_Num(int count_Order_Num) {
        Count_Order_Num = count_Order_Num;
    }

    public int getCOUNT_VISITS_NUM() {
        return COUNT_VISITS_NUM;
    }

    public void setCOUNT_VISITS_NUM(int COUNT_VISITS_NUM) {
        this.COUNT_VISITS_NUM = COUNT_VISITS_NUM;
    }
}
