package com.open.framework.workflow.service.impl;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.open.framework.workflow.api.dto.ProcessDefDTO;
import com.open.framework.workflow.api.dto.ProcessInsDTO;
import com.open.framework.workflow.api.dto.TaskDTO;
import com.open.framework.workflow.service.*;
import org.flowable.engine.*;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


@Service
public class WorkFlowServiceImpl implements WorkFlowService {


    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private ActModelService actModelService;
    @Autowired
    private ActHistoryService actHistoryService;
    @Autowired
    private ActProcessDefService actProcessDefService;
    @Autowired
    private ActProcessInsService actProcessInsService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private IdentityService identityService;

    /**
     * 查询所有流程定义对象集合
     *
     * @return
     */
    @Override
    public List<ProcessDefDTO> getAllProcessDef() {
        return actProcessDefService.findByDto(null);
    }


    /**
     * 流程部署
     *
     * @param filePath 流程文件路径
     */
    @Override
    public String deploy(String filePath) {
        return actModelService.deployByFilePath(filePath);
    }

    /**
     * 根据 模版流和文件名部署
     * @param input 模版流
     * @param fileName 文件名
     * @return
     */
    @Override
    public String deploy(InputStream input, String fileName) {
        return actModelService.deployByFile(input,fileName);
    }

    /**
     * 根据流程定义key启动
     * @param processDefKey 流程定义key
     * @return
     */
    @Override
    public String startFlowKey(String processDefKey) {
        String instanceId = actProcessDefService.startDef(processDefKey);
        return instanceId;
    }
    /**
     * 根据流程定义KEY启动工作流
     *
     * @param processDefKey
     * @param paramsMap
     * @return
     */
    @Override
    public String startFlowKey(String processDefKey, Map paramsMap) {
        String instanceId = actProcessDefService.startDefMap(processDefKey, paramsMap);
        return instanceId;
    }

    /**
     * 根据流程定义key启动工作流,携带参数和启动人
     * @param processDefKey 流程定义key
     * @param paramsMap 参数
     * @param userId 启动人
     * @return
     */
    @Override
    public String startFlowKey(String processDefKey, Map paramsMap, String userId) {
        String instanceId = actProcessDefService.startDefMapUser(processDefKey, paramsMap,userId);
        return instanceId;
    }

    /**
     * 根据流程定义key启动流程设置启动人
     * @param processDefKey 流程定义key
     * @param userId 启动人
     * @return
     */
    @Override
    public String startFlowKey(String processDefKey,String userId) {
        String instanceId = actProcessDefService.startDefUser(processDefKey,userId);
        return instanceId;
    }

    /**
     * 根据流程定义id启动流程,设置启动人
     * @param processDefId 流程定义id
     * @param userId 启动人
     * @return
     */
    @Override
    public String startFlowDefId(String processDefId, String userId) {
        String instanceId = actProcessDefService.startProcessDefIdUser(processDefId,userId);
        return instanceId;
    }

    /**
     * 根据流程定义id启动流程,设置启动人,携带参数
     * @param processDefId 流程定义id
     * @param paramsMap 参数
     * @param userId 契丹人
     * @return
     */
    @Override
    public String startFlowDefId(String processDefId, Map paramsMap, String userId) {
        String instanceId = actProcessDefService.startProcessDefIdMapUser(processDefId,paramsMap,userId);
        return instanceId;
    }
    /**
     * 根据流程定义ID查询流程实例
     *
     * @param processDefId
     * @return
     */
    @Override
    public List<ProcessInsDTO> getInstanceByDefId(String processDefId) {
        return actProcessInsService.getInstanceByDefinedId(processDefId);
    }

    @Override
    public List<ProcessInsDTO> findIns(boolean haveTask) {
        return actProcessInsService.findAll(haveTask);
    }

    @Override
    public List<ProcessInsDTO> findInsByUser(String userId,boolean haveTask){
        return actProcessInsService.findAllByUser(userId,haveTask);
    }

    @Override
    public void setInsParam(String processInsId,String key, Object value) {
         actProcessInsService.setVariable(processInsId,key,value);
    }

    @Override
    public Object getInsParam (String key, String processInsId) {
        return actProcessInsService.getVariable(key,processInsId);
    }

    @Override
    public Map getInsParamAll (String processInsId) {
        return actProcessInsService.getVariableAll(processInsId);
    }

