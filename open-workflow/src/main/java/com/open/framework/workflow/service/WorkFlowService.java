package com.open.framework.workflow.service;



import com.open.framework.workflow.api.dto.ProcessDefDTO;
import com.open.framework.workflow.api.dto.ProcessInsDTO;
import com.open.framework.workflow.api.dto.TaskDTO;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @Auther: hsj
 * @Date: 2019/5/16 10:53
 * @Description: 工作流对外提供接口
 */
public interface WorkFlowService {
    /**
     * 根据文件名部署流程
     *
     * @param filePath 文件路径
     * @return
     */
    String deploy(String filePath);

    /**
     * 根据流部署
     *
     * @param input 模版流
     * @param fileName 文件名
     * @return
     */
    String deploy(InputStream input, String fileName);
    /**
     * 查询全部流程定义
     *
     * @return
     */
    List<ProcessDefDTO> getAllProcessDef();

    /**
     * 根据key,和参数,启动流程
     *
     * @param processDefKey 流程定义key
     * @param map 参数
     * @return
     */
    String startFlowKey(String processDefKey, Map map);

    /**
     * 根据key,启动流程
     *
     * @param processDefKey 流程定义key
     * @return
     */
    String startFlowKey(String processDefKey);
    /**
     * 根据流程定义id和参数启动流程
     * @param processDefId 流程定义id
     * @param map 参数
     * @return
     */
    String startFlowDef(String processDefId, Map map);

    /**
     * 根据流程id启动流程
     * @param processDefId 流程定义id
     * @return
     */
    String startFlowDef(String processDefId);
    /**
     * 根据流程定义key启动工作流,携带参数和启动人
     * @param processDefKey 流程定义key
     * @param paramsMap 参数
     * @param userId 启动人
     * @return
     */
    String startFlowKey(String processDefKey, Map paramsMap, String userId);
    /**
     * 根据流程定义key启动流程设置启动人
     * @param processDefKey 流程定义key
     * @param userId 启动人
     * @return
     */
    String startFlowKey(String processDefKey, String userId);
    /**
     * 根据流程定义id启动流程,设置启动人
     * @param processDefId 流程定义id
     * @param userId 启动人
     * @return
     */
    String startFlowDefId(String processDefId, String userId);
    /**
     * 根据流程定义id启动流程,设置启动人,携带参数
     * @param processDefId 流程定义id
     * @param paramsMap 参数
     * @param userId 契丹人
     * @return
     */
    String startFlowDefId(String processDefId, Map paramsMap, String userId);
    /**
     * 根据流程定义ID查询流程实例
     *
     * @param processDefId 流程定义ID
     * @return
     */
    List<ProcessInsDTO> getInstanceByDefId(String processDefId);

    /**
     * 获取全部的流程实例
     * @param haveTask 是否包含任务 true包含
     * @return
     */
    List<ProcessInsDTO> findIns(boolean haveTask);

    /**
     * 获取全部的流程实例
     * @param userId 启动人id
     * @param haveTask 是否包含任务 true包含
     * @return
     */
    List<ProcessInsDTO> findInsByUser(String userId, boolean haveTask);

    /**
     * 设置流程实例的变量
     * @param processInsId 流程实例Id
     * @param key 参数名称
     * @param value 值
     */
    void setInsParam(String processInsId, String key, Object value);

    /**
     * 得到流程实例的变量
     * @param key 参数名称
     * @param processInsId 流程实例Id
     * @return
     */
    Object getInsParam(String key, String processInsId);

    /**
     * 得到流程实例的全部变量
     * @param processInsId 流程实例Id
     * @return
     */
    Map getInsParamAll(String processInsId);

    /**
     * 得到流程实例的历史全部变量
     * @param processInsId 流程实例Id
     * @return
     */
    Map getHisParamAll(String processInsId);

    /**
     * 根据用户,查询用户启动的流程实例
     * @param userId 启动人
     * @param page 开始记录数
     * @param pageSize 条数
     * @return
     */
    List<ProcessDefDTO> findByUserId(String userId, int page, int pageSize);

    /**
     * 根据个人查询任务
     *
     * @param userId 用户id
     * @return
     */
    List<TaskDTO> findTask(String userId);

    /**
     * 根据任务id获取任务
     * @param taskId 任务id
     * @return
     */
    TaskDTO getTask(String taskId);
    /**
     * 根据任务id获取任务
     * @param taskId 任务id
     * @return
     */
    void claimTask(String taskId);

    /**
     *  会签任务
     * @param taskId 任务id
     * @param userId 任务id
     */
    void claimTask(String taskId, String userId);
    /**
     * 根据任务id,提交任务
     *
     * @param taskId 任务id
     */
    void completeTask(String taskId);

    /**
     * 根据任务id,参数提交任务
     *
     * @param taskId 任务id
     * @param map 参数
     */
    void completeTask(String taskId, Map map);

    /**
     * 根据任务id,参数和人提交任务
     *
     * @param taskId 任务id
     * @param map 参数
     * @param assignee 人员id
     */
    void completeTask(String taskId, Map map, String assignee);

    /**
     * 设置任务的参数
     *
     * @param taskId 任务id
     * @param map 参数
     */
    void setVariables(String taskId, Map map);

    /**
     *
     * @param taskId 任务id
     * @param key 参数名
     * @param value 参数值
     */
    void setVariables(String taskId, String key, String value);
    /**
     * 根据任务id获取参数
     * @param taskId 任务id
     * @return
     */
    Map<String, Object> getVariables(String taskId);

    /**
     *
     * @param taskId 任务id
     * @param key 参数名
     * @return
     */
    Object  getVariables(String taskId, String key);
}
