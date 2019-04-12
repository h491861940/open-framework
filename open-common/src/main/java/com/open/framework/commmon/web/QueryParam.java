package com.open.framework.commmon.web;

import java.util.List;

public class QueryParam {
    /**
     * 条件集合,前端传入,然后解析
     */
    private List<Object> specOpers;
    /**
     * 分页对象
     */
    private PageBean pageBean;
    /**
     * 排序字段,"name,code_d" desc的时候需要加_d,多个字段使用","隔开
     */
    private String orderBy;
    /**
     * 是否显示统计数量,如果false不显示,默认不显示
     */
    private boolean showCount=false;

    public List<Object> getSpecOpers() {
        return specOpers;
    }

    public void setSpecOpers(List<Object> specOpers) {
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

    public boolean getShowCount() {
        return showCount;
    }

    public void setShowCount(boolean showCount) {
        this.showCount = showCount;
    }
}
