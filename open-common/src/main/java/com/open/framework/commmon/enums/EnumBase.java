package com.open.framework.commmon.enums;


import com.open.framework.commmon.BaseConstant;

public final class EnumBase {

    public static void main(String[] args) {
        System.out.println(CacheCode.GUAVA.getCode());
    }

    public enum CommonYN implements IEnumFace {

        //
        yes(1, "是"),

        no(0, "否");
        private NVPair p;

        CommonYN(Integer val, String description) {
            this.p = new NVPair(val, description);
        }

        @Override
        public NVPair getPair() {
            return p;
        }

        public Integer getVal() {
            return p.getVal();
        }

        public String getText() {
            return p.getText();
        }
    }

    public enum EnableDisable implements IEnumFace {

        //
        enable(1, "启用"),

        disable(0, "禁用");
        private NVPair p;

        EnableDisable(Integer val, String description) {
            this.p = new NVPair(val, description);
        }

        @Override
        public NVPair getPair() {
            return p;
        }

        public Integer getVal() {
            return p.getVal();
        }

        public String getText() {
            return p.getText();
        }
    }

    public enum CacheCode implements IEnumFace {
        GUAVA(BaseConstant.CACHE_GUAVA, "内存"), REDIS(BaseConstant.CACHE_REDIS, "redis"), EHCACHE(BaseConstant
                .CACHE_EHCACHE, "ehcache");

        private NVPair p;

        CacheCode(String code, String description) {
            this.p = new NVPair(code, description);
        }

        @Override
        public NVPair getPair() {
            return p;
        }

        public String getCode() {
            return p.getCode();
        }
    }

    public enum PlatformCode implements IEnumFace {
        SYSTEM_ERROR(0, "系统异常:  "), UNKONW_ERROR(-1, "未知错误"), SQL_ERROR(1000, "数据库错误"), TRANSACTION_ERROR(1100, "事物异常");
        private NVPair p;

        PlatformCode(Integer val, String description) {
            this.p = new NVPair(val, description);
        }

        @Override
        public NVPair getPair() {
            return p;
        }

        public Integer getVal() {
            return p.getVal();
        }

        public String getText() {
            return p.getText();
        }
    }
}
