package com.open.framework.workflow.service;

import com.open.framework.workflow.api.dto.ProcessInsDTO;
import com.open.framework.workflow.api.dto.TaskDTO;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: hsj
 * @Date: 2019/5/14 11:38
 * @Description: 流程实例的操作
 */
@Service
public class ActProcessInsService {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;

    public String getProcessInsState(String processInsId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInsId).singleResult();
        if (processInstance == null) {
            //完成
            return "0";
        } else {
            //未完成
            return "1";
        }
    }

    /**
     * 激活和挂机流程实例
     *
     * @param processInsId
     * @param state        true激活,false挂起
     * @return
     */
    public String changeState(String processInsId, Boolean state) {
        if (null != state && state) {
            runtimeService.activateProcessInstanceById(processInsId);
            return "已激活ID为[" + processInsId + "]的流程实例。";
        } else {
            runtimeService.suspendProcessInstanceById(processInsId);
            return "已挂起ID为[" + processInsId + "]的流程实例。";
        }
    }
    private List getListDto(List<ProcessInstance> list,boolean haveTask){
        List<ProcessInsDTO> dtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                ProcessInsDTO processInsDTO =new ProcessInsDTO(item.getId(),item.getName(),item.getProcessDefinitionId(),item.getProcessDefinitionKey(),item.getDeploymentId()
                        ,item.getProcessInstanceId(),item.getStartTime(),item.getStartUserId());
                if(haveTask){
                    List<TaskDTO> taskList=actTaskService.findTaskByProcessIns(item.getId());
                    if(!CollectionUtils.isEmpty(taskList)){
                        TaskDTO taskDTO=taskList.get(0);
                        processInsDTO.setTaskId(taskDTO.getId());
                        processInsDTO.setTaskName(taskDTO.getName());
                        processInsDTO.setOwner(taskDTO.getOwner());
                        processInsDTO.setAssignee(taskDTO.getAssignee());
                        processInsDTO.setParentTaskId(taskDTO.getParentTaskId());
                        processInsDTO.setTaskCreateTime(taskDTO.getCreateTime());
                        processInsDTO.setTaskDueDate(taskDTO.getDueDate());
                        processInsDTO.setExecutionId(taskDTO.getExecutionId());
                        processInsDTO.setTaskDefKey(taskDTO.getTaskDefinitionKey());
                        processInsDTO.setTaskClaimTime(taskDTO.getClaimTime());
                        processInsDTO.setTaskEndTime(taskDTO.getEndTime());
                        processInsDTO.setDurationInMillis(taskDTO.getDurationInMillis());
                    }

                }
                dtoList.add(processInsDTO);
            });
        }
        return dtoList;
    }
    /**
     * 查询全部实例分页
     * @param page
     * @param pageSize
     * @param haveTask 是否包含任务
     * @return
     */
    public List<ProcessInsDTO> findAllPage(int page, int pageSize,boolean haveTask) {
        if(page>0){
            page=page-1;
        }
        List<ProcessInstance> list = runtimeService
                .createProcessInstanceQuery().orderByStartTime().desc().listPage(page,pageSize);
        return getListDto(list,haveTask);
    }

    /**
     * 根据启动人分页
     * @param userId 启动人
     * @param page
     * @param pageSize
     * @param haveTask 是否包含任务
     * @return
     */
    public List<ProcessInsDTO> findAllPageByUser(String userId,int page, int pageSize,boolean haveTask) {
        if(page>0){
            page=page-1;
        }
        List<ProcessInstance> list = runtimeService
                .createProcessInstanceQuery().startedBy(userId).orderByStartTime().desc().listPage(page,pageSize);
        return getListDto(list,haveTask);
    }
    /**
     * 查询全部实例不分页
     * @return
     */
    public List<ProcessInsDTO> findAll(boolean haveTask) {
        List<ProcessInstance> list = runtimeService
                .createProcessInstanceQuery().orderByStartTime().desc().list();
        return getListDto(list,haveTask);
    }

    /**
     * 获取启动人的流程实例
     * @param userId 启动人id
     * @param haveTask 是否包含任务信息返回
     * @return
     */
    public List<ProcessInsDTO> findAllByUser(String userId,boolean haveTask) {
        List<ProcessInstance> list = runtimeService
                .createProcessInstanceQuery().startedBy(userId).orderByStartTime().desc().list();
        return getListDto(list,haveTask);
    }
    /**
     * 根據流程定义Id查询
     * @param processDefId
     * @return
     */
    public List<ProcessInsDTO> getInstanceByDefinedId(String processDefId) {
        List<ProcessInstance> list = runtimeService
                .createProcessInstanceQuery()
                .processDefinitionId(processDefId).list();
        List<ProcessInsDTO> dtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                ProcessInsDTO ProcessInsDTO =new ProcessInsDTO(item.getId(),item.getName(),item.getProcessDefinitionId(),item.getProcessDefinitionKey(),item.getDeploymentId()
                        ,item.getProcessInstanceId(),item.getStartTime(),item.getStartUserId());
                dtoList.add(ProcessInsDTO);
            });
        }
        return dtoList;
    }

    /**
     * 设置流程变量
     * @param processInsId 实例id
     * @param key key
     * @param value 值
     */
    public void setVariable(String processInsId,String key, Object value) {
        //设置流程变量
        runtimeService.setVariable(processInsId, key, value);
    }

    /**
     * 获取实例变量
     * @param key 变量名称
     * @param processInsId 实例值
     * @return
     */
    public Object getVariable (String key, String processInsId) {
        Object variable = runtimeService.getVariable(processInsId, key);
        return variable;
    }

    /**
     * 获取实例全部参数
     * @param processInsId 实例id
     * @return
     */
    public Map getVariableAll (String processInsId) {
        Map map = runtimeService.getVariables(processInsId);
        return map;
    }
}
