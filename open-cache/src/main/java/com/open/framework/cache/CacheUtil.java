package com.open.framework.cache;


import com.open.framework.commmon.BaseConstant;
import com.open.framework.commmon.enums.EnumBase;
import com.open.framework.cache.mode.CacheMode;
import com.open.framework.cache.mode.CacheModeFactory;
import com.open.framework.commmon.utils.YamlUtils;


public class CacheUtil {

    static CacheMode mode;

    public static void init() {
        if (mode == null) {
            String cacheType = YamlUtils.DefaultGetYamlValue(BaseConstant.CACHE_TYPE);
            if (BaseConstant.CACHE_REDIS.equals(cacheType)) {
                mode = CacheModeFactory.getCacheMode(EnumBase.CacheCode.REDIS.getCode());//BaseConstant.CACHE_REDIS直接用这个也行,只是为了验证枚举
            } else if (BaseConstant.CACHE_EHCACHE.equals(cacheType)) {
                mode = CacheModeFactory.getCacheMode(EnumBase.CacheCode.EHCACHE.getCode());
            } else {
                mode = CacheModeFactory.getCacheMode(EnumBase.CacheCode.GUAVA.getCode());
            }
        }
    }

    public static void set(String key, Object value) {
        if(null!=mode){
            mode.set(key, value);
        }
    }

    public static void set(String name, String key, Object value) {
        if(null!=mode){
            mode.set(name, key, value);
        }
    }

    public static Object get(String key) {
        try {
            return mode.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    public static Object get(String name, String key) {
        try {
            return mode.get(name, key);
        } catch (Exception e) {
            return null;
        }
    }

    public static void remove(String key) {
        if(null!=mode){
            mode.remove(key);
        }
    }

    public static void remove(String name, String key) {
        if(null!=mode){
            mode.remove(name, key);
        }
    }
}
