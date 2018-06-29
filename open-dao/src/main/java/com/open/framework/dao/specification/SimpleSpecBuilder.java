package com.open.framework.dao.specification;

import com.open.framework.commmon.BaseConstant;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class SimpleSpecBuilder<T> {

    /**
     * 条件列表
     */
    private List<SpecOper> opers;

    @SuppressWarnings("rawtypes")
	public static<T> SimpleSpecBuilder init(String key, String oper, Object value) {
    	return new SimpleSpecBuilder<T>(key, oper, value);
    }

    /**
     * 构造函数，初始化的条件是and
     */
    public SimpleSpecBuilder(String key, String oper, Object value) {
        SpecOper so = new SpecOper();
        so.setJoin(BaseConstant.DAO_AND);
        so.setKey(key);
        so.setOper(oper);
        so.setValue(value);
        opers = new ArrayList<SpecOper>();
        opers.add(so);
    }

    public SimpleSpecBuilder() {
        opers = new ArrayList<SpecOper>();
    }

    /**
     * 完成条件的添加
     * @return
     */
    public SimpleSpecBuilder<T> add(String key, String oper, Object value, String join) {
        SpecOper so = new SpecOper();
        so.setKey(key);
        so.setValue(value);
        so.setOper(oper);
        so.setJoin(join);
        opers.add(so);
        return this;
    }


    /**
     * 添加or条件的重载
     * @return this，方便后续的链式调用
     */
    public SimpleSpecBuilder<T> addOr(String key, String oper, Object value) {
        return this.add(key,oper,value,BaseConstant.DAO_OR);
    }

    /**
     * 添加and的条件
     * @return
     */
    public SimpleSpecBuilder<T> add(String key, String oper, Object value) {
        return this.add(key,oper,value,BaseConstant.DAO_AND);
    }


    public Specification<T> getSpec() {
        Specification<T> specification = new SimpleSpec<T>(opers);
        return specification;
    }
}