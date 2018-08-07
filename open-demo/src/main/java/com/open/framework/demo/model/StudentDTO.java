package com.open.framework.demo.model;


import com.open.framework.dao.model.BaseDTO;
import com.open.framework.dao.model.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;


public class StudentDTO extends BaseDTO   {
    public StudentDTO(String name, String address, int age) {
        this.name = name;
        this.address = address;
        this.age = age;
    }

    public StudentDTO() {
        
    }

    private String name;
    private String address;
    private int age;
    /**
     *classroom对象的外键，不建议使用对象的方式关联 
     */
    private String cid;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}
}
