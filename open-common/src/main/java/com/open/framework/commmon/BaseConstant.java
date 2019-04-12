package com.open.framework.commmon;

import java.util.HashMap;
import java.util.Map;

public class BaseConstant {
    public static final String DEFAULT_CONFIG="application.yaml";

    //web返回前台提示    start
    public static final int RESULT_SUCCESS=0;
    public static final String RESULT_SUCCESS_MSG="success";
    //web返回前台提示    end

    //缓存常量    start
    /**
     * 获取ehcahce的配置
     */
    public static final String CACHE_PROPERTIES = "open.cache";
    /**
     * ehcache的时候默认的放入框架缓存
     */
    public static final String CACHE_FRAMEWORK = "framework";
    public static final String CACHE_NAME="open";
    /**
     * 判断CacheUtil初始化的缓存类型
     */
    public static final String CACHE_TYPE = "open.cache.type";
    public static final String CACHE_GUAVA = "guava";
    public static final String CACHE_REDIS = "redis";
    public static final String CACHE_EHCACHE = "ehcache";

    //缓存常量    end

    //licence常量    start
    /**
     * 是否坚持licence
     */
    public static final String LICENSE_CHECK = "open.license.check";
    /**
     * 检查licence的路径
     */
    public static final String LICENSE_PATH = "open.license.path";
    /**
     * 检查licence的配置,如果强制开启,把对应检查的启动类注解去掉即可
     */
    public static final String LICENSE_CHECK_YES = "true";
    //licence常量    end

    //多数据源常量   start

    /**
     * 主数据源配置前缀
     */
    public static final String PRIMARY_DS_PREFIX = "spring.datasource";

    /**
     * 定制数据源配置前缀
     */
    public static final String MULTI_DS_PREFIX = "open.datasource.multi";
    //多数据源常量   end

    //dao条件比较符   start
    /**
     * 比较方式：等于
     */
    public static final String EQUAL = "eq";
    /**
     * 比较方式：不等于
     */
    public static final String NOT_EQUAL = "ne";
    /**
     * 比较方式：小于
     */
    public static final String LESS = "lt";
    /**
     * 比较方式：小于或等于
     */
    public static final String LESS_OR_EQUAL = "le";
    /**
     * 比较方式：大于
     */
    public static final String GREATER = "gt";
    /**
     * 比较方式：大于或等于
     */
    public static final String GREATER_OR_EQUAL = "ge";
    /**
     * 比较方式：开始于
     */
    public static final String BEGINS_WITH = "bw";
    /**
     * 比较方式：不开始于
     */
    public static final String NOT_BEGINS_WITH = "bn";
    /**
     * 比较方式：结束与
     */
    public static final String ENDS_WITH = "ew";
    /**
     * 比较方式：不结束与
     */
    public static final String NOT_ENDS_WITH = "en";
    /**
     * 比较方式：为null
     */
    public static final String IS_NULL = "null";
    /**
     * 比较方式：不为null
     */
    public static final String IS_NOT_NULL = "not";
    /**
     * 比较方式：属于
     */
    public static final String IS_IN = "in";
    /**
     * 比较方式：不属于
     */
    public static final String IS_NOT_IN = "ni";
    /**
     * 比较方式：为null或''
     */
    public static final String IS_EMPTY = "ie";
    /**
     * 比较方式：不为null或''
     */
    public static final String IS_NOT_EMPTY = "ine";
    /**
     * 比较方式：LIKE
     */
    public static final String LIKE = "like";
    /**
     * 比较方式：LIKE百分号在前
     */
    public static final String LIKEB = ":like";
    /**
     * 比较方式：LIKE百分号灾后
     */
    public static final String LIKEA = "like:";
    /**
     * 比较方式：NOT LIKE
     */
    public static final String NOT_LIKE = "nlike";

    /**
     * 逻辑方式"AND"
     */
    public static final String DAO_AND = "and";
    /**
     * 逻辑方式"OR"
     */
    public static final String DAO_OR = "or";
    //dao条件比较符   start
    //dao 逻辑删除   start
    /**
     * 逻辑删除字段名.
     */
    public final static String DELETEED_FIELD = "delState";
    /**
     * 逻辑删除是否开启
     */
    public final static String USE_LOGIC_DELETE="true";
    /**
     * 获取逻辑删除是否开启的属性
     */
    public final static String LOGIC_DELETE_YAML="open.logic-delete";
    //dao 逻辑删除   end

    public static final String PAGE_SIZE = "pageSize";

    public static final String PAGE      = "page";

    //导出相关常量   start
    /**
     * 存放导出转换的值,gid==value或者code==value,每次启动或者别的时候往进丢
     */
    public static Map formatMap=new HashMap();
    //导出相关常量   end
}
