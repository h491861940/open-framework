package com.open.framework.schedule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.open.framework.dao.model.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @Description: 任务详情实体
 * @CreateDate: 2018/7/19 0019 上午 11:00
 * @Version: 1.0
 */

@Entity
@Table(name = "QRTZ_TASK_DETAIL")
@DynamicUpdate
@DynamicInsert
public class TaskDetail extends BaseEntity {

    public TaskDetail() {
    }
    public TaskDetail(String gid) {
        this.gid=gid;
    }
    /**
     * 任务分组
     */
    private String groupName;

    /**
     * 任务注册ID
     */
    private String regId;

    /**
     * 名称
     */
    private String name;

    /**
     * 任务类型
     *
     * @see com.huiju.framework.scheduler.enums.TaskType
     */
    private Integer type;

    /**
     * 任务级别
     */
    private Integer lvl;

    /**
     * CRON表达式
     */
    private String cronExpr;

    /**
     * 当前任务状态
     */
    private String currentState;

    /**
     * 最近一次执行时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date execLastTime;

    /**
     * 下次执行时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date execNextTime;

    /**
     * 是否记录日志
     */
    private Integer recordLog;

    /**
     * 是否启动，0否，1是
     * 启动：交给quartz运行任务
     * 不启动：不交给quartz;启动->不启动，从quartz中删除正在运行的任务
     */
    private Integer startup;

    /**
     * 描述
     */
    private String description;

    /**
     * 注册的任务
     */
    @ManyToOne()
    @JoinColumn(name = "regId", insertable = false, updatable = false)
    private TaskReg reg;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = TaskDetailParam.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "detailId")
    private List<TaskDetailParam> taskDetailParams;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getLvl() {
        return lvl;
    }

    public void setLvl(Integer lvl) {
        this.lvl = lvl;
    }

    public String getCronExpr() {
        return cronExpr;
    }

    public void setCronExpr(String cronExpr) {
        this.cronExpr = cronExpr;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public Date getExecLastTime() {
        return execLastTime;
    }

    public void setExecLastTime(Date execLastTime) {
        this.execLastTime = execLastTime;
    }

    public Date getExecNextTime() {
        return execNextTime;
    }

    public void setExecNextTime(Date execNextTime) {
        this.execNextTime = execNextTime;
    }

    public Integer getRecordLog() {
        return recordLog;
    }

    public void setRecordLog(Integer recordLog) {
        this.recordLog = recordLog;
    }

    public Integer getStartup() {
        return startup;
    }

    public void setStartup(Integer startup) {
        this.startup = startup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskReg getReg() {
        return reg;
    }

    public void setReg(TaskReg reg) {
        this.reg = reg;
    }

    public List<TaskDetailParam> getTaskDetailParams() {
        return taskDetailParams;
    }

    public void setTaskDetailParams(List<TaskDetailParam> taskDetailParams) {
        this.taskDetailParams = taskDetailParams;
    }
}
