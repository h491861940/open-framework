package com.open.framework.cache.mode;

import java.util.HashMap;
import java.util.Map;

import com.open.framework.cache.CacheUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class CacheModeFactory implements ApplicationContextAware {
    private static Map<String, CacheMode> cacheBeanMap;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, CacheMode> map = applicationContext.getBeansOfType(CacheMode.class);
        cacheBeanMap = new HashMap<>();
        map.forEach((key, value) -> cacheBeanMap.put(value.getCode(), value));
        CacheUtil.init();//初始化缓存
    }

    public static <T extends CacheMode> T getCacheMode(String code) {
        return (T) cacheBeanMap.get(code);
    }
}