    @Override
    public Map getHisParamAll (String processInsId) {
        return actHistoryService.getHisVariableAll(processInsId);
    }

    /**
     * 根据用户,查询用户启动的流程实例
     * @param userId 启动人
     * @param page 开始记录数
     * @param pageSize 条数
     * @return
     */
    @Override
    public List<ProcessDefDTO> findByUserId(String userId, int page, int pageSize) {
        return actProcessDefService.findByUserId(userId,page,pageSize);
    }
    /**
     * 获取用户的所有任务(分页查询)
     *
     * @param assignee 用户id
     * @return
     */
    @Override
    public List<TaskDTO> findTask(String assignee) {
        return actTaskService.findUserTask(assignee);
    }

    @Override
    public void completeTask(String taskId) {
        actTaskService.completeTask(taskId);
    }

    @Override
    public void completeTask(String taskId, Map map) {
        actTaskService.completeTaskMap(taskId,map);
    }

    @Override
    public void completeTask(String taskId, Map map, String assignee) {
        actTaskService.completeTaskMapAssignee(taskId, map, assignee);
    }

    @Override
    public void setVariables(String taskId, Map map) {
        actTaskService.setVariablesLocal(taskId, map);
    }

    @Override
    public void setVariables(String taskId, String key, String value) {
        actTaskService.setVariableLocal(taskId,key,value);
    }

    @Override
    public Map<String, Object> getVariables(String taskId) {
        return actTaskService.getVariablesLocal(taskId);
    }

    @Override
    public Object getVariables(String taskId, String key) {
        return actTaskService.getVariableLocal(taskId,key);
    }

    /**
     * 获取组的所有任务(分页查询)
     *
     * @param groupId
     * @param page
     * @param pageSize
     * @return
     */
    public List<Task> getALLTaskGroupId(String groupId, int page,
                                        int pageSize) {
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateGroup(groupId).active().orderByTaskId().desc()
                .listPage(page, pageSize);
        return tasks;
    }

    /**
     * 获取组集合的所有任务(分页查询)
     *
     * @param groupIdList
     * @param page
     * @param pageSize
     * @return
     */
    public List<Task> getALLTaskGroupIdList(List<String> groupIdList,
                                            int page, int pageSize) {
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateGroupIn(groupIdList).active().orderByTaskId()
                .desc().listPage(page, pageSize);
        return tasks;
    }

    /**
     * 获取组集合的所有任务(不分页查询)
     *
     * @param groupIdList 角色GID的集合
     * @return
     */
    public List<Task> getALLTaskGroupIdList(List<String> groupIdList) {
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateGroupIn(groupIdList).active().orderByTaskId()
                .desc().list();
        return tasks;
    }
    @Override
    public String startFlowDef(String processDefId) {
       return actProcessDefService.startProcessDefId(processDefId);

    }
    @Override
    public String startFlowDef(String processDefId, Map variables) {
        return actProcessDefService.startProcessDefIdMap(processDefId,variables);
    }

    /**
     * 根据任务ID查找任务对象
     *
     * @param taskId
     * @return
     */
    @Override
    public TaskDTO getTask(String taskId) {
        return actTaskService.getTaskByTaskId(taskId);
    }

    /**
     * 会签任务
     * @param taskId 任务id
     */
    @Override
    public void claimTask(String taskId) {
        actTaskService.claimTask(taskId);
    }

    /**
     * 会签任务
     * @param taskId 任务id
     * @param userId 任务id
     */
    @Override
    public void claimTask(String taskId, String userId) {
        actTaskService.claimTaskUserId(taskId,userId);
    }

    /**
     * 根据流程实例ID查找当前的任务
     *
     * @param processInstId
     * @return
     */
    public List<TaskDTO> findTaskByProcessIns(String processInstId) {
        return actTaskService.findTaskByProcessIns(processInstId);
    }

    /**
     * 根据实例id返回单个任务
     * @param processInstId
     * @return
     */
    public TaskDTO findTaskSingle(String processInstId) {
        List<TaskDTO> list=actTaskService.findTaskByProcessIns(processInstId);
        if(!CollectionUtils.isEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据运行ID查找运行的参数MAP
     *
     * @param executionId
     * @return
     */
    public Map<String, Object> getVariablesByexecutionId(String executionId) {
        Map<String, Object> map = runtimeService.getVariables(executionId);
        return map;
    }
}
