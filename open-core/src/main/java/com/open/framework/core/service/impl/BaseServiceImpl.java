package com.open.framework.core.service.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import com.open.framework.commmon.utils.BeanUtil;
import com.open.framework.core.service.BaseService;
import com.open.framework.dao.repository.base.BaseRepository;
import com.sun.xml.internal.bind.v2.model.core.ID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;


public class BaseServiceImpl<T,S> implements BaseService<T, S> {

	@Autowired
	BaseRepository<T, Serializable> baseRepository;

	@Override
	public T save(T t) {
		return baseRepository.save(t);
	}

	@Override
	public T saveDto(S dto) {
		Class<T> entityClass = getEntityClass();
		try {
			T entity = entityClass.newInstance();
			BeanUtil.copyProperties(dto, entity);
			entity= save(entity);
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public S getDto(String id) {
		Class<S> dtoClass = getDtoClass();
		try {
			S dto = dtoClass.newInstance();
			T entity =  get(id);
			BeanUtil.copyProperties(entity,dto );
			return dto;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public T get(String id) {
		return baseRepository.getOne(id);
	}

	@Override
	public void delete(String id) {
		baseRepository.deleteById(id);
	}

	@Override
	public void delete(T t) {
		baseRepository.delete(t);
	}

	@Override
	public boolean existsById(String id) {
		return baseRepository.existsById(id);
	}
	@Override
	public boolean exists(Example<T> example) {
		return baseRepository.exists(example);
	}
	private Class getEntityClass() {
		return getGenericClass(0);
	}

	private Class getDtoClass() {
		return getGenericClass(1);
	}

	/**
	 * 根据i得到返修的类型,0是entity,1是DTO
	 *
	 * @param i
	 * @return
	 */
	private Class getGenericClass(int i) {
		if (i == 0 || i == 1) {
			Type type = this.getClass().getGenericSuperclass();//拿到带类型参数的泛型父类
			if (type instanceof ParameterizedType) {//这个Type对象根据泛型声明，就有可能是4中接口之一，如果它是BaseDao<User>这种形式
				ParameterizedType parameterizedType = (ParameterizedType) type;
				Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();//获取泛型的类型参数数组
				if (actualTypeArguments != null && actualTypeArguments.length == 2) {
					if (actualTypeArguments[i] instanceof Class) {//类型参数也有可能不是Class类型
						return (Class<T>) actualTypeArguments[i];
					}
				}
			}
		}
		return null;
	}
}