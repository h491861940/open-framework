package com.open.framework.core.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.open.framework.commmon.web.QueryParam;
import com.open.framework.dao.repository.base.BaseRepository;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * @author Dio 此接口定义了通用的实体类的数据库操作 通用的操作方法均包含Class<T> entityClass实体类参数.
 *
 */
public interface IBaseDao
{
	/**
	 * 根据dto的名字获取实体对象,直接去掉DTO查找
	 * @param dtoName
	 * @return
	 * @throws ClassNotFoundException
	 */
    public Class getEntityByDto(String dtoName) throws ClassNotFoundException;
	/**
	 * 获取Entity对象的主键属性名.
	 */
	public String getIdName(Class<?> entityClass);

	/**
	 * 根据ID获取对象.
	 * 
	 * @param entityClass
	 * @param id
	 * @return
	 */
	public <E>E get(Class<E> entityClass, Serializable id);
	/**
	 * 根据ID列表获取对象列表.
	 * 
	 * @param entityClass
	 * @param ids
	 *            ID列表
	 * @return
	 */
	public <E>List<E> getAll(Class<E> entityClass, Collection<String> ids);
	
	/**
	 * 根据ID数组获取对象列表.
	 * 
	 * @param entityClass
	 * @param ids
	 *            ID数组
	 * @return
	 */
	public <E>List<E> getAll(Class<E> entityClass, String[] ids);

	/**
	 * 获取全部对象
	 * 
	 * @param entityClass
	 * @return
	 */
	public <E>List<E> getAll(Class<E> entityClass);

	/**
	 * 获取全部对象,并按单个属性列排序.
	 * 
	 * @param entityClass
	 * @param orderByProperty
	 *            排序属性
	 * @param isAsc
	 *            排序方式
	 * @return
	 */
	public <E>List<E> getAll(Class<E> entityClass, String orderByProperty, boolean isAsc);

	/**
	 * 获取全部对象,并按多个属性列排序.
	 * 
	 * @param entityClass
	 * @param orderByProperty
	 *            排序属性
	 * @param isAsc
	 *            排序方式
	 * @return
	 */
	public <E>List<E> getAll(Class<E> entityClass, String[] orderByProperty, Boolean[] isAsc);

	/**
	 * 保存单个对象.
	 */
	public Object saveOrUpdate(Object entity);

	/**
	 * 保存多个对象(列表).
	 */
	public void saveAll(Collection<?> entities);


	/**
	 * 删除单个对象.
	 */
	public void remove(Object entity);

	/**
	 * 删除多个对象(列表).
	 */
	public void remove(Collection<?> entities);

	/**
	 * 根据ID删除单个对象.
	 */
	public void removeById(Class<?> entityClass, Serializable id);

	/**
	 * 根据ID列表删除多个对象.
	 */
	public void removeAll(Class<?> entityClass, Collection<String> ids);


	/**
	 * 依据单个字段条件批量删除
	 *
	 * @param entity
	 *            实体对象
	 * @param propertyName
	 *            属性名（字段名）
	 * @param value
	 *            值
	 */
	public void remove(Class<?> entityClass, String propertyName, Object value);

	/**
	 * 依据多个字段条件批量删除
	 *
	 * @param entity
	 *            实体对象
	 * @param propertyName
	 *            多属性名（多字段名）
	 * @param value
	 *            对应的值
	 */
	public void remove(Class<?> entityClass, String[] propertyName, Object[] value);

	public void removeAllIdHql(Class<?> entityClass, Collection<String> ids);
	/**
	 * 根据hql语句查询对象列表.
	 * 
	 * @param hql
	 *            hql语句，参数占位符为"?".
	 * @param values
	 *            可变参数，依据顺序替换占位符的"?"
	 */
	@SuppressWarnings("rawtypes")
	public List findHql(String hql, Object... values);

	/**
	 * 根据hql语句查询对象分页列表.
	 * 
	 * @param hql
	 *            hql语句，参数占位符为"?".
	 * @param pageIndex
	 *            页码
	 * @param pageSize
	 *            每页记录数
	 * @param values
	 *            可变参数，依据顺序替换占位符的"?"
	 */
	public List<?> findPage(String hql, int pageIndex, int pageSize, Object... values);

