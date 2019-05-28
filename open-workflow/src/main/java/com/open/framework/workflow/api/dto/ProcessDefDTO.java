package com.open.framework.workflow.api.dto;

import lombok.Data;

import java.io.Serializable;


/**
 * @Auther: hsj
 * @Date: 2019/5/14 18:57
 * @Description: 流程定义对象
 */
@Data
public class ProcessDefDTO implements Serializable {
    private String id;
    /**
     * 定义名称
     */
    private String name;
    private String description;
    /**
     * 流程key
     */
    private String  key;
    private Integer  version;
    private String category;
    /**
     * 部署id
     */
    private String deploymentId;
    private String resourceName;
    private String diagramResourceName;
    public ProcessDefDTO(){}

    public ProcessDefDTO(String id, String name, String description, String key, Integer version, String category,
                         String deploymentId, String resourceName, String diagramResourceName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.key = key;
        this.version = version;
        this.category = category;
        this.deploymentId = deploymentId;
        this.resourceName = resourceName;
        this.diagramResourceName = diagramResourceName;
    }
}
