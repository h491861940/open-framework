package com.open.framework.commmon.web;

import java.io.Serializable;
import java.util.List;

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
    private int numberOfElements;
    private long totalElements;
    private int totalPages;
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }


    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
