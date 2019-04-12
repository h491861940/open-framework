package com.open.framework.schedule.entity;


import com.open.framework.dao.model.BaseEntity;

import javax.persistence.*;

/**
 * @Description: 任务详情参数实体
 * @CreateDate: 2018/7/19 0019 上午 10:20
 * @Version: 1.0
 */

@Entity
@Table(name = "QRTZ_TASK_DETAIL_PARAM")
public class TaskDetailParam extends BaseEntity {

    public TaskDetailParam() {
    }
    public TaskDetailParam(String gid) {
        this.gid=gid;
    }

    /**
     * 任务详情ID
     */
    private String detailId;

    private String paramCode;

    /**
     * 参数值
     */
    private String paramValue;

    /**
     * 描述
     */
    private String description;

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }



    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
