package com.open.framework.dao.other;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


/**
 * 仅仅只是对size进行了封装，太过简单
 *
 */
public class PageUtil {
	

	public static final int size = 10;
	
	public static Pageable getPage(int page, int size, Sort sort) {
		if(sort==null) return PageRequest.of(page-1, size);
		return PageRequest.of(page-1, size, sort);
	}
	
	public static Pageable getPage(int page) {
		return getPage(page,size,null);
	}
	public static Pageable getPage(int page, int size) {
		return getPage(page,size,null);
	}
	public static Pageable getPage(int page, Sort sort) {
		return getPage(page,size,sort);
	}
}
