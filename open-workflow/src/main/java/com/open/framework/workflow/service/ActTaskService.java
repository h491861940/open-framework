package com.open.framework.workflow.service;

import com.open.framework.workflow.api.dto.TaskDTO;
import com.open.framework.workflow.common.ConstantUtil;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: hsj
 * @Date: 2019/5/14 11:38
 * @Description: 任务的操作
 */
@Service
public class ActTaskService {

    @Autowired
    TaskService taskService;
    @Autowired
    HistoryService historyService;
    /**
     * 会签任务
     *
     * @param taskId 任务id
     */
    public void claimTask(String taskId) {
        claimTaskUserId(taskId, ConstantUtil.getCurrentUserName());
    }

    /**
     *  会签任务
     * @param taskId 任务id
     * @param userId 任务id
     */
    public void claimTaskUserId(String taskId,String userId) {
        taskService.claim(taskId, userId);
    }
    /**
     * 指派流程
     *
     * @param taskId
     * @param userOrGroupId
     * @param type
     */
    public void setTaskAssignee(String taskId, String userOrGroupId, String type) {
        if (type.equals("person")) {
            taskService.setAssignee(taskId, userOrGroupId);
        } else {
            taskService.unclaim(taskId);
            taskService.addCandidateGroup(taskId, userOrGroupId);
        }
    }

