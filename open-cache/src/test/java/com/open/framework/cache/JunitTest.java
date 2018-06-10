package com.open.framework.cache;

import com.open.framework.cache.properties.CacheManageProperties;
import com.open.framework.commmon.utils.StringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JunitTest.class)
@EnableConfigurationProperties
@ContextConfiguration(classes = { CacheConfig.class })
@ComponentScan
public class JunitTest {

    @Autowired(required = false)
    private CacheManageProperties cacheProperties;
    @Autowired
    private CacheTest             cacheTest;

    @Test
    public void testEhcacheUse() {
        cacheTest.use("aa");
        cacheTest.use("aa");
    }

    @Test
    public void testEhcacheClear() {
        cacheTest.use("aa");
        cacheTest.clear();
        cacheTest.use("aa");
    }
    @Test
    public void testCacheUtil() {
        //RedisUtils.set("qqqq","testCache");
        CacheUtil.set("test2","testCache");
       System.out.println(CacheUtil.get("test2"));
      // CacheUtil.remove("test");
      // System.out.println(CacheUtil.get("test"));
    }

    @Test
    public void propsTest() {
        System.out.println("name: " + cacheProperties.getName());
        System.out.println("diskStore: " + cacheProperties.getDiskStore());
        System.out.println("caches: " + cacheProperties.getCaches().size());
        System.out.println("caches: " + cacheProperties.getCaches().get(0).getMemoryStoreEvictionPolicy());
    }
}
