package com.bangware.shengyibao.practicalprojects.model.entity;


import com.bangware.shengyibao.customer.model.entity.CustomerImage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyBean implements Serializable {
	private static final long serialVersionUID = -7060210544600464482L;
	private int id;
	private String employee_name;
	private String content;
	private List<CustomerImage> images = new ArrayList<CustomerImage>();
	private int total;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmployee_name() {
		return employee_name;
	}

	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}

	public List<CustomerImage> getImages() {
		return images;
	}

	public void setImages(List<CustomerImage> images) {
		this.images = images;
	}

	public void addImages(CustomerImage images){
		this.images.add(images);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}
