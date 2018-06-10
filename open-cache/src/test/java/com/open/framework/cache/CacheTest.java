package com.open.framework.cache;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheTest {
    @Autowired(required = false)
    CacheManager cacheManage;
    
    @Cacheable(value="framework",key="'add'")
    public String use(String username) {
        System.out.println("新增成功"+username);
        return "1111";
    }

    @CacheEvict(value="framework",key="'add'")
    public void clear() {
        System.out.println("删除成功");

    }

}
