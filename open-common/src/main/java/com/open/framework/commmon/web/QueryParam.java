package com.open.framework.commmon.web;

import java.util.List;

public class QueryParam {
    private List<SpecOper> specOpers;

    private PageBean pageBean;

    private String orderBy;

    public List<SpecOper> getSpecOpers() {
        return specOpers;
    }

    public void setSpecOpers(List<SpecOper> specOpers) {
        this.specOpers = specOpers;
    }

    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
