package com.open.framework.cache.properties;

public class CacheProperties {

    private String  name;
    private String  memoryStoreEvictionPolicy;
    private long    maxEntriesLocalHeap;
    private long    timeToLiveSeconds;
    private long    timeToIdleSeconds;
    private long    diskExpiryThreadIntervalSeconds;
    private boolean eternal;
    private long    maxEntriesLocalDisk;
    private boolean diskPersistent;
    private int     maxElementsInMemory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemoryStoreEvictionPolicy() {
        return memoryStoreEvictionPolicy;
    }

    public void setMemoryStoreEvictionPolicy(String memoryStoreEvictionPolicy) {
        this.memoryStoreEvictionPolicy = memoryStoreEvictionPolicy;
    }

    public long getMaxEntriesLocalHeap() {
        return maxEntriesLocalHeap;
    }

    public void setMaxEntriesLocalHeap(long maxEntriesLocalHeap) {
        this.maxEntriesLocalHeap = maxEntriesLocalHeap;
    }

    public long getTimeToLiveSeconds() {
        return timeToLiveSeconds;
    }

    public void setTimeToLiveSeconds(long timeToLiveSeconds) {
        this.timeToLiveSeconds = timeToLiveSeconds;
    }

    public long getTimeToIdleSeconds() {
        return timeToIdleSeconds;
    }

    public void setTimeToIdleSeconds(long timeToIdleSeconds) {
        this.timeToIdleSeconds = timeToIdleSeconds;
    }

    public long getDiskExpiryThreadIntervalSeconds() {
        return diskExpiryThreadIntervalSeconds;
    }

    public void setDiskExpiryThreadIntervalSeconds(long diskExpiryThreadIntervalSeconds) {
        this.diskExpiryThreadIntervalSeconds = diskExpiryThreadIntervalSeconds;
    }

    public boolean isEternal() {
        return eternal;
    }

    public void setEternal(boolean eternal) {
        this.eternal = eternal;
    }

    public long getMaxEntriesLocalDisk() {
        return maxEntriesLocalDisk;
    }

    public void setMaxEntriesLocalDisk(long maxEntriesLocalDisk) {
        this.maxEntriesLocalDisk = maxEntriesLocalDisk;
    }

    public boolean isDiskPersistent() {
        return diskPersistent;
    }

    public void setDiskPersistent(boolean diskPersistent) {
        this.diskPersistent = diskPersistent;
    }

    public int getMaxElementsInMemory() {
        return maxElementsInMemory;
    }

    public void setMaxElementsInMemory(int maxElementsInMemory) {
        this.maxElementsInMemory = maxElementsInMemory;
    }

}
