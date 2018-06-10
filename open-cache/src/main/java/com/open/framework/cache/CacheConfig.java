package com.open.framework.cache;

import com.open.framework.commmon.BaseConstant;
import com.open.framework.cache.properties.CacheManageProperties;
import com.open.framework.cache.properties.CacheProperties;
import com.open.framework.commmon.utils.StringUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;


import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.DiskStoreConfiguration;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

import static com.open.framework.commmon.utils.StringUtil.*;
/*caches:
        - name: platform
        memoryStoreEvictionPolicy: LRU
        maxEntriesLocalHeap: 1000
        timeToLiveSeconds: 1800
        timeToIdleSeconds: 1800
        diskExpiryThreadIntervalSeconds: 600
        eternal: true
        maxEntriesLocalDisk: 0
        diskPersistent: true
        maxElementsInMemory: 1
        - name: test
        memoryStoreEvictionPolicy: LRU
        maxEntriesLocalHeap: 1000
        timeToLiveSeconds: 1800
        timeToIdleSeconds: 1800
        diskExpiryThreadIntervalSeconds: 600*/
/*cache元素的属性：
name：缓存名称
maxElementsInMemory：内存中最大缓存对象数
maxElementsOnDisk：硬盘中最大缓存对象数，若是0表示无穷大
eternal：true表示对象永不过期，此时会忽略timeToIdleSeconds和timeToLiveSeconds属性，默认为false
overflowToDisk：true表示当内存缓存的对象数目达到了
maxElementsInMemory界限后，会把溢出的对象写到硬盘缓存中。注意：如果缓存的对象要写入到硬盘中的话，则该对象必须实现了Serializable接口才行。
diskSpoolBufferSizeMB：磁盘缓存区大小，默认为30MB。每个Cache都应该有自己的一个缓存区。
diskPersistent：是否缓存虚拟机重启期数据，是否持久化磁盘缓存,当这个属性的值为true时,系统在初始化时会在磁盘中查找文件名 为cache名称,后缀名为index的文件，这个文件中存放了已经持久化在磁盘中的cache的index,找到后会把cache加载到内存，要想把 cache真正持久化到磁盘,写程序时注意执行net.sf.ehcache.Cache.put(Element element)后要调用flush()方法。
diskExpiryThreadIntervalSeconds：磁盘失效线程运行时间间隔，默认为120秒
timeToIdleSeconds： 设定允许对象处于空闲状态的最长时间，以秒为单位。当对象自从最近一次被访问后，如果处于空闲状态的时间超过了timeToIdleSeconds属性 值，这个对象就会过期，EHCache将把它从缓存中清空。只有当eternal属性为false，该属性才有效。如果该属性值为0，则表示对象可以无限 期地处于空闲状态
timeToLiveSeconds：设定对象允许存在于缓存中的最长时间，以秒为单位。当对象自从被存放到缓存中后，如果处于缓存中的时间超过了 timeToLiveSeconds属性值，这个对象就会过期，EHCache将把它从缓存中清除。只有当eternal属性为false，该属性才有 效。如果该属性值为0，则表示对象可以无限期地存在于缓存中。timeToLiveSeconds必须大于timeToIdleSeconds属性，才有 意义
memoryStoreEvictionPolicy：当达到maxElementsInMemory限制时，Ehcache将会根据指定的策略去清理内存。可选策略有：LRU（最近最少使用，默认策略）、FIFO（先进先出）、LFU（最少访问次数）。
*/
@Configuration
@EnableCaching
@ConditionalOnProperty(name = BaseConstant.CACHE_TYPE,havingValue = BaseConstant.CACHE_EHCACHE)
@EnableConfigurationProperties(CacheManageProperties.class)
public class CacheConfig implements CachingConfigurer {

    private CacheManageProperties cacheManageProperties;

    public CacheConfig(CacheManageProperties cacheManageProperties){
        this.cacheManageProperties = cacheManageProperties;
    }

    @SuppressWarnings("deprecation")
    @ConditionalOnMissingBean
    @Bean(destroyMethod = "shutdown")
    public net.sf.ehcache.CacheManager ehCacheManager() {
        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        config.diskStore(new DiskStoreConfiguration().path(StringUtil.isEmptyDefault(cacheManageProperties.getDiskStore(),System.getProperty("java.io.tmpdir"))));
        if(cacheManageProperties.getCaches().size()<1){
            List<CacheProperties> list=new ArrayList<CacheProperties>();
            CacheProperties cacheProperties=new CacheProperties();
            cacheProperties.setName(BaseConstant.CACHE_FRAMEWORK);
            cacheProperties.setMemoryStoreEvictionPolicy("LRU");
            cacheProperties.setMaxElementsInMemory(1);
            cacheProperties.setMaxEntriesLocalDisk(0);
            cacheProperties.setEternal(true);
            cacheProperties.setDiskPersistent(true);
            cacheProperties.setMaxEntriesLocalHeap(1000);
            cacheProperties.setTimeToIdleSeconds(1800);
            cacheProperties.setTimeToLiveSeconds(1800);
            cacheProperties.setDiskExpiryThreadIntervalSeconds(600);
            list.add(cacheProperties);
            cacheManageProperties.setCaches(list);
        }
        config.setName(StringUtil.isEmptyDefault(cacheManageProperties.getName(),BaseConstant.CACHE_NAME));
        for (CacheProperties cacheProperties : cacheManageProperties.getCaches()) {
            CacheConfiguration cacheConfiguration = new CacheConfiguration();
            cacheConfiguration.setName(cacheProperties.getName());
            cacheConfiguration.setEternal(cacheProperties.isEternal());
            cacheConfiguration.setMaxEntriesLocalDisk(cacheProperties.getMaxEntriesLocalDisk());
            cacheConfiguration.setDiskPersistent(cacheProperties.isDiskPersistent());
            cacheConfiguration.setMaxElementsInMemory(cacheProperties.getMaxElementsInMemory());
            cacheConfiguration.setMemoryStoreEvictionPolicy(cacheProperties.getMemoryStoreEvictionPolicy());
            cacheConfiguration.setMaxEntriesLocalHeap(cacheProperties.getMaxEntriesLocalHeap());
            cacheConfiguration.setTimeToLiveSeconds(cacheProperties.getTimeToLiveSeconds());
            cacheConfiguration.setTimeToIdleSeconds(cacheProperties.getTimeToIdleSeconds());
            cacheConfiguration.setOverflowToDisk(true);
            cacheConfiguration.setDiskExpiryThreadIntervalSeconds(cacheProperties.getDiskExpiryThreadIntervalSeconds());
            config.addCache(cacheConfiguration);
        }
        return net.sf.ehcache.CacheManager.newInstance(config);
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheManager());
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

    @Override
    public CacheResolver cacheResolver() {

        return null;
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return null;
    }

}
