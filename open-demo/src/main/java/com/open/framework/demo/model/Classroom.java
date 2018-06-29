package com.open.framework.demo.model;

import com.open.framework.dao.model.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name="t_classroom")
public class Classroom extends BaseEntity {
	private String name;
	private String grade;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
}
