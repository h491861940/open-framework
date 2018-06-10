package com.open.framework.cache.mode.impl;


import com.open.framework.commmon.BaseConstant;
import com.open.framework.commmon.enums.EnumBase;
import com.open.framework.cache.mode.CacheMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;


@Component
public class EhcacheMode implements CacheMode {
    @Autowired(required = false)
    CacheManager cacheManage;

    @Override
    public String getCode() {
        return EnumBase.CacheCode.EHCACHE.getCode();
    }

    @Override
    public void set(String key, Object value) {
        set(BaseConstant.CACHE_FRAMEWORK, key, value);
    }

    @Override
    public void set(String name, String key, Object value) {
        Cache cache = cacheManage.getCache(name);
        cache.put(key, value);
    }

    @Override
    public Object get(String key) {
        return get(BaseConstant.CACHE_FRAMEWORK, key);
    }

    @Override
    public Object get(String name, String key) {
        Cache cache = cacheManage.getCache(name);
        ValueWrapper value = cache.get(key);
        if (null != value && null != value.get()) {
            return value.get();
        }
        return null;
    }

    @Override
    public void remove(String key) {
        remove(BaseConstant.CACHE_FRAMEWORK, key);
    }

    @Override
    public void remove(String name, String key) {
        org.springframework.cache.ehcache.EhCacheCacheManager cacheCacheManager = (EhCacheCacheManager) cacheManage;
        net.sf.ehcache.CacheManager ehCacheManager = cacheCacheManager.getCacheManager();
        net.sf.ehcache.Cache cache = ehCacheManager.getCache(name);
        cache.remove(key);
    }

}
