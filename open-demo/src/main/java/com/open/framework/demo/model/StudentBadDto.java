package com.open.framework.demo.model;

import com.open.framework.dao.model.BaseDTO;

public class StudentBadDto extends BaseDTO {

	private Student stu;
	private Classroom cla;


	public StudentBadDto(Student stu, Classroom cla) {
		super();
		this.stu = stu;
		this.cla = cla;
	}

	public StudentBadDto() {
	}

	public Student getStu() {
		return stu;
	}
	public void setStu(Student stu) {
		this.stu = stu;
	}
	public Classroom getCla() {
		return cla;
	}
	public void setCla(Classroom cla) {
		this.cla = cla;
	}
}
