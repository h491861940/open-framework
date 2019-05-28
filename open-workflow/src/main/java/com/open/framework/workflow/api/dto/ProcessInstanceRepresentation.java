package com.open.framework.workflow.api.dto;


import java.util.Date;

import lombok.Data;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
@Data
public class ProcessInstanceRepresentation  {
    protected String id;
    protected String name;
    protected String businessKey;
    protected String processDefinitionId;
    protected String tenantId;
    protected Date started;
    protected Date ended;
    protected String processDefinitionName;
    protected String processDefinitionDescription;
    protected String processDefinitionKey;
    protected String processDefinitionCategory;
    protected int processDefinitionVersion;
    protected String processDefinitionDeploymentId;
    protected boolean graphicalNotationDefined;
    protected boolean startFormDefined;

    public ProcessInstanceRepresentation(ProcessInstance processInstance, ProcessDefinition processDefinition, boolean graphicalNotation) {
        this(processInstance, graphicalNotation);
        this.mapProcessDefinition(processDefinition);
    }

    public ProcessInstanceRepresentation(ProcessInstance processInstance, boolean graphicalNotation) {
        this.id = processInstance.getId();
        this.name = processInstance.getName();
        this.businessKey = processInstance.getBusinessKey();
        this.processDefinitionId = processInstance.getProcessDefinitionId();
        this.tenantId = processInstance.getTenantId();
        this.graphicalNotationDefined = graphicalNotation;
    }

    public ProcessInstanceRepresentation(HistoricProcessInstance processInstance, ProcessDefinition processDefinition, boolean graphicalNotation) {
        this(processInstance, graphicalNotation);
        this.mapProcessDefinition(processDefinition);
    }

    public ProcessInstanceRepresentation(HistoricProcessInstance processInstance, boolean graphicalNotation) {
        this.id = processInstance.getId();
        this.name = processInstance.getName();
        this.businessKey = processInstance.getBusinessKey();
        this.processDefinitionId = processInstance.getProcessDefinitionId();
        this.tenantId = processInstance.getTenantId();
        this.graphicalNotationDefined = graphicalNotation;
        this.started = processInstance.getStartTime();
        this.ended = processInstance.getEndTime();
    }

    protected void mapProcessDefinition(ProcessDefinition processDefinition) {
        if (processDefinition != null) {
            this.processDefinitionName = processDefinition.getName();
            this.processDefinitionDescription = processDefinition.getDescription();
            this.processDefinitionKey = processDefinition.getKey();
            this.processDefinitionCategory = processDefinition.getCategory();
            this.processDefinitionVersion = processDefinition.getVersion();
            this.processDefinitionDeploymentId = processDefinition.getDeploymentId();
        }

    }


}
