package com.open.framework.dao;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class HibernateInterceptor extends EmptyInterceptor {
    

    @Override
    public String onPrepareStatement(String sql) {

        String sql1=sql.replace("t_student student0_","t_student student0_ where del_state = false");
        System.out.println("execute sql: " + sql1);
        return super.onPrepareStatement(sql1);
    }
}