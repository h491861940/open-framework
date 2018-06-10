package com.open.framework.cache.properties;

import java.util.ArrayList;
import java.util.List;

import com.open.framework.commmon.BaseConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = BaseConstant.CACHE_PROPERTIES)
public class CacheManageProperties {

    private String                name;

    private String                diskStore;

    private List<CacheProperties> caches = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiskStore() {
        return diskStore;
    }

    public void setDiskStore(String diskStore) {
        this.diskStore = diskStore;
    }

    public List<CacheProperties> getCaches() {
        return caches;
    }

    public void setCaches(List<CacheProperties> caches) {
        this.caches = caches;
    }

}
