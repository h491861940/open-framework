package com.open.framework.demo.exception;

import com.open.framework.commmon.enums.IEnumFace;
import com.open.framework.commmon.enums.NVPair;

public enum BusinessExCode implements IEnumFace {
    BUSINESS_ERROR(2000, "业务统一异常"), CODE_ERROR(2001, "编码异常"),NAME_ERROR(2002,"名称错误%s");
    private NVPair p;

    BusinessExCode(Integer val, String description) {
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