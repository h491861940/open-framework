package com.open.framework.dao.dynamic;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * 指定数据源注解</br>
 * 在使用数据源切换的时候,需要增加如下的配置,才能保证事物的一致,在切换数据源的方法里面抛出异常的时候,上层会回滚
 * 而且new一个事物会保证数据源的切换
 *@ChangeDs(key = "ds1")
 * @Transactional(propagation=Propagation.REQUIRES_NEW)
 * 外部异常不会影响内部,所以这种切数据源的方法,应该放在最后面
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ChangeDs {

	/**
	 * 数据源key
	 *
	 * @return the string
	 */
	String key();

}
