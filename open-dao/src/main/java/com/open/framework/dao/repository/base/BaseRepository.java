package com.open.framework.dao.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface BaseRepository<T,ID extends Serializable> extends JpaRepository<T,ID> {

    public List<Object[]> listBySQL(String sql);
    @Transactional
    public void logicDelete(ID id);
    @Transactional
    public void logicDelete(T entity);
    @Transactional
    public void logicDelete(Iterable<? extends T> entities);
    @Transactional
    public void updateBySql(String sql, Object... args);

    @Transactional
    public void updateByHql(String hql, Object... args);
}
