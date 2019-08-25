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
import com.open.framework.commmon.utils.ClassUtil;
import com.open.framework.commmon.utils.JsonUtil;
import com.open.framework.commmon.utils.StringUtil;
import com.open.framework.commmon.utils.YamlUtils;
import com.open.framework.commmon.web.PageBean;
import com.open.framework.commmon.web.QueryParam;
import com.open.framework.commmon.web.SpecOper;
import com.open.framework.dao.model.BaseEntity;
import com.open.framework.dao.other.PageUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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
	@Transactional
	public void updateBySql(String sql,Object...args) {
    	Query query = em.createNativeQuery(sql);
    	int i = 0;
    	for(Object arg:args) {
    		query.setParameter(i++,arg);
    	}
    	query.executeUpdate();
	}

	@Override
	@Transactional
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
		String useLogicDelete=YamlUtils.DefaultGetYamlValue(BaseConstant.LOGIC_DELETE_YAML);
		if(StringUtil.isEmpty(useLogicDelete) || BaseConstant.USE_LOGIC_DELETE.equals(useLogicDelete)){
			logicDelete(entity);
		}else{
			super.delete(entity);
		}
	}

	@Override
	@Transactional
	public void logicDelete(ID id) {
		Optional<T> optionalT= super.findById(id);//有问题,没法保存
		if(optionalT.isPresent()){
			T entity=optionalT.get();
			if (!(entity instanceof BaseEntity)) {
				return;
			}
			BaseEntity model = (BaseEntity) entity;
			model.setDelState(BaseConstant.DELETE);
			this.em.merge(model);
		}
	}


	@Override
	@Transactional
	public void logicDelete(T entity) {
		BaseEntity model = (BaseEntity) entity;
		logicDelete((ID) model.getGid());
		/*if (StringUtil.isBlank(model.getGid())) {
			em.persist(model);
		} else {
			em.merge(model);
		}*/

	}

	@Override
	@Transactional
	public void logicDelete(Iterable<? extends T> entities) {
		if (null == entities) {
			return;
		}
		for (T entity : entities) {
			logicDelete(entity);
		}
	}
	/**
	 * @description: 复制自com.open.framework.core.dao.impl.BaseDao,为了不引入core包,所以要同步修改
	 * @author: hsj
	 * @date: 2019-08-25 18:20:34
	 */
	@Override
	public Object findAll(QueryParam queryParam) {
		if(null==queryParam){
			queryParam=new QueryParam();
		}
		Criteria criteria = em.unwrap(Session.class).createCriteria(this.getDomainClass());;
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
						criteria.addOrder(org.hibernate.criterion.Order.desc(ff[0]));
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
				if (queryParam.isShowCount()) {
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
	public static Criteria getWhere(Criteria criteria, List<Object> specOpers) {
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

	public static Criterion getCriterion(SpecOper specOper) {
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
