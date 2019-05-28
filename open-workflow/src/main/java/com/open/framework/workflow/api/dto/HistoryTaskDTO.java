package com.open.framework.workflow.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @Auther: hsj
 * @Date: 2019/5/15 11:19
 * @Description: 历史任务对象,带参数变量
 */
@Data
public class HistoryTaskDTO implements Serializable {
   
    private String  id;
    /**
     * 任务名称
     */
    private String name;
    /**
     * 实例id
     */
    private String processInstanceId;
    /**
     * 流程定义id
     */
    private String processDefinitionId;
    private Date endTime;
    /**
     * 花费时间
     */
    private Long durationInMillis;
    private String deleteReason;
    private String executionId;
    private String parentTaskId;
    private String description;
    private String owner;
    private String assignee;
    private String taskDefinitionKey;
    private String formKey;
    private Date claimTime;
    private String category;
    private Date createTime;
    private Date dueDate;
    /**
     * 变量集合
     */
    private Map variabls;
    public HistoryTaskDTO(String id, String name, String owner, String assignee, String
            parentTaskId, String description, Date createTime, Date endTime,Long durationInMillis,Date dueDate, String executionId, String
                           processInstanceId, String processDefinitionId, String taskDefinitionKey, String formKey, Date claimTime) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.assignee = assignee;
        this.parentTaskId = parentTaskId;
        this.description = description;
        this.createTime = createTime;
        this.endTime=endTime;
        this.durationInMillis=durationInMillis;
        this.dueDate = dueDate;
        this.executionId = executionId;
        this.processInstanceId = processInstanceId;
        this.processDefinitionId = processDefinitionId;
        this.taskDefinitionKey = taskDefinitionKey;
        this.formKey = formKey;
        this.claimTime = claimTime;
    }
}
