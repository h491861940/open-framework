package com.open.framework.workflow.service;

import com.open.framework.commmon.BaseConstant;
import com.open.framework.workflow.api.dto.HistoryProcessInsDTO;
import com.open.framework.workflow.api.dto.HistoryTaskDTO;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: hsj
 * @Date: 2019/5/14 11:38
 * @Description: 历史记录
 */
@Service
public class ActHistoryService {
    @Autowired
    HistoryService historyService;

    /**
     * 查询历史任务
     *
     * @param processInsId
     * @return
     */
    public List<HistoryTaskDTO> getTaskByProcessInsId(String processInsId) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInsId)
                .orderByHistoricTaskInstanceStartTime().desc().list();

        List<HistoryTaskDTO> dtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                HistoryTaskDTO historyTaskDTO = new HistoryTaskDTO(item.getId(), item.getName(), item.getOwner(), item.getAssignee(), item
                        .getParentTaskId(), item.getDescription(), item.getCreateTime(),item.getEndTime(),item.getDurationInMillis(), item.getDueDate()
                        , item.getExecutionId(), item.getProcessInstanceId(), item.getProcessDefinitionId(), item
                        .getTaskDefinitionKey(), item.getFormKey(), item.getClaimTime());
                dtoList.add(historyTaskDTO);
            });
        }
        return dtoList;
    }

    /**
     * 查询任务,包含参数
     * @param processInsId
     * @return
     */
    public List<HistoryTaskDTO> getTaskByProcessInsIdParam(String processInsId) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInsId)
                .orderByHistoricTaskInstanceStartTime().desc().list();
        Map<String,Map> allVar=new HashMap();
            List<HistoricVariableInstance> listVar= historyService.createHistoricVariableInstanceQuery().processInstanceId(processInsId).list();
            for (int i = 0; i < listVar.size(); i++) {
                HistoricVariableInstance historicVariableInstance = listVar.get(i);
                String taskId=historicVariableInstance.getTaskId();
                Map tempMap=allVar.get(taskId);
                if(null==tempMap){
                    tempMap=new HashMap();
                }
                tempMap.put(historicVariableInstance.getVariableName(),historicVariableInstance.getValue());
                allVar.put(taskId,tempMap);
        }
        List<HistoryTaskDTO> dtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                HistoryTaskDTO historyTaskDTO = new HistoryTaskDTO(item.getId(), item.getName(), item.getOwner(), item.getAssignee(), item
                        .getParentTaskId(), item.getDescription(), item.getCreateTime(),item.getEndTime(),item.getDurationInMillis(), item.getDueDate()
                        , item.getExecutionId(), item.getProcessInstanceId(), item.getProcessDefinitionId(), item
                        .getTaskDefinitionKey(), item.getFormKey(), item.getClaimTime());
                historyTaskDTO.setVariabls(allVar.get(item.getId()));
                dtoList.add(historyTaskDTO);
            });
        }
        return dtoList;
    }
    /**
     * 查询历史流程实例
     *
     * @param processInsId
     * @return
     */
    public HistoryProcessInsDTO getProcessInsByProcessInsId(String processInsId) {
        HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInsId)
                .orderByProcessInstanceStartTime().asc().singleResult();
        if (null != hpi) {
            HistoryProcessInsDTO historyProcessInsDTO =null;// CommonHelper.copy(hpi, HistoryProcessInsDTO.class);
            return historyProcessInsDTO;
        }
        return null;
    }

    /**
     * 查询历史活动
     *
     * @param processInsId
     * @return
     */
    public List<HistoricActivityInstance> getActivityByProcessInsId(String processInsId) {
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInsId)
                .orderByHistoricActivityInstanceStartTime().asc()
                .list();
        if (list != null && list.size() > 0) {
            for (HistoricActivityInstance hai : list) {
                System.out.println(hai.getId() + "   " + hai.getProcessInstanceId() + "   " + hai.getActivityType()
                        + "  " + hai.getStartTime() + "   " + hai.getEndTime() + "   " + hai.getDurationInMillis());
            }
        }
        return list;
    }

    /**
     * 获取实例历史变量
     * @param processInsId 实例id
     * @param key key
     * @return
     */
    public Object getHisVariable(String processInsId,String key) {
        Object variable = null;
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInsId).list();
        for (int i = 0; i < list.size(); i++) {
            HistoricVariableInstance historicVariableInstance = list.get(i);
            if (historicVariableInstance.getVariableName().equals(key)) {
                variable = historicVariableInstance.getValue();
            }
        }
        return variable;
    }

    /**
     * 获取流程实例历史全部变量
     * @param processInsId 实例id
     * @return
     */
    public Map getHisVariableAll(String processInsId) {
        Map variable = new HashMap<>();
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInsId).list();

        for (int i = 0; i < list.size(); i++) {
            HistoricVariableInstance historicVariableInstance = list.get(i);
            variable.put(historicVariableInstance.getVariableName(),historicVariableInstance.getValue());
        }
        return variable;
    }
}
