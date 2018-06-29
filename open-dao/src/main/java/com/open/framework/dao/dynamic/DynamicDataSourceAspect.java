package com.open.framework.dao.dynamic;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 基于aspectj的多数据源aop</br>
 *
 */
@Aspect
@Order(1)
@Component
public class DynamicDataSourceAspect {

	/**
	 * The constant logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

	/**
	 * Change data source.
	 *
	 * @param point
	 * @param changeDs
	 * @throws Throwable
	 */
	@Before("@annotation(changeDs)")
	public void changeDataSource(JoinPoint point, ChangeDs changeDs) throws Throwable {
		String key = changeDs.key();
		if (!DynamicDataSourceContextHolder.containsDataSource(key)) {
			//logger.error("当前会话切换数据源[{}]不存在，使用默认数据源 > {}", key, point.getSignature());
		} else {
			//logger.debug("当前会话切换数据源为: {}", key);
			DynamicDataSourceContextHolder.setDataSource(key);
		}
	}

	/**
	 * Restore data source.
	 *
	 * @param point
	 * @param changeDs
	 */
	@After("@annotation(changeDs)")
	public void restoreDataSource(JoinPoint point, ChangeDs changeDs) {
		//logger.debug("当前会话结束，重置数据源为: {} ", changeDs.key());
		DynamicDataSourceContextHolder.clearDataSource();
	}

}
