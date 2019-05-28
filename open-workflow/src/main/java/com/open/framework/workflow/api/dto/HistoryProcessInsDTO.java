package com.open.framework.workflow.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: hsj
 * @Date: 2019/5/15 11:19
 * @Description: 历史实例对象
 */
@Data
public class HistoryProcessInsDTO implements Serializable {
    private String id;
    /**
     * 实例名称
     */
    private String name;
    private String businessKey;
    private String deleteReason;
    /**
     * 部署id
     */
    private String deploymentId;
    private String description;
    private Long durationInMillis;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    private String processDefinitionId;
    private String processDefinitionKey;
    private String processDefinitionName;
    private Integer processDefinitionVersion;
    /**
     * 流程实例id
     */
    private String processInstanceId;
    /**
     * 启动人
     */
    private String startUserId;
}
