package com.open.framework.dao;

import com.open.framework.dao.model.BaseEntity;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;

import java.io.Serializable;
import java.util.*;
import org.hibernate.type.Type;
public class HibernateInterceptor extends EmptyInterceptor {
    

    @Override
    public String onPrepareStatement(String sql) {

       /* String sql1=sql.replace("t_student student0_","t_student student0_ where del_state = false");
        System.out.println("execute sql: " + sql1);*/
        return super.onPrepareStatement(sql);
    }
    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types)
    {
        System.out.println("修改功能");
       /* setValue(currentState, propertyNames, "createId", "admin");
        setValue(currentState, propertyNames, "modifyDate", new Date());*/
        return true;
    }

    /**
     * 数据新增时.
     */
    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
    {
        System.out.println("新增功能");
        /*setValue(state, propertyNames, "createId", "admin");
        setValue(state, propertyNames, "modifyDate", new Date());
        getValue(state, propertyNames, "modifyId");*/
        return true;
    }


    /**
     * TODO 简单描述该方法的实现功能（可选）.
     * @see org.hibernate.EmptyInterceptor#onDelete(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
     */
    public void onDelete(Object entity, Serializable id,  Object[] state, String[] propertyNames,  Type[] types)
    {
        System.out.println("删除功能");
    }

    /**
     * 设置新值
     *
     * @param currentState
     * @param propertyNames
     * @param propertyToSet
     * @param value
     */
    private void setValue(Object[] currentState, String[] propertyNames, String propertyToSet, Object value)
    {
        int index = Arrays.asList(propertyNames).indexOf(propertyToSet);
        if (index >= 0)
        {
            currentState[index] = value;
        }
    }

    /**
     * 返回原值
     * @param previousState
     * @param propertyNames
     * @param propertyToGet
     * @return
     */
    private Object getValue(Object[] previousState, String[] propertyNames, String propertyToGet)
    {
        int index = Arrays.asList(propertyNames).indexOf(propertyToGet);
        if (index >= 0)
        {
            return previousState[index];
        }
        return null;
    }
}