	/**
	 * 依据hql语句查询对象列表.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	public List<?> find(String hql, Map<String, ?> values);

	/**
	 * 依据hql语句查询对象分页列表.
	 * 
	 * @param hql
	 *            hql语句
	 * @param pageIndex
	 *            页码
	 * @param pageSize
	 *            每页记录数
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	public List<?> findPage(String hql, int pageIndex, int pageSize, Map<String, ?> values);

	/**
	 * 依据query对象查询分页列表.
	 * 
	 * @param query
	 *            query对象
	 * @param pageIndex
	 *            页码
	 * @param pageSize
	 *            每页记录数
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	public List<?> findPage(Query query, int pageIndex, int pageSize);

	/**
	 * 依据Criteria查询对象列表.
	 * 
	 * @param criterions
	 *            数量可变的Criterion.
	 */
	public List<?> find(Class<?> entityClass, Criterion... criterions);

	/**
	 * 查询指定的单个属性列
	 * 
	 * @param entityClass
	 *            实体类
	 * @param propertyName
	 *            属性名(字段名)
	 * @return
	 */
	public List<?> find(Class<?> entityClass, String propertyName);

	/**
	 * 获取多个属性列
	 * 
	 * @param entityClass
	 *            实体类
	 * @param propertyName
	 *            多个属性名(字段名)
	 * @return Map<属性名,属性值>
	 */
	public List<Map<String, ?>> find(Class<?> entityClass, String[] propertyName);

	/**
	 * 根据属性名和属性值查询对象
	 * 
	 * @param entityClass
	 *            实体类
	 * @param propertyName
	 *            属性名(字段名)
	 * @param value
	 *            值
	 * @return
	 */
	public <E>List<E> find(Class<E> entityClass, String propertyName, Object value);

	/**
	 * 根据属性名和属性值查询记录数
	 * 
	 * @param entityClass
	 *            实体类
	 * @param propertyName
	 *            属性名(字段名)
	 * @param value
	 *            值
	 * @return
	 */
	public Long findCount(Class<?> entityClass, String propertyName, Object value);

	/**
	 * 根据多个属性名对应的属性值查询对象.
	 * 
	 * @param entityClass
	 *            实体类
	 * @param propertyName
	 *            多属性名(字段名)
	 * @param value
	 *            对应的值
	 * @return
	 */
	public <E>List<E> find(Class<E> entityClass, String[] propertyName, Object[] value);

	/**
	 * 根据多个属性名对应的属性值查询记录数.
	 * 
	 * @param entityClass
	 *            实体类
	 * @param propertyName
	 *            多属性名(字段名)
	 * @param value
	 *            对应的值
	 * @return
	 */
	public Long findCount(Class<?> entityClass, String[] propertyName, Object[] value);

	/**
	 * 查询指定的多个属性列,并按指定属性列排序
	 * 
	 * @param entityClass
	 *            实体类
	 * @param propertyName
	 *            多个属性名(字段名)
	 * @param orderBy
	 *            多个排序字段
	 * @param isAsc
	 *            对应的排序方式true为升序(asc),false为降序(desc)
	 * @return
	 */
	public List<Map<String,?>> findOrderBy(Class<?> entityClass, String[] propertyName, String[] orderBy, Boolean[]
            isAsc);

	/**
	 * 查询指定的单个属性列,并排序
	 * 
	 * @param entityClass
	 *            实体类
	 * @param propertyName
	 *            属性名(字段名)
	 * @param isAsc
	 *            排序方式
	 * @return
	 */
	public List<Object> findOrderBy(Class<?> entityClass, String propertyName, boolean isAsc);

	/**
	 * 查询指定的多个属性列,并排序
	 * 
	 * @param entityClass
	 *            实体类
	 * @param propertyName
	 *            属性名(字段名)
	 * @param isAsc
	 *            排序方式
	 * @return
	 */
	public List<Map<String,?>> findOrderBy(Class<?> entityClass, String[] propertyName, Boolean[] isAsc);

	/**
	 * 查询指定的单个属性列,并按指定属性列排序
	 * 
	 * @param entityClass
	 *            实体类
	 * @param propertyName
	 *            属性名(字段名)
	 * @param orderBy
	 *            排序字段
	 * @param isAsc
	 *            排序方式
	 * @return
	 */
	public List<Object> findOrderBy(Class<?> entityClass, String propertyName, String orderBy, boolean isAsc);

	/**
	 * 根据属性名和属性值查询对象,带排序参数.
	 * 
	 * @param entityClass
	 *            实体类
	 * @param propertyName
	 *            属性名(字段名)
	 * @param value
	 *            值
	 * @param orderBy
	 *            排序字段
	 * @param isAsc
	 *            true为升序(asc),false为降序(desc)
	 * @return
	 */
	public <E>List<E> findOrderBy(Class<E> entityClass, String propertyName, Object value, String orderBy, boolean isAsc);

