package com.open.framework.dao.repository.base;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;
import javax.persistence.criteria.*;
import static org.springframework.data.jpa.repository.query.QueryUtils.DELETE_ALL_QUERY_STRING;
import static org.springframework.data.jpa.repository.query.QueryUtils.applyAndBind;
import static org.springframework.data.jpa.repository.query.QueryUtils.getQueryString;
import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;
import com.open.framework.commmon.BaseConstant;
import com.open.framework.commmon.utils.StringUtil;
import com.open.framework.commmon.utils.YamlUtils;
import com.open.framework.dao.model.BaseEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.support.*;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T,ID>
        implements BaseRepository<T,ID> {

    private final EntityManager em;
    //父类没有不带参数的构造方法，这里手动构造父类
	public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
	    super(domainClass,em);
        this.em = em;
	}
    //通过em来完成查询
    @SuppressWarnings("unchecked")
	@Override
    public List<Object[]> listBySQL(String sql) {
        return em.createNativeQuery(sql).getResultList();
    }

	@Override
	public void updateBySql(String sql,Object...args) {
    	Query query = em.createNativeQuery(sql);
    	int i = 0;
    	for(Object arg:args) {
    		query.setParameter(i++,arg);
    	}
    	query.executeUpdate();
	}

	@Override
	public void updateByHql(String hql,Object...args) {
    	Query query = em.createQuery(hql);
    	int i = 0;
    	for(Object arg:args) {
    		query.setParameter(i++,arg);
    	}
    	query.executeUpdate();
	}

	@Override
	@Transactional
	public void delete(T entity) {
		//修改为逻辑删除
		if(BaseConstant.USE_LOGIC_DELETE.equals(YamlUtils.DefaultGetYamlValue(BaseConstant.LOGIC_DELETE_YAML))){
			logicDelete(entity);
		}else{
			super.delete(entity);
		}
	}

	@Override
	public void logicDelete(ID id) {
		T entity = super.getOne(id);
		if (null == entity || !(entity instanceof BaseEntity)) {
			return;
		}
		BaseEntity model = (BaseEntity) entity;
		model.setDelState(true);

		this.em.merge(model);
	}


	@Override
	public void logicDelete(T entity) {
		if (null == entity || !(entity instanceof BaseEntity)) {
			return;
		}

		BaseEntity model = (BaseEntity) entity;
		model.setDelState(true);

		if (StringUtil.isBlank(model.getGid())) {
			em.persist(model);
		} else {
			em.merge(model);
		}

	}

	@Override
	public void logicDelete(Iterable<? extends T> entities) {
		if (null == entities) {
			return;
		}

		for (T entity : entities) {
			logicDelete(entity);
		}
	}

}
