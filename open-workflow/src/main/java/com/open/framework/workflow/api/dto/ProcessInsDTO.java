package com.open.framework.workflow.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: hsj
 * @Date: 2019/5/17 15:36
 * @Description: 流程实例对象
 */
@Data
public class ProcessInsDTO implements Serializable {
    /**
     * 流程实例id
     */
    private String  id;
    /**
     * 实例名称
     */
    private String name;
    /**
     * 流程定义id
     */
    private String  processDefId;
    /**
     * 流程定义key
     */
    private String  processDefKey;
    /**
     * 部署id
     */
    private String  deploymentId;
    /**
     * 流程实例id
     */
    private String   processInsId;
    /**
     * 流程实例启动时间
     */
    private Date startTime;
    /**
     * 流程启动实例人
     */
    private String  startUserId;
    /**
     * 任务详情
     */
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 所有者
     */
    private String owner;
    /**
     * 执行人
     */
    private String assignee;
    private String parentTaskId;
    /**
     * 创建时间
     */
    private Date taskCreateTime;
    private Date taskDueDate;
    private String executionId;
    private String taskDefKey;
    private String formKey;
    private Date taskClaimTime;
    /**
     * 结束时间
     */
    private Date taskEndTime;
    /**
     * 花费时间
     */
    protected Long durationInMillis;
    public ProcessInsDTO() {
    }

    public ProcessInsDTO(String id, String name, String processDefId, String processDefKey, String
            deploymentId, String processInsId, Date startTime,  String startUserId) {
        this.id = id;
        this.name = name;
        this.processDefId = processDefId;
        this.processDefKey = processDefKey;
        this.deploymentId = deploymentId;
        this.processInsId = processInsId;
        this.startTime = startTime;
        this.startUserId = startUserId;
    }
}
