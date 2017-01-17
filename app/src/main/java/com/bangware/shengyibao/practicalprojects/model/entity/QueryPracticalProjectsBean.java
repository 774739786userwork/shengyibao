package com.bangware.shengyibao.practicalprojects.model.entity;

import com.bangware.shengyibao.customer.model.entity.CustomerImage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/28.
 */

public class QueryPracticalProjectsBean implements Serializable{
    private String id;
    private String employee_name;
    private String content;
    private List<CustomerImage> images = new ArrayList<CustomerImage>();
    private int total;

    public List<CustomerImage> getImages() {
        return images;
    }

    public void setImages(List<CustomerImage> images) {
        this.images = images;
    }

    public void addImages(CustomerImage images){
        this.images.add(images);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
