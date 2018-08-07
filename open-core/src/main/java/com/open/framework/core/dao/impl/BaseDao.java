package com.open.framework.core.dao.impl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;

import com.open.framework.commmon.BaseConstant;
import com.open.framework.commmon.utils.BeanUtil;
import com.open.framework.commmon.utils.JsonUtil;
import com.open.framework.commmon.utils.SpringUtil;
import com.open.framework.commmon.utils.StringUtil;
import com.open.framework.commmon.web.PageBean;
import com.open.framework.commmon.web.QueryParam;
import com.open.framework.commmon.web.SpecOper;
import com.open.framework.core.dao.IBaseDao;
import com.open.framework.core.service.impl.BaseServiceImpl;
import com.open.framework.dao.model.BaseEntity;
import com.open.framework.dao.other.PageUtil;
import com.open.framework.dao.other.SortUtil;
import org.hibernate.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


/**
 * @param <T> 实体类
 * @author Dio 此类实现了通用的实体类的数据库操作，操作均需要实体类作为参数
 */
@Component
public class BaseDao implements IBaseDao {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    public static Map<String, Class> entityMap = new HashMap();
    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public BaseDao() {
    }

    @Override
    public <U> U get(Class<U> entityClass, Serializable id) {
        return (U) this.getCurrentSession().get(entityClass, id);
    }

    @Override
    public <E> List<E> getAll(Class<E> entityClass, Collection<String> ids) {
        //TODO 目前发现Orcale查询in语句，最多只能包含1000个元素，所以暂且进行分批查询，后面有更好解决方案再做调整。
        int allCount = ids.size();
        int maxCount = 1000;//每批最大数
        if (allCount > maxCount) {
            int pageCount = allCount / maxCount;//分批数
            int modCount = allCount % maxCount;//余数
            if (modCount > 0) {
                //如果存在余数则分批数+1
                pageCount = pageCount + 1;
            }
            List<String> allIds = new ArrayList<String>();
            allIds.addAll(ids);
            List<E> allList = new ArrayList<E>();
            int fromIdx = 0;
            int toIdx = 0;
            for (int i = 0; i < pageCount; i++) {
                List<String> pageIds;
                fromIdx = i * maxCount;
                toIdx = fromIdx + maxCount;
                if (i + 1 == pageCount) {
                    toIdx = fromIdx + modCount;
                }
                pageIds = allIds.subList(fromIdx, toIdx);//FIXME BUG 每次取少一个的问题
                List<E> pageList = (List<E>) this.find(entityClass, Restrictions.in(getIdName(entityClass), pageIds));
                if (pageList != null && pageList.size() > 0) {
                    allList.addAll(pageList);
                }
            }
            return allList;
        } else {
            return (List<E>) this.find(entityClass, Restrictions.in(getIdName(entityClass), ids));
        }
    }

    @Override
    public <E> List<E> getAll(Class<E> entityClass, String[] ids) {
        return this.getAll(entityClass, this.arrayToList(ids));
    }

    @Override
    public <E> List<E> getAll(Class<E> entityClass) {
        return (List<E>) this.find(entityClass);
    }

    @Override
    public <E> List<E> getAll(Class<E> entityClass, String orderByProperty, boolean isAsc) {
        return this.getAll(entityClass, new String[]{orderByProperty}, new Boolean[]{isAsc});
    }

    @Override
    public <E> List<E> getAll(Class<E> entityClass, String[] orderByProperty, Boolean[] isAsc) {
        Assert.noNullElements(orderByProperty);
        Assert.noNullElements(isAsc);
        Criteria c = this.createCriteria(entityClass);
        if (orderByProperty.length == isAsc.length) {
            for (int i = 0; i < orderByProperty.length; i++) {
                if (isAsc[i]) {
                    c.addOrder(Order.asc(orderByProperty[i]));
                } else {
                    c.addOrder(Order.desc(orderByProperty[i]));
                }
            }
        } else {
            logger.error("参数属性名数组长度与对应值的数组长度不一致");
        }
        return c.list();
    }

