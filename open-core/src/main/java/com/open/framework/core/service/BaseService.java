package com.open.framework.core.service;

import com.open.framework.commmon.web.ExportData;
import com.open.framework.commmon.web.QueryParam;

import java.util.List;

/**
 * 基本的service,单表继承这个,可以不用些crud
 *
 * @param <T> 实体
 * @param <D> DTO
 */
public interface BaseService<T, D> {
    /**
     * 保存实体
     *
     * @param t
     * @return
     */
    T save(T t);

    /**
     * 保存实体,参数是DTO,传进来以后,根据DTO去找实体,然后再保存实体
     *
     * @param dto
     * @return
     */
    T saveDto(D dto);

    D getDto(String id);

    D getDto(Class dtoClass, String id);

    T get(Class<T> entityClass, String id);

    void delete(Class<T> entityClass, String id);

    void delete(T t);

    void deleteBatchDto(Class dtoClass, List<String> ids);

    void deleteBatch(Class entityClass, List<String> ids);

    Object query(Class dto, QueryParam queryParam);

    boolean exists(Class entityClass, String propertyName, Object value);

    boolean exists(Class<?> entityClass, String[] propertyName, Object[] value);

    boolean existsById(Class entityClass, String id);

    void export(Class dtoClass, ExportData exportData);
}