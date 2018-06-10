package com.open.framework.cache.mode.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.open.framework.commmon.enums.EnumBase;
import com.open.framework.cache.mode.CacheMode;
import org.springframework.stereotype.Component;


@Component
public class GuavaMode implements CacheMode {
    static LoadingCache<String, Object> cahceBuilder = CacheBuilder.newBuilder().build(new CacheLoader<String, Object>() {

        @Override
        public Object load(String key) throws Exception {
            return null;
        }
    });

    @Override
    public String getCode() {
        return EnumBase.CacheCode.GUAVA.getCode();
    }

    @Override
    public void set(String key,Object value) {
        cahceBuilder.put(key, value);
    }

    @Override 
    public Object get(String key) {
        try {
            return cahceBuilder.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void set(String name, String key, Object value) {
        set(key,value);
    }

    @Override
    public Object get(String name, String key) {
        return get(key);
    }

    @Override
    public void remove(String key) {
        cahceBuilder.invalidate(key);
    }

    @Override
    public void remove(String name, String key) {
        cahceBuilder.invalidate(key);
    }
}
