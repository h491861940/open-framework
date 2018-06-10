package com.open.framework.dao.dynamic;

import java.lang.annotation.*;

/**
 * 指定数据源注解</br>
 *
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
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
