package com.open.framework.cache.mode;

public interface CacheMode {

    /**
     * 查询编码
     *
     * @return 编码
     */
    String getCode();

    /**
     * set值
     */
    void set(String key, Object value);

    void set(String name, String key, Object value);

    /**
     * get值
     */
    Object get(String key);

    Object get(String name, String key);

    /**
     * 移除緩存
     * @param key
     */
    void remove(String key);

    void remove(String name, String key);
}
