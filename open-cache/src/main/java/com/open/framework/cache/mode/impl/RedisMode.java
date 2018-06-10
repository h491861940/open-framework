package com.open.framework.cache.mode.impl;

import com.open.framework.cache.RedisUtils;
import com.open.framework.commmon.enums.EnumBase;
import com.open.framework.cache.mode.CacheMode;
import org.springframework.stereotype.Component;


@Component
public class RedisMode implements CacheMode {


    @Override
    public String getCode() {
        return EnumBase.CacheCode.REDIS.getCode();
    }

    @Override
    public void set(String key, Object value) {
        RedisUtils.set(key,value);
    }

    @Override
    public void set(String name, String key, Object value) {
        RedisUtils.set(key,value);
    }

    @Override
    public Object get(String key) {
        return RedisUtils.get(key);
    }

    @Override
    public Object get(String name, String key) {
        return RedisUtils.get(key);
    }

    @Override
    public void remove(String key) {
        RedisUtils.delete(key);
    }

    @Override
    public void remove(String name, String key) {
        RedisUtils.delete(key);
    }

}
