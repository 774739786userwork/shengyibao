package com.bangware.shengyibao.ladingbilling.model.entity;
import com.bangware.shengyibao.model.Product;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 余货打印
 * Created by bangware on 2017/1/24.
 */

public class StockPrinterBean implements Serializable{
    private CarBean carBean;
    private List<Product> list = new ArrayList<Product>();
    public CarBean getCarBean() {
        return carBean;
    }

    public void setCarBean(CarBean carBean) {
        this.carBean = carBean;
    }

    public List<Product> getList() {
        return list;
    }

    public void setList(List<Product> list) {
        this.list = list;
    }
    public void addProducts(Product product){
        this.list.add(product);
    }
}
