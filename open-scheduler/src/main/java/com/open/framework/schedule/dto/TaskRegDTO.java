package com.open.framework.schedule.dto;


import com.open.framework.dao.model.BaseDTO;
import com.open.framework.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

public class TaskRegDTO extends BaseDTO {

    public TaskRegDTO() {
    }

    /**
     * 注册的任务名称
     */
    private String name;

    /**
     * 全类名
     */
    private String fullClassName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public void setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
    }
}
