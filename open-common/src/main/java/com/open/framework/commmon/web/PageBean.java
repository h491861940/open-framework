package com.open.framework.commmon.web;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
/**
 * @description: 分页对象
 * @author: hsj
 * @date: 2019-08-23 18:15:48
 */
@Data
public class PageBean implements Serializable {
    /**
     * 当前页
     */
    private int page;
    /**
     * 当前页显示记录条数
     */
    private int pageSize;
    /**
     * 取得查询总记录数
     */
    private long count;
    /**
     * 查询返回的list
     */
    private List rows;
    /**
     *  当前页的记录数
     */
    private int numberOfElements;
    /**
     *  获取总记录数
     */
    private long totalElements;
    /**
     * 总共多少页
     */
    private int totalPages;


}
