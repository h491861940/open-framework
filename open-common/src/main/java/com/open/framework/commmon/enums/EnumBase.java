package com.open.framework.commmon.enums;


import com.open.framework.commmon.BaseConstant;

public final class EnumBase
{

    public static void main(String[] args)
    {
        System.out.println(CacheCode.GUAVA.getCode());
    }

    public enum CacheCode implements IEnumFace
    {
        GUAVA(BaseConstant.CACHE_GUAVA, "内存"), REDIS(BaseConstant.CACHE_REDIS, "redis"),EHCACHE(BaseConstant.CACHE_EHCACHE, "ehcache");

        private NVPair p;

        CacheCode(String code, String description)
        {
            this.p = new NVPair(code, description);
        }

        @Override
        public NVPair getPair()
        {
            return p;
        }

        public String getCode()
        {
            return p.getCode();
        }
    }

}