    /**
     * 查询全部的任务
     * @return
     */
    public List<TaskDTO> findAllTask() {
        List<Task> list = taskService.createTaskQuery().orderByTaskCreateTime().desc()
                .list();
        List<TaskDTO> dtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                TaskDTO taskDTO = new TaskDTO(item.getId(), item.getName(), item.getOwner(), item.getAssignee(), item
                        .getParentTaskId(), item.getDescription(), item.getCreateTime(), item.getDueDate()
                        , item.getExecutionId(), item.getProcessInstanceId(), item.getProcessDefinitionId(), item
                        .getTaskDefinitionKey(), item.getFormKey(), item.getClaimTime());
                dtoList.add(taskDTO);
            });
        }
        return dtoList;
    }

    /**
     * 查询
     *
     * @param assignee
     * @return
     */
    public List<TaskDTO> findUserTask(String assignee) {
        //没有查询用户属于组的,如果需要查询,需要加taskCandidateGroup(user.getGroup())
        List<Task> list = taskService.createTaskQuery().or()
                .taskAssignee(assignee).taskCandidateUser(assignee).endOr().orderByTaskCreateTime().desc()
                .list();
        List<TaskDTO> dtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                TaskDTO taskDTO = new TaskDTO(item.getId(), item.getName(), item.getOwner(), item.getAssignee(), item
                        .getParentTaskId(), item.getDescription(), item.getCreateTime(), item.getDueDate()
                        , item.getExecutionId(), item.getProcessInstanceId(), item.getProcessDefinitionId(), item
                        .getTaskDefinitionKey(), item.getFormKey(), item.getClaimTime());
                dtoList.add(taskDTO);
            });
        }
        return dtoList;
    }

    /**
     * 查询用户分页任务
     * @param assignee 人员
     * @param page
     * @param pageSize
     * @return
     */
    public List<TaskDTO> findUserTaskPage(String assignee,int page, int pageSize) {
        //没有查询用户属于组的,如果需要查询,需要加taskCandidateGroup(user.getGroup())
        if(page>0){
            page=page-1;
        }
        List<Task> list = taskService.createTaskQuery().or()
                .taskAssignee(assignee).taskCandidateUser(assignee).endOr().orderByTaskCreateTime().desc()
                .listPage(page,pageSize);
        List<TaskDTO> dtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                TaskDTO taskDTO = new TaskDTO(item.getId(), item.getName(), item.getOwner(), item.getAssignee(), item
                        .getParentTaskId(), item.getDescription(), item.getCreateTime(), item.getDueDate()
                        , item.getExecutionId(), item.getProcessInstanceId(), item.getProcessDefinitionId(), item
                        .getTaskDefinitionKey(), item.getFormKey(), item.getClaimTime());
                dtoList.add(taskDTO);
            });
        }
        return dtoList;
    }
    /**
     * 查询我完成的任务
     * @param assignee
     * @param page
     * @param pageSize
     * @return
     */
    public List<TaskDTO> findUserTaskPageFinished(String assignee,int page, int pageSize) {
        if(page>0){
            page=page-1;
        }
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().or()
                .taskAssignee(assignee).taskCandidateUser(assignee).endOr().finished().listPage(page,pageSize);
        List<TaskDTO> dtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (HistoricTaskInstance ht : list) {
                TaskDTO taskDTO=new TaskDTO(ht.getId(),ht.getName(),ht.getProcessInstanceId(),ht.getCreateTime(),ht.getEndTime(),ht.getDurationInMillis(),ht.getAssignee(),ht.getDueDate());
                dtoList.add(taskDTO);
            }
        }
        return dtoList;
    }

    /**
     * 查询我完成的任务
     * @param assignee
     * @return
     */
    public List<TaskDTO> findUserTaskFinished(String assignee) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().or()
                .taskAssignee(assignee).taskCandidateUser(assignee).endOr().finished().finished().list();
        List<TaskDTO> dtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (HistoricTaskInstance ht : list) {
                TaskDTO taskDTO=new TaskDTO(ht.getId(),ht.getName(),ht.getProcessInstanceId(),ht.getCreateTime(),ht.getEndTime(),ht.getDurationInMillis(),ht.getAssignee(),ht.getDueDate());
                dtoList.add(taskDTO);
            }
        }
        return dtoList;
    }


    /**
     * 根据流程实例id查询任务
     * @param ProcessInsId
     * @return
     */
    public List<TaskDTO> findTaskByProcessIns(String ProcessInsId) {
        List<Task> list = taskService.createTaskQuery().processInstanceId(ProcessInsId)
               .orderByTaskCreateTime().desc().list();
        List<TaskDTO> dtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                TaskDTO taskDTO = new TaskDTO(item.getId(), item.getName(), item.getOwner(), item.getAssignee(), item
                        .getParentTaskId(), item.getDescription(), item.getCreateTime(), item.getDueDate()
                        , item.getExecutionId(), item.getProcessInstanceId(), item.getProcessDefinitionId(), item
                        .getTaskDefinitionKey(), item.getFormKey(), item.getClaimTime());
                dtoList.add(taskDTO);
            });
        }
        return dtoList;
    }
    /**
     * 根据任务id完成任务
     * @param taskId 任务id
     */
    public void completeTask(String taskId) {
        taskService.complete(taskId);
    }

    /**
     * 提交任务,带参数
     * @param taskId
     * @param params
     */
    public void completeTaskMap(String taskId, Map params) {
        this.completeTaskMapAssignee(taskId, params, ConstantUtil.getCurrentUserName());
    }

    /**
     * 提交任务,带参数和任务完成人
     * @param taskId
     * @param map
     * @param assignee
     */
    public void completeTaskMapAssignee(String taskId, Map map, String assignee) {
        taskService.setAssignee(taskId, assignee);
        taskService.complete(taskId, map);
    }

    /**
     * 根据任务id获取任务
     * @param taskId 任务id
     * @return
     */
    public TaskDTO getTaskByTaskId(String taskId) {
        List<Task> tasks = taskService.createTaskQuery().taskId(taskId).list();
        if (tasks != null && tasks.size() > 0) {
            Task task = tasks.get(0);
            TaskDTO taskDTO = new TaskDTO(task.getId(), task.getName(), task.getOwner(), task.getAssignee(), task
                    .getParentTaskId(), task.getDescription(), task.getCreateTime(), task.getDueDate()
                    , task.getExecutionId(), task.getProcessInstanceId(), task.getProcessDefinitionId(), task
                    .getTaskDefinitionKey(), task.getFormKey(), task.getClaimTime());
            return taskDTO;
        }
       return null;
    }

    /**
     * 设置任务变量集合
     * @param taskId
     * @param params
     */
    public void setVariables(String taskId, Map params) {
        taskService.setVariables(taskId, params);
    }

    /**
     * 设置任务变量
     * @param taskId
     * @param key
     * @param value
     */
    public void setVariable(String taskId, String key, Object value) {
        taskService.setVariable(taskId, key, value);
    }

    /**
     * 获取任务变量的值
     * @param taskId
     * @param key
     * @return
     */
    public Object getVariable(String taskId, String key) {
        return taskService.getVariable(taskId, key);
    }

    /**
     * 获取任务全部参数
     * @param taskId
     * @return
     */
    public Map<String, Object> getVariables(String taskId) {
        return taskService.getVariables(taskId);
    }
    /**
     * 设置任务变量集合 当前任务
     * @param taskId
     * @param params
     */
    public void setVariablesLocal(String taskId, Map params) {
        taskService.setVariablesLocal(taskId, params);
    }

    /**
     * 设置任务变量 当前任务
     * @param taskId
     * @param key
     * @param value
     */
    public void setVariableLocal(String taskId, String key, Object value) {
        taskService.setVariableLocal(taskId, key, value);
    }

    /**
     * 获取任务变量的值 当前任务
     * @param taskId
     * @param key
     * @return
     */
    public Object getVariableLocal(String taskId, String key) {
        return taskService.getVariableLocal(taskId, key);
    }

    /**
     * 获取任务全部参数 当前任务
     * @param taskId
     * @return
     */
    public Map<String, Object> getVariablesLocal(String taskId) {
        return taskService.getVariablesLocal( taskId);
    }
}
