package com.open.framework.commmon.web;

import lombok.Data;

import java.util.List;
/**
 * @description: 查询对象
 * @author: hsj
 * @date: 2019-08-23 18:16:12
 */
@Data
public class QueryParam {
    /**
     * 条件集合,前端传入,然后解析,不能具体化,否则不能放map和list,为了放map,list,不能具体化SpecOper
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


}
