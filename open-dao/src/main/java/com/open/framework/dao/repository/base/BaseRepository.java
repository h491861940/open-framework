package com.open.framework.dao.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>,JpaSpecificationExecutor<T> {

    List<Object[]> listBySQL(String sql);

    void logicDelete(ID id);

    void logicDelete(T entity);

    void logicDelete(Iterable<? extends T> entities);

    void updateBySql(String sql, Object... args);

    void updateByHql(String hql, Object... args);
}