    @Override
    public Object saveOrUpdate(Object entity) {
        if (entity instanceof BaseEntity) {
            if (StringUtil.isNotEmpty(((BaseEntity) entity).getGid())) {
                this.getCurrentSession().merge(entity);
            } else {
                this.getCurrentSession().persist(entity);
            }
        }
        return entity;
    }

    @Override

    public void saveAll(Collection<?> entities) {
        for (Object entity : entities) {
            entityManager.persist(entity);
        }
    }


    @Override
    public void remove(Object entity) {
        if (entity instanceof BaseEntity) {
            ((BaseEntity) entity).setDelState(true);
            entityManager.persist(entity);
        }
    }

    @Override
    public void remove(Collection<?> entities) {
        for (Object entity : entities) {
            remove(entity);
        }

    }

    @Override
    public void remove(Class<?> entityClass, String propertyName, Object value) {
        this.remove(this.find(entityClass, propertyName, value));
    }

    @Override
    public void remove(Class<?> entityClass, String[] propertyName, Object[] value) {
        this.remove(this.find(entityClass, propertyName, value));
    }

    @Override
    public void removeAllIdHql(Class<?> entityClass, Collection<String> ids) {
        String hql = "update " + entityClass.getName() + " set modifyDate= :modifyDate,modifyId= :modifyId,delState= " +
                ":delState where gid  in  (:gid)";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter("modifyDate", new Date());
        query.setParameter("modifyId", "admin");
        query.setParameter("delState", true);
        query.setParameterList("gid", ids);
        query.executeUpdate();
    }

    @Override

    public void removeById(Class<?> entityClass, Serializable id) {
        this.remove(this.get(entityClass, id));
    }

    @Override

