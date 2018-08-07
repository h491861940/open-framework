/*
 * Copyright 2017 Neusoft All right reserved. This software is the confidential and proprietary information of Neusoft
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with Neusoft.
 */

package com.open.framework.commmon.utils;


import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



public class BeanUtil extends BeanUtils {

    private static Lock                    initLock      = new ReentrantLock();

    private static Map<String, BeanCopier> beanCopierMap = new HashMap<String, BeanCopier>();

    /**
     * 初始化 BeanCopier
     * 
     * @param source
     * @param target
     * @return
     */
    private static BeanCopier initCopier(Class source, Class target) {
        initLock.lock();
        BeanCopier find = beanCopierMap.get(source.getName() + "_" + target.getName());
        if (find != null) {
            initLock.unlock();
            return find;
        }
        BeanCopier beanCopier = BeanCopier.create(source, target, false);
        beanCopierMap.put(source.getName() + "_" + target.getName(), beanCopier);
        initLock.unlock();
        return beanCopier;
    }

    /**
     * 获取BeanCopier
     * 
     * @param source
     * @param target
     * @return
     */
    private static BeanCopier getBeanCopier(Class source, Class target) {
        BeanCopier beanCopier = beanCopierMap.get(source.getClass().getName() + "_" + target.getName());
        if (beanCopier != null) {
            return beanCopier;
        }
        return initCopier(source, target);
    }

    /**
     * BeanToBean
     * 
     * @param source
     * @param targetSource
     * @return
     */
    public static Object convert(Object source, Object targetSource) {
        if (source == null) {
            return null;
        }
        BeanCopier beanCopier = getBeanCopier(source.getClass(), targetSource.getClass());
        beanCopier.copy(source, targetSource, null);
        return targetSource;
    }

    /**
     * Pojo 类型转换（浅复制，字段名&类型相同则被复制）
     * 
     * @param source
     * @param targetClass
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> T convert(Object source, Class<T> targetClass) throws InstantiationException,
                                                                     IllegalAccessException {
        if (source == null) {
            return null;
        }
        BeanCopier beanCopier = getBeanCopier(source.getClass(), targetClass);
        T target = targetClass.newInstance();
        beanCopier.copy(source, target, null);
        return target;
    }

    /**
     * bean转map
     * 
     * @param o
     * @return Map
     */
    public static Map beanToMap(Object o) {
        if (o == null) {
            return null;
        }
        BeanMap m = BeanMap.create(o);
        return m;
    }

    /**
     * 将map装换为javabean对象
     * 
     * @param map
     * @param bean
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        if (map == null || bean == null) {
            return null;
        }
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

}
