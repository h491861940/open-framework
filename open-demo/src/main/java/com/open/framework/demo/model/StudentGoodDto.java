package com.open.framework.demo.model;

public class StudentGoodDto {
	private String sid;
	private String sname;
	private String claId;
	private String claName;
	private String grade;
	
	public StudentGoodDto() {}
	
	public StudentGoodDto(String sid,String sname, String claId, String claName, String grade) {
		super();
		this.sid = sid;
		this.sname = sname;
		this.claId = claId;
		this.claName = claName;
		this.grade = grade;
	}
	
	
	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getClaId() {
		return claId;
	}
	public void setClaId(String claId) {
		this.claId = claId;
	}
	public String getClaName() {
		return claName;
	}
	public void setClaName(String claName) {
		this.claName = claName;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
}
