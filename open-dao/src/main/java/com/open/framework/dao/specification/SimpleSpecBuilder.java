package com.open.framework.dao.specification;

import com.open.framework.commmon.BaseConstant;
import com.open.framework.commmon.exceptions.PlatformException;
import com.open.framework.commmon.utils.StringUtil;
import com.open.framework.commmon.web.SpecOper;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class SimpleSpecBuilder<T> {

    /**
     * 条件列表
     */
    private List<Object> opers;

    @SuppressWarnings("rawtypes")
	public static<T> SimpleSpecBuilder init(String key, String oper, Object value) {
    	return new SimpleSpecBuilder<T>(key, oper, value);
    }

    /**
     * 构造函数，初始化的条件是and
     */
    public SimpleSpecBuilder(String key, String oper, Object value) {
        SpecOper so = new SpecOper();
        so.setKey(key);
        so.setOper(oper);
        so.setValue(value);
        opers = new ArrayList<Object>();
        opers.add(so);
    }

    public SimpleSpecBuilder() {
        opers = new ArrayList<Object>();
    }
    public SimpleSpecBuilder(List<Object> specOpers) {
        opers = new ArrayList<Object>();
        opers.addAll(specOpers);
    }
    /**
     * 完成条件的添加
     * @return
     */
    private SimpleSpecBuilder<T> addWhere(String key, String oper, Object value, String join) {
        SpecOper so = new SpecOper();
        so.setKey(key);
        so.setValue(value);
        so.setOper(oper);
        so.setJoin(join);
        opers.add(so);
        return this;
    }
    private SimpleSpecBuilder<T> addWhere(String[] keys, String[] opers, Object[] values, String join) {
        List temp=new ArrayList();
        if((null!=keys && keys.length>0) && (keys.length==opers.length && opers.length==values.length)){
            for (int i = 0; i < keys.length; i++) {
                SpecOper so = new SpecOper();
                so.setKey(keys[i]);
                so.setValue(values[i]);
                so.setOper(opers[i]);
                so.setJoin(join);
                temp.add(so);
            }
            return this.addList(temp);
        }else {
            throw  new PlatformException("参数个数不匹配,请确认参数个数 key:"+keys+",opers:"+opers+",values:"+values);
        }

    }

    /**
     * 添加or条件的重载
     * @return this，方便后续的链式调用
     */
    public SimpleSpecBuilder<T> or(String key, String oper, Object value) {
        return this.addWhere(key,oper,value,BaseConstant.DAO_OR);
    }
    /**
     * @Author hsj
     * @Description of方式的集合,条件组成一个整体
     * @Date  2018-07-15 17:18:59
     * @Param null:
     * @return
     **/
    public SimpleSpecBuilder<T> or(String[] keys, String[] opers, Object[] values) {
        return this.addWhere(keys,opers,values,BaseConstant.DAO_OR);
    }

    /**
     * @Author hsj
     * @Description 添加单个的查询方法
     * @Date  2018-07-15 17:18:09
     * @Param null: 
     * @return 
     **/
    public SimpleSpecBuilder<T> and(String key, String oper, Object value) {
        return this.addWhere(key,oper,value,BaseConstant.DAO_AND);
    }
    /**
     * @Author hsj
     * @Description  添加组合的查询方法,根据数组,组织一个集合,然后带括号
     * @Date  2018-07-15 17:18:24
     * @Param null: 
     * @return 
     **/
    public SimpleSpecBuilder<T> and(String[] keys, String[] opers, Object[] values) {
        return this.addWhere(keys,opers,values,BaseConstant.DAO_AND);
    }
    private SimpleSpecBuilder<T> addList(List<Object> specOpers) {
        opers.add(specOpers);
        return this;
    }
    public Specification<T> getSpec() {
        Specification<T> specification = new SimpleSpec<T>(opers);
        return specification;
    }
}