package com.open.framework.cache;

import com.open.framework.commmon.utils.SpringUtil;
import org.springframework.data.redis.core.RedisTemplate;


public class RedisUtils {

    public static RedisTemplate redisTemplate = (RedisTemplate) SpringUtil.getBean("redisTemplate");

    public static void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public static Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public static void delete(String key) {
        redisTemplate.delete(key);
    }
}