package com.open.framework.workflow.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: hsj
 * @Date: 2019/5/15 11:19
 * @Description: 任务对象
 */
@Data
public class TaskDTO implements Serializable {
    /**
     * 任务id
     */
    private String id;
    /**
     * 任务名称
     */
    private String name;
    /**
     * 所有者
     */
    private String owner;
    /**
     * 执行人
     */
    private String assignee;
    private String parentTaskId;
    private String description;
    /**
     * 创建时间
     */
    private Date createTime;
    private Date dueDate;
    private String executionId;
    /**
     * 实例id
     */
    private String processInstanceId;
    private String processDefinitionId;
    private String taskDefinitionKey;
    private String formKey;
    private Date claimTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 花费时间
     */
    protected Long durationInMillis;
    public TaskDTO(){}
    public TaskDTO(String id, String name,String processInstanceId, Date createTime, Date endTime, Long durationInMillis, String
            assignee, Date dueDate){
        this.id = id;
        this.name = name;
        this.assignee = assignee;
        this.createTime = createTime;
        this.dueDate = dueDate;
        this.processInstanceId = processInstanceId;
        this.endTime=endTime;
        this.durationInMillis=durationInMillis;
    }
    public TaskDTO(String id, String name, String owner, String assignee, String
            parentTaskId, String description, Date createTime, Date dueDate, String executionId, String
            processInstanceId, String processDefinitionId, String taskDefinitionKey, String formKey, Date claimTime) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.assignee = assignee;
        this.parentTaskId = parentTaskId;
        this.description = description;
        this.createTime = createTime;
        this.dueDate = dueDate;
        this.executionId = executionId;
        this.processInstanceId = processInstanceId;
        this.processDefinitionId = processDefinitionId;
        this.taskDefinitionKey = taskDefinitionKey;
        this.formKey = formKey;
        this.claimTime = claimTime;
    }
}
