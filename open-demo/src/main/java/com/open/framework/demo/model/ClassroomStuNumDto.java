package com.open.framework.demo.model;


public class ClassroomStuNumDto {
	private String cid;
	private String cname;
	private String grade;
	private long snum;

	public ClassroomStuNumDto(String cid, String cname, String grade, long snum) {
		super();
		this.cid = cid;
		this.cname = cname;
		this.grade = grade;
		this.snum = snum;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public long getSnum() {
		return snum;
	}

	public void setSnum(long snum) {
		this.snum = snum;
	}
}
