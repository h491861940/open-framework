package com.open.framework.commmon.utils.collections;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 
 *  功能说明:集合操作工具类
 */
public class CollectionUtils {
	/**
	 * 去除重复元素
	 *
	 * @param list 需要处理的list
	 * @param <T>  泛型方法
	 * @return 去重后的list
	 */
	public static <T> List<T> removeDuplicate(List<T> list) {
		if (list == null || list.size() == 0) {
			return new ArrayList<>();
		}
		return new ArrayList<>(new HashSet<>(list));

	}
}
