package com.open.framework.schedule.entity;


import com.open.framework.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Description: 任务注册实体
 * @CreateDate: 2018/7/19 0019 上午 10:12
 * @Version: 1.0
 */

@Entity
@Table(name = "QRTZ_TASK_REG")
public class TaskReg extends BaseEntity {

    public TaskReg() {
    }

    public TaskReg(String gid) {
        this.gid = gid;
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