	/**
	 * 根据属性名和属性值查询对象,带排序参数.
	 * 
	 * @param entityClass
	 *            实体类
	 * @param propertyName
	 *            多属性名(字段名)
	 * @param value
	 *            对应的值
	 * @param orderBy
	 *            多字段排序
	 * @param isAsc
	 *            对应的排序方式，true为升序(asc),false为降序(desc)
	 * @return
	 */
	public <E>List<E> findOrderBy(Class<E> entityClass, String[] propertyName, Object[] value, String[] orderBy, Boolean[] isAsc);

	/**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	public Object findUniqueHql(String hql, Object... values);

	/**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	public Object findUnique(String hql, Map<String, ?> values);

	/**
	 * 根据单个属性名和属性值查询唯一对象.
	 * 
	 * @param entityClass
	 *            实体类
	 * @param propertyName
	 *            属性名(字段名)
	 * @param value
	 *            对应的值
	 * @return 符合条的唯一对象 or null
	 */
	public <E>E findUnique(Class<E> entityClass, String propertyName, Object value);

	/**
	 * 根据多个属性名和属性值查询唯一对象.
	 * 
	 * @param entityClass
	 *            实体类
	 * @param propertyName
	 *            多属性名(字段名)
	 * @param value
	 *            对应的值
	 * @return 符合条的唯一对象 or null
	 */
	public <E>E findUnique(Class<E> entityClass, String[] propertyName, Object[] value);

	/**
	 * 执行HQL进行批量修改/删除操作.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 * @return 更新记录数.
	 */
	public int batchExecute(String hql, Object... values);

	/**
	 * 执行HQL进行批量修改/删除操作.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 * @return 更新记录数.
	 */
	public int batchExecute(String hql, Map<String, ?> values);

	/**
	 * 根据查询HQL与参数列表创建Query对象.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	public Query createQuery(String queryString, Object... values);

	/**
	 * 根据查询HQL与参数列表创建Query对象.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	public Query createQuery(String queryString, Map<String, ?> values);

	/**
	 * 依据单个属性的值查询，是否为唯一记录.
	 * 
	 * @param entityClass
	 *            实体类
	 * @param propertyName
	 *            属性名(字段名)
	 * @param value
	 *            对应的值
	 * @return
	 */
	public boolean isUnique(Class<?> entityClass, String propertyName, Object value);

	/**
	 * 依据多个属性的值查询，是否为唯一记录.
	 * 
	 * @param entityClass
	 *            实体类
	 * @param propertyName
	 *            多属性名(字段名)
	 * @param value
	 *            对应的值
	 * @return
	 */
	public boolean isUnique(Class<?> entityClass, String[] propertyName, Object[] value);

	/**
	 * 依据单个属性的值查询，是否存在记录.
	 * 
	 * @param entityClass
	 *            实体类
	 * @param propertyName
	 *            属性名(字段名)
	 * @param value
	 *            对应的值
	 * @return
	 */
	public boolean isExist(Class<?> entityClass, String propertyName, Object value);

	/**
	 * 依据多个属性的值查询，是否存在记录.
	 * 
	 * @param entityClass
	 *            实体类
	 * @param propertyName
	 *            属性名(字段名)
	 * @param value
	 *            对应的值
	 * @return
	 */
	public boolean isExist(Class<?> entityClass, String[] propertyName, Object[] value);

	/**
	 * 创建Criteria对象.
	 *
	 * @param entityClass实体类
	 * @param criterions可变的Restrictions条件列表
	 */
	public Criteria createCriteria(Class<?> entityClass, Criterion... criterions);

	/**
	 * 依据单个属性名和属性值，创建Criteria对象.
	 *
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public Criteria createCriteriaBy(Class<?> entityClass, String propertyName, Object value);

	/**
	 * 依据单个属性名和属性值，以及单个排序属性和排序方式，创建Criteria对象.
	 *
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public Criteria createCriteriaBy(Class<?> entityClass, String propertyName, Object value, String orderBy, boolean isAsc);

	/**
	 * 依据多个属性名和属性值，创建Criteria对象.
	 *
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public Criteria createCriteriaBy(Class<?> entityClass, String[] propertyName, Object[] value);

	/**
	 * 依据多个属性名和属性值，以及多个排序属性和排序方式，创建Criteria对象.
	 *
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public Criteria createCriteriaBy(Class<?> entityClass, String[] propertyName, Object[] value, String[] orderBy,
                                     Boolean[] isAsc);

	/**
	 * 得到当前Session对象
	 */
	public Session getCurrentSession();

	/**
	 * 调用HibernateTemplate的flush方法
	 */
	public void flush();

	/**
	 * 调用HibernateTemplate的clear方法
	 */
	public void clear();

	public Object findAll(Class entityClass,QueryParam queryParam);
}
