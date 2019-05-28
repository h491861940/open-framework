package com.open.framework.workflow.api.dto;

import lombok.Data;

import java.io.Serializable;


/**
 * @Auther: hsj
 * @Date: 2019/5/14 18:57
 * @Description: 流程定义查询对象
 */
@Data
public class ProcessDefQueryDTO implements Serializable {
    /**
     * 流程定义id
     */
    private String processDefId;
    /**
     * 部署id
     */
    private String deploymentId;
    /**
     * key
     */
    private String processDefKey;
    private Integer firstResult;
    private Integer maxResults;
}
