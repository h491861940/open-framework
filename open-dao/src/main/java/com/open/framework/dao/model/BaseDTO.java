package com.open.framework.dao.model;

import java.io.Serializable;

public class BaseDTO implements Serializable {
    private String gid;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }
}