    public void removeAll(Class<?> entityClass, Collection<String> ids) {
        this.remove(this.getAll(entityClass, ids));
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List findHql(String hql, Object... values) {
        Assert.hasText(hql);
        return this.createQuery(hql, values).list();
    }

    @Override
    public List<?> findPage(String hql, int pageIndex, int pageSize, Object... values) {
        return this.findPage(this.createQuery(hql, values), pageIndex, pageSize);
    }

    @Override
    public List<?> find(String hql, Map<String, ?> values) {
        return this.createQuery(hql, values).list();
    }

    @Override
    public List<?> findPage(String hql, int pageIndex, int pageSize, Map<String, ?> values) {
        return this.findPage(this.createQuery(hql, values), pageIndex, pageSize);
    }

    @Override
    public List<?> findPage(Query query, int pageIndex, int pageSize) {
        Assert.notNull(query, "Query对象不能为null");
        return query.setFirstResult((int) (pageIndex - 1) * pageSize).setMaxResults(pageSize).list();
    }

    @Override
    @Transactional(readOnly = true)
    public List<?> find(Class<?> entityClass, Criterion... criterions) {
        return this.createCriteria(entityClass, criterions).list();
    }

    @Override
    public List<?> find(Class<?> entityClass, String propertyName) {
        List<Map<String, ?>> list = this.find(entityClass, new String[]{propertyName});
        List<Object> dataList = new ArrayList<Object>();
        for (Map<String, ?> m : list) {
            dataList.add(m.get(propertyName));
        }
        return dataList;
    }


    @Override
    public List<Map<String, ?>> find(Class<?> entityClass, String[] propertyName) {
        Assert.noNullElements(propertyName);
        StringBuffer sb = new StringBuffer();
        for (String pp : propertyName) {
            sb.append(pp + " as " + pp + ",");
        }
        if (sb.toString().endsWith(",")) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return this.createQuery("select new map(" + sb.toString() + ") from " + entityClass.getName()).list();
    }

    @Override
    public <E> List<E> find(Class<E> entityClass, String propertyName, Object value) {
        return this.find(entityClass, new String[]{propertyName}, new Object[]{value});
    }

    @Override
    public <E> List<E> find(Class<E> entityClass, String[] propertyName, Object[] value) {
        return this.createCriteriaBy(entityClass, propertyName, value).list();
    }

    @Override
    public Long findCount(Class<?> entityClass, String propertyName, Object value) {
        return this.findCount(entityClass, new String[]{propertyName}, new Object[]{value});
    }

    @Override
    public Long findCount(Class<?> entityClass, String[] propertyName, Object[] value) {
        return (Long) this.createCriteriaBy(entityClass, propertyName, value).setProjection(Projections.rowCount())
                .uniqueResult();
    }

    @Override
    public List<Object> findOrderBy(Class<?> entityClass, String propertyName, String orderBy, boolean isAsc) {
        List<Map<String, ?>> list = this.findOrderBy(entityClass, new String[]{propertyName}, new String[]{orderBy},
                new Boolean[]{isAsc});
        //只取当前数据列数据
        List<Object> colList = new ArrayList<Object>();
        for (Map<String, ?> m : list) {
            colList.add(m.get(propertyName));
        }
        return colList;
    }

    @Override
    public List<Map<String, ?>> findOrderBy(Class<?> entityClass, String[] propertyName, String[] orderBy, Boolean[]
            isAsc) {
        Assert.noNullElements(propertyName);
        Assert.noNullElements(orderBy);
        Assert.noNullElements(isAsc);
        StringBuffer sb = new StringBuffer("select ");
        for (String pp : propertyName) {
            sb.append(pp + ",");
        }
        if (sb.toString().endsWith(",")) {
            sb.delete(sb.length() - 1, sb.length());
        }
        sb.append(" from ");
        sb.append(entityClass.getName());
        if (orderBy.length == isAsc.length) {
            sb.append(" order by ");
            for (int i = 0; i < orderBy.length; i++) {
                sb.append(" " + orderBy[i] + " ");
                if (isAsc[i]) {
                    sb.append("asc");
                } else {
                    sb.append("desc");
                }
                sb.append(",");
            }
            if (sb.toString().endsWith(",")) {
                sb.delete(sb.length() - 1, sb.length());
            }
        } else {
            logger.error("参数排序字段数组长度与对应排序方式的数组长度不一致");
        }
        return this.createQuery(sb.toString()).list();

    }

    public List<Object> findOrderBy(Class<?> entityClass, String propertyName, boolean isAsc) {
        List<Map<String, ?>> list = this.findOrderBy(entityClass, new String[]{propertyName}, new Boolean[]{isAsc});
        //只取当前数据列数据
        List<Object> colList = new ArrayList<Object>();
        for (Map<String, ?> m : list) {
            colList.add(m.get(propertyName));
        }
        return colList;
    }

    @Override
    public List<Map<String, ?>> findOrderBy(Class<?> entityClass, String[] propertyName, Boolean[] isAsc) {
        return this.findOrderBy(entityClass, propertyName, propertyName, isAsc);
    }

    @Override
    public <E> List<E> findOrderBy(Class<E> entityClass, String propertyName, Object value, String orderBy, boolean
            isAsc) {
        return this.findOrderBy(entityClass, new String[]{propertyName}, new Object[]{value}, new String[]{orderBy},
                new Boolean[]{isAsc});
    }

    @Override
    public <E> List<E> findOrderBy(Class<E> entityClass, String[] propertyName, Object[] value, String[] orderBy,
                                   Boolean[] isAsc) {
        return this.createCriteriaBy(entityClass, propertyName, value, orderBy, isAsc).list();
    }

    @Override
    public <E> E findUnique(Class<E> entityClass, String propertyName, Object value) {
        return this.findUnique(entityClass, new String[]{propertyName}, new Object[]{value});
    }

    @Override
    public <E> E findUnique(Class<E> entityClass, String[] propertyName, Object[] value) {
        return (E) this.createCriteriaBy(entityClass, propertyName, value).uniqueResult();
    }

    @Override
    public Object findUniqueHql(String hql, Object... values) {
        return this.createQuery(hql, values).uniqueResult();
    }

    @Override
    public Object findUnique(String hql, Map<String, ?> values) {
        return this.createQuery(hql, values).uniqueResult();
    }

    @Override
    public boolean isUnique(Class<?> entityClass, String propertyName, Object value) {
        return this.isUnique(entityClass, new String[]{propertyName}, new Object[]{value});
    }

    @Override
    public boolean isUnique(Class<?> entityClass, String[] propertyName, Object[] value) {
        Criteria criteria = this.createCriteriaBy(entityClass, propertyName, value).setProjection(Projections
                .rowCount());
        Long count = Long.valueOf(criteria.uniqueResult().toString());
        return count == 1;
    }

    @Override
    public boolean isExist(Class<?> entityClass, String propertyName, Object value) {
        return this.isExist(entityClass, new String[]{propertyName}, new Object[]{value});
    }

    @Override
    public boolean isExist(Class<?> entityClass, String[] propertyName, Object[] value) {
        Criteria criteria = this.createCriteriaBy(entityClass, propertyName, value).setProjection(Projections
                .rowCount());
        Long count = Long.valueOf(criteria.uniqueResult().toString());
        return count > 0;
    }

    @Override
    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    /**
     * getSession:(获得Session). <br/>
     */
    public Session getSession() {
        return this.getCurrentSession();
    }

    @Override
    public void flush() {
        entityManager.flush();
    }

    @Override
    public void clear() {
        entityManager.clear();
    }


    @Override
    public Class getEntityByDto(String dtoName) throws ClassNotFoundException {
        SessionFactory sessionFactory = this.getCurrentSession().getSessionFactory();
        String entityName = dtoName.replace("DTO", "");
        Class entity = entityMap.get(entityName);
        if (null != entity) {
            return entity;
        } else {
            Set<EntityType<?>> entities = sessionFactory.getMetamodel().getEntities();
            for (EntityType entityType : entities) {
                if (entityType.getName().equals(entityName)) {
                    entityMap.put(entityName, entityType.getJavaType());
                    return entityMap.get(entityName);
                }
            }
        }
        return null;
    }

    @Override
    public String getIdName(Class<?> entityClass) {
        Assert.notNull(entityClass);
        ClassMetadata meta = this.getCurrentSession().getSessionFactory().getClassMetadata(entityClass);

        Assert.notNull(meta, "Class " + entityClass + " not define in hibernate session factory.");
        String idName = meta.getIdentifierPropertyName();
        Assert.hasText(idName, entityClass.getSimpleName() + " has no identifier property define.");
        return idName;
    }

    @Override
    public Query createQuery(String queryString, Object... values) {
        Assert.notNull(queryString, "queryString不能为null");
        Assert.hasText(queryString, "queryString不能为空字符串");
        Query query = this.getCurrentSession().createQuery(queryString);

        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query;
    }

    @Override
    public Query createQuery(String queryString, Map<String, ?> values) {
        Assert.notNull(queryString, "queryString不能为null");
        Assert.hasText(queryString, "queryString不能为空");
        Query query = getCurrentSession().createQuery(queryString);
        if (values != null) {
            query.setProperties(values);
        }
        return query;
    }

    @Override
    public int batchExecute(String hql, Object... values) {
        return this.createQuery(hql, values).executeUpdate();
    }

    @Override
    public int batchExecute(String hql, Map<String, ?> values) {
        return this.createQuery(hql, values).executeUpdate();
    }

    @Override
    public Criteria createCriteria(Class<?> entityClass, Criterion... criterions) {
        Criteria criteria = this.getCurrentSession().createCriteria(entityClass);
        for (Criterion c : criterions) {
            criteria.add(c);
        }
        return criteria;
    }

    @Override
    public Criteria createCriteriaBy(Class<?> entityClass, String propertyName, Object value) {
        return this.createCriteriaBy(entityClass, new String[]{propertyName}, new Object[]{value});
    }

    @Override
    public Criteria createCriteriaBy(Class<?> entityClass, String[] propertyName, Object[] value) {
        Assert.notEmpty(propertyName);
        Assert.notEmpty(value);
        Criteria criteria = this.createCriteria(entityClass);
        if (propertyName.length == value.length) {
            for (int i = 0; i < propertyName.length; i++) {
                criteria.add(Restrictions.eq(propertyName[i], (i < value.length) ? value[i] : null));
            }
        } else {
            logger.error("参数属性名数组长度与对应值的数组长度不一致");
        }
        return criteria;
    }

    @Override
    public Criteria createCriteriaBy(Class<?> entityClass, String propertyName, Object value, String orderBy, boolean
            isAsc) {
        return this.createCriteriaBy(entityClass, new String[]{propertyName}, new Object[]{value}, new
                String[]{orderBy}, new Boolean[]{isAsc});
    }

    @Override
    public Criteria createCriteriaBy(Class<?> entityClass, String[] propertyName, Object[] value, String[] orderBy,
                                     Boolean[] isAsc) {
        Assert.notEmpty(propertyName);
        Assert.notEmpty(value);
        Assert.notEmpty(orderBy);
        Assert.notEmpty(isAsc);
        Criteria criteria = this.createCriteria(entityClass);
        if (propertyName.length == value.length) {
            for (int i = 0; i < propertyName.length; i++) {
                criteria.add(Restrictions.eq(propertyName[i], (i < value.length) ? value[i] : null));
            }
        } else {
            logger.error("参数属性名数组长度与对应值的数组长度不一致");
        }
        if (orderBy.length == isAsc.length) {
            for (int i = 0; i < orderBy.length; i++) {
                if ((i < isAsc.length) ? isAsc[i] : true) {
                    criteria.addOrder(Order.asc(orderBy[i]));
                } else {
                    criteria.addOrder(Order.desc(orderBy[i]));
                }
            }
        } else {
            logger.error("参数排序字段数组长度与对应排序方式的数组长度不一致");
        }
        return criteria;
    }

    /**
     * 数组转换为列表
     *
     * @param array
     * @return
     */
    protected List arrayToList(Object[] array) {
        Assert.noNullElements(array);
        List list = new ArrayList();
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
        return list;
    }

    @Override
    public Object findAll(Class entityClass, QueryParam queryParam) {
        if(null==queryParam){
            queryParam=new QueryParam();
        }
        Criteria criteria = this.createCriteria(entityClass);
        List<Object> specOpers = queryParam.getSpecOpers();
        String orderBy = queryParam.getOrderBy();
        PageBean pageBean = queryParam.getPageBean();
        if (null != specOpers && specOpers.size() > 0) {
            criteria = getWhere(criteria, specOpers);
        }
        if (StringUtil.isNotEmpty(orderBy)) {
            String[] orders = orderBy.split(",");
            for (int i = 0; i < orders.length; i++) {
                String[] ff = orders[i].split("_");
                if (ff.length >= 2) {
                    if (ff[1].equals("d")) {
                        criteria.addOrder(Order.desc(ff[0]));
                    } else {
                        criteria.addOrder(Order.asc(ff[0]));
                    }
                }
            }
        }

        if (null != pageBean) {
            pageBean = PageUtil.getPagebean(pageBean);
            Integer pageSize = pageBean.getPageSize();
            Integer page = pageBean.getPage();
            if (pageSize > 0) {
                PageBean newPageBean = new PageBean();
                newPageBean.setPageSize(pageSize);
                newPageBean.setPage(page);
                //是否显示总数
                if (queryParam.getShowCount()) {
                    //显示总数以后,需要置空,否则下面.list()的时候会得到的是count()的数据
                    newPageBean.setCount((Long) criteria.setProjection(Projections.rowCount())
                            .uniqueResult());
                    criteria.setProjection(null);
                }
                List list = criteria.setFirstResult((int) (page - 1) * pageSize).setMaxResults(pageSize).list();
                newPageBean.setRows(list);
                return newPageBean;//返回分页对象
            }
        }
        return criteria.list();//返回没有分页对象
    }

    public Criteria getWhere(Criteria criteria, List<Object> specOpers) {
        List<Criterion> list = null;
        String tempJoin = null;
        //拿到条件的数组,条件有一个或者集合,所以只能是object,然后自己判断类型转换
        for (Object specOperObj : specOpers) {
            //如果是map说明是一个,是一个时候直接and,因为如果是有组合需要是list,or不能单个,需要有组合才行
            if (specOperObj instanceof Map) {
                SpecOper specOper = (SpecOper) JsonUtil.mapToBean((Map) specOperObj, SpecOper.class);
                Criterion temp = getCriterion(specOper);
                criteria.add(temp);
            } else if (specOperObj instanceof List) {
                //是数组,然后循环数组,把map转换成SpecOper对象
                List<SpecOper> specOperList=(List) specOperObj;
                if (specOperList.size() > 0) {
                    //new一个条件集合,用于组合
                    Criterion[] criterions = new Criterion[specOperList.size()];
                    //默认是and
                    String join=BaseConstant.DAO_AND;
                    for (int i = 0; i < specOperList.size(); i++) {
                        Map map = (Map) specOperList.get(i);
                        SpecOper specOper = (SpecOper) JsonUtil.mapToBean(map, SpecOper.class);
                        Criterion temp = getCriterion(specOper);
                        criterions[i] = temp;
                        //根据第一个的连接方式来判断租户里面是or还是and
                        if(i==0){
                            join=specOper.getJoin();
                        }
                    }
                    if(BaseConstant.DAO_OR.equalsIgnoreCase(join)){
                        criteria.add(Restrictions.or(criterions));
                    }else{
                        criteria.add(Restrictions.and(criterions));
                    }
                }
            }
        }
        return criteria;
    }

    public Criterion getCriterion(SpecOper specOper) {
        Criterion temp = null;
        String oper = specOper.getOper();
        String key = specOper.getKey();
        Object value = specOper.getValue();
        if (BaseConstant.EQUAL.equalsIgnoreCase(oper)) {
            temp = Restrictions.eq(key, value);
        } else if (BaseConstant.GREATER_OR_EQUAL.equalsIgnoreCase(oper)) {
            temp = Restrictions.isNotEmpty(key);
        } else if (BaseConstant.LESS_OR_EQUAL.equalsIgnoreCase(oper)) {
            temp = Restrictions.le(key, value);
        } else if (BaseConstant.GREATER.equalsIgnoreCase(oper)) {
            temp = Restrictions.gt(key, value);
        } else if (BaseConstant.LESS.equalsIgnoreCase(oper)) {
            temp = Restrictions.lt(key, value);
        } else if (BaseConstant.LIKE.equalsIgnoreCase(oper)) {
            temp = Restrictions.like(key, "%" + value + "%");
        } else if (BaseConstant.LIKEA.equalsIgnoreCase(oper)) {
            temp = Restrictions.like(key, value + "%");
        } else if (BaseConstant.LIKEB.equalsIgnoreCase(oper)) {
            temp = Restrictions.like(key, "%" + value);
        } else if (BaseConstant.IS_NULL.equalsIgnoreCase(oper)) {
            temp = Restrictions.isNull(key);
        } else if (BaseConstant.IS_NOT_NULL.equalsIgnoreCase(oper)) {
            temp = Restrictions.isNotNull(key);
        } else if (BaseConstant.NOT_EQUAL.equalsIgnoreCase(oper)) {
            temp = Restrictions.ne(key, value);
        } else if (BaseConstant.NOT_LIKE.equalsIgnoreCase(oper)) {
            temp = Restrictions.sqlRestriction(" " + StringUtil.camelToUnderline(key) + " not like '%"
                    + value + "%'");
        } else if (BaseConstant.IS_EMPTY.equalsIgnoreCase(oper)) {
            temp = Restrictions.isEmpty(key);
        } else if (BaseConstant.IS_NOT_EMPTY.equalsIgnoreCase(oper)) {
            temp = Restrictions.isNotEmpty(key);
        } else if (BaseConstant.IS_IN.equalsIgnoreCase(oper)) {
            String[] values = value.toString().split(",");
            temp = Restrictions.in(key, values);
        } else if (BaseConstant.IS_NOT_IN.equalsIgnoreCase(oper)) {
            String[] values = value.toString().split(",");
            temp = Restrictions.not(Restrictions.in(key, values));
        }
        return temp;
    }
}
