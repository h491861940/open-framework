package com.open.framework.core.service.impl;

import com.open.framework.commmon.exceptions.PlatformException;
import com.open.framework.commmon.utils.BeanUtil;
import com.open.framework.commmon.utils.ClassUtil;
import com.open.framework.commmon.web.ExportData;
import com.open.framework.commmon.web.PageBean;
import com.open.framework.commmon.web.QueryParam;
import com.open.framework.core.dao.IBaseDao;
import com.open.framework.core.service.BaseService;
import com.open.framework.core.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 基础的crud提供类
 * @author: hsj
 * @date: 2019-08-23 18:06:33
 */
@Service
public class BaseServiceImpl<T, D> implements BaseService<T, D> {

    @Autowired
    IBaseDao baseDao;

    @Autowired
    ExportService exportService;

    public Class<T> getEntityClass(String dtoName) {
        Class<T> entityClass = ClassUtil.getEntityClass(this.getClass());
        try {
            //判断是否有entityClass,用处是判断是否有继承的子类,有子类就有泛型,没有的话根据dto去找实体
            if (null == entityClass) {
                entityClass = baseDao.getEntityByDto(dtoName);
            }
            return entityClass;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlatformException(dtoName + "没有找见对应的实体,实体默认为:" + dtoName.replace("DTO", ""));
        }
    }

    @Override
    public T save(T t) {
        return (T) baseDao.saveOrUpdate(t);
    }

    @Override
    public T saveDto(D dto) {
        Class entityClass = getEntityClass(dto.getClass().getSimpleName());
        try {
            T entity = (T) entityClass.newInstance();
            BeanUtil.copyProperties(dto, entity);
            entity = save(entity);
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlatformException(e.getMessage());
        }
    }

    @Override
    public D getDto(String id) {
        Class<D> dtoClass = ClassUtil.getDtoClass(this.getClass());
        return getDto(dtoClass, id);
    }

    @Override
    public D getDto(Class dtoClass, String id) {
        Class<T> entityClass = getEntityClass(dtoClass.getSimpleName());
        T entity = get(entityClass, id);
        try {
            D dto = (D) dtoClass.newInstance();
            BeanUtil.copyProperties(entity, dto);
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlatformException(e.getMessage());
        }
    }

    @Override
    public T get(Class<T> entityClass, String id) {
        return baseDao.get(entityClass, id);
    }

    @Override
    public void delete(Class<T> entityClass, String id) {
        baseDao.removeById(entityClass, id);
    }

    @Override
    public void delete(T t) {
        baseDao.remove(t);
    }

    @Override
    public void deleteBatch(Class entityClass, List<String> ids) {
        baseDao.removeAll(entityClass, ids);
    }

    @Override
    public void deleteBatchDto(Class dtoClass, List<String> ids) {
        Class<T> entityClass = getEntityClass(dtoClass.getSimpleName());
        baseDao.removeAllIdHql(entityClass, ids);
    }

    @Override
    public boolean existsById(Class entityClass, String id) {
        return baseDao.isExist(entityClass, baseDao.getIdName(entityClass), id);
    }

    @Override
    public void export(Class dtoClass, ExportData exportData) {
       Object object= query(dtoClass,exportData.getQueryParam());
        if (object instanceof PageBean) {
            exportData.setDatas( ((PageBean) object).getRows());
        } else {
            exportData.setDatas((List) object);
        }
        exportService.export(exportData);
    }

    @Override
    public Object query(Class dto, QueryParam queryParam) {
        Class entityClass = getEntityClass(dto.getSimpleName());
        return baseDao.findAll(entityClass, queryParam);
    }

    @Override
    public boolean exists(Class entityClass, String propertyName, Object value) {
        return exists(entityClass, new String[]{propertyName}, new Object[]{value});
    }

    @Override
    public boolean exists(Class<?> entityClass, String[] propertyName, Object[] value) {
        return baseDao.isExist(entityClass, propertyName, value);
    }
}