package com.open.framework.commmon;

public class BaseConstant {
    public static final String DEFAULT_CONFIG="application.yaml";
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
    public static final String LICENCE_CHECK = "open.licence.check";
    /**
     * 检查licence的路径
     */
    public static final String LICENCE_PATH = "open.licence.path";
    /**
     * 检查licence的配置,如果强制开启,把对应检查的启动类注解去掉即可
     */
    public static final String LICENCE_CHECK_YES = "true";
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
}
