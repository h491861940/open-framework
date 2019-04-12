package com.open.framework.schedule.dto;


import com.open.framework.dao.model.BaseDTO;
import com.open.framework.dao.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @Description: 任务详情参数实体
 * @CreateDate: 2018/7/19 0019 上午 10:20
 * @Version: 1.0
 */

public class TaskDetailParamDTO extends BaseDTO {

    public TaskDetailParamDTO() {
    }
    public TaskDetailParamDTO(String gid) {
        this.gid=gid;
    }

    /**
     * 任务详情ID
     */
    private String detailId;

    @Transient
    private String paramCode;

    @Transient
    private Integer lvl;
    /**
     * 任务参数ID
     */
    private String paramId;

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

    public Integer getLvl() {
        return lvl;
    }

    public void setLvl(Integer lvl) {
        this.lvl = lvl;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
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
