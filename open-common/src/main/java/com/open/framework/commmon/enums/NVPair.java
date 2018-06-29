package com.open.framework.commmon.enums;


public class NVPair
{
	/**
	 * 值，整形
	 */
	private Integer val;
	/**
	 * 主键，字符串
	 */
	private String gid;
	/**
	 * 编码，字符串
	 */
	private String code;
	/**
	 * 汉字
	 */
	private String text;

	public NVPair()
	{

	}

	public NVPair(Integer val, String text)
	{
		super();
		this.val = val;
		this.text = text;
	}

	public NVPair(String code, String text)
	{
		super();
		this.code = code;
		this.text = text;
	}
	public NVPair(Integer val, String code, String text)
	{
		super();
		this.val = val;
		this.code = code;
		this.text = text;
	}

	public NVPair(Integer val, String gid, String code, String text)
	{
		super();
		this.val = val;
		this.gid = gid;
		this.code = code;
		this.text = text;
	}

	public Integer getVal()
	{
		return val;
	}

	public void setVal(Integer val)
	{
		this.val = val;
	}

	public String getGid()
	{
		return gid;
	}

	public void setGid(String gid)
	{
		this.gid = gid;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

}
