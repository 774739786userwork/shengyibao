package com.bangware.shengyibao.customer.model.entity;

import com.bangware.shengyibao.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 * 我的客户的行政区域下的实体类
 */

public class CustomerTypes {
    private  int id;
    private String GovernmentArea;
    private List<Customer> customerList= new ArrayList<Customer>();

    public String getGovernmentArea() {
        return GovernmentArea;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGovernmentArea(String governmentArea) {
        GovernmentArea = governmentArea;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }
    public void addCustomerList(Customer customer){
        this.customerList.add(customer);
    }
}
