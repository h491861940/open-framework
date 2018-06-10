package com.open.framework.dao.dynamic;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态数据源持有者</br>
 *
 */
public class DynamicDataSourceContextHolder {

	/**
	 * The constant dsHolder.
	 */
	private static final ThreadLocal<String> dsHolder = new ThreadLocal<String>();

	/**
	 * The constant dsKeys.
	 */
	public static List<String> dsKeys = new ArrayList<>();

	/**
	 * Sets data source.
	 *
	 * @param key the key
	 */
	public static void setDataSource(String key) {
		dsHolder.set(key);
	}

	/**
	 * Gets data source.
	 *
	 * @return the data source
	 */
	public static String getDataSource() {
		return dsHolder.get();
	}

	/**
	 * Clear data source.
	 */
	public static void clearDataSource() {
		dsHolder.remove();
	}

	/**
	 * Contains data source boolean.
	 *
	 * @param key the key
	 * @return the boolean
	 */
	public static boolean containsDataSource(String key){
		return dsKeys.contains(key);
	}

}
