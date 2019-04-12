package com.open.framework.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.open.framework.dao.model.BaseDTO;
import com.open.framework.dao.model.IdEntity;
import com.open.framework.schedule.entity.TaskDetail;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Description: 任务日志实体
 * @CreateDate: 2018/7/19 0019 上午 10:21
 * @Version: 1.0
 */
public class TaskLogDTO extends BaseDTO {

    public TaskLogDTO() {

    }
    public TaskLogDTO(String gid) {
        this.gid=gid;
    }
    /**
     * 任务详情ID
     */
    private String detailId;

    /**
     * 执行结果
     */
    private Integer result;

    /**
     * 信息
     */
    private String message;

    /**
     * 执行开始时间
     */
    private Date execStartTime;

    /**
     * 执行结束时间
     */
    private Date execEndTime;

    private TaskDetail detail;

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getExecStartTime() {
        return execStartTime;
    }

    public void setExecStartTime(Date execStartTime) {
        this.execStartTime = execStartTime;
    }

    public Date getExecEndTime() {
        return execEndTime;
    }

    public void setExecEndTime(Date execEndTime) {
        this.execEndTime = execEndTime;
    }

    public TaskDetail getDetail() {
        return detail;
    }

    public void setDetail(TaskDetail detail) {
        this.detail = detail;
    }
}
