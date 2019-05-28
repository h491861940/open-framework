package com.open.framework.workflow.service;

import com.open.framework.commmon.utils.StringUtil;
import com.open.framework.workflow.api.dto.ProcessDefDTO;
import com.open.framework.workflow.api.dto.ProcessDefQueryDTO;
import com.open.framework.workflow.common.ConstantUtil;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: hsj
 * @Date: 2019/5/14 11:38
 * @Description: 流程定义的操作
 */

@Service
public class ActProcessDefService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    /**
     * 根据key启动流程
     *
     * @param processDefKey 流程定义key
     * @return
     */
    public String startDef(String processDefKey) {
        // 使用流程定义的key启动流程实例
        if(StringUtil.isNotEmpty(ConstantUtil.getCurrentUserName())){
            Authentication.setAuthenticatedUserId(ConstantUtil.getCurrentUserName());
        }
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefKey);
        runtimeService.setProcessInstanceName(pi.getId(),processDefKey);
        if(StringUtil.isNotEmpty(ConstantUtil.getCurrentUserName())){
            Authentication.setAuthenticatedUserId(null);
        }
        return pi.getId();
    }

    /**
     * 启动流程,设置启动人
     * @param processDefKey 流程定义key
     * @param userId 启动人
     * @return
     */
    public String startDefUser(String processDefKey, String userId) {
        Authentication.setAuthenticatedUserId(userId);
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefKey);
        runtimeService.setProcessInstanceName(pi.getId(),processDefKey);
        Authentication.setAuthenticatedUserId(null);
        return pi.getId();
    }

    /**
     * 根据key启动流程,带参数
     *
     * @param processDefKey 流程定义key
     * @param paramsMap 参数
     * @return
     */
    public String startDefMap(String processDefKey, Map paramsMap) {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefKey, paramsMap);
        return pi.getId();
    }

    /**
     * 流程定义id启动流程 设置启动流程的人和参数
     * @param processDefKey 流程定义key
     * @param paramsMap 参数
     * @param userId 启动人
     * @return
     */
    public String startDefMapUser(String processDefKey, Map paramsMap, String userId) {
        if (StringUtil.isNotEmpty(userId)) {
            Authentication.setAuthenticatedUserId(userId);
        }
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefKey, paramsMap);
        if (StringUtil.isNotEmpty(userId)) {
            Authentication.setAuthenticatedUserId(null);
        }
        return pi.getId();
    }

    /**
     * 流程定义id启动流程
     * @param processDefId 流程定义id
     * @return
     */
    public String startProcessDefId(String processDefId) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefId);
        return processInstance.getId();
    }

    /**
     * 流程id启动流程,设置启动人
     * @param processDefId 流程定义id
     * @param userId 启动人id
     * @return
     */
    public String startProcessDefIdUser(String processDefId, String userId) {
        if (StringUtil.isNotEmpty(userId)) {
            Authentication.setAuthenticatedUserId(userId);
        }
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefId);
        if (StringUtil.isNotEmpty(userId)) {
            Authentication.setAuthenticatedUserId(null);
        }
        return processInstance.getId();

    }

    /**
     * 启动流程携带参数
     * @param processDefId 流程定义id
     * @param paramsMap 参数
     * @return
     */
    public String startProcessDefIdMap(String processDefId, Map paramsMap) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefId, paramsMap);
        return processInstance.getId();

    }

    /**
     * 流程定义id启动流程,携带参数设置启动人
     * @param processDefId 流程定义id
     * @param paramsMap 参数
     * @param userId 启动人
     * @return
     */
    public String startProcessDefIdMapUser(String processDefId, Map paramsMap, String userId) {
        if (StringUtil.isNotEmpty(userId)) {
            Authentication.setAuthenticatedUserId(userId);
        }
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefId, paramsMap);
        if (StringUtil.isNotEmpty(userId)) {
            Authentication.setAuthenticatedUserId(null);
        }
        return processInstance.getId();
    }

    /**
     * 流程定义转换为流程定义DTO
     * @param list
     * @return
     */
    private List<ProcessDefDTO> getProcessDefDTO(List<ProcessDefinition> list) {
        List<ProcessDefDTO> dtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                ProcessDefDTO processDefDTO = new ProcessDefDTO(item.getId(), item.getName(), item.getDescription(),
                        item.getKey(), item.getVersion(), item.getCategory(),
                        item.getDeploymentId(), item.getResourceName(), item.getDiagramResourceName());
                dtoList.add(processDefDTO);
            });
        }
        return dtoList;
    }

    /**
     * 根据dto值不同,拼接不同的条件,查询流程定义dto
     * @param processDefQueryDTO
     * @return
     */
    public List<ProcessDefDTO> findByDto(ProcessDefQueryDTO processDefQueryDTO) {
        List<ProcessDefinition> list;
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        if (StringUtil.isNotEmpty(processDefQueryDTO.getProcessDefId())) {
            processDefinitionQuery.processDefinitionId(processDefQueryDTO.getProcessDefId());
        } else if (StringUtil.isNotEmpty(processDefQueryDTO.getDeploymentId())) {
            processDefinitionQuery.deploymentId(processDefQueryDTO.getDeploymentId());
        } else if (StringUtil.isNotEmpty(processDefQueryDTO.getProcessDefKey())) {
            processDefinitionQuery.processDefinitionKey(processDefQueryDTO.getProcessDefKey());
        }
        if (null != processDefQueryDTO.getFirstResult() && null != processDefQueryDTO.getMaxResults()) {
            list = processDefinitionQuery.listPage(processDefQueryDTO.getFirstResult(), processDefQueryDTO
                    .getMaxResults());
        } else {
            list = processDefinitionQuery.list();
        }
        List<ProcessDefDTO> dtoList = getProcessDefDTO(list);
        return dtoList;
    }
    public List<ProcessDefDTO> findByUserId(String userId,int page,int pageSize) {
        List<ProcessDefinition> list;
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().startableByUser(userId);
        if (page>=0 && pageSize>0) {
            if(page>0){
                page=page-1;
            }
            list = processDefinitionQuery.listPage(page, pageSize);
        } else {
            list = processDefinitionQuery.list();
        }
        List<ProcessDefDTO> dtoList = getProcessDefDTO(list);
        return dtoList;
    }

    /**
     * 获取全部数据
     *
     * @return
     */
    public List<ProcessDefDTO> getAllList() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        List<ProcessDefDTO> dtoList = getProcessDefDTO(list);
        return dtoList;
    }

    /**
     * 分页显示数据
     *
     * @param page
     * @param pageSize
     * @return
     */
    public List<ProcessDefDTO> getPageList(int page, int pageSize) {
        if(page>0){
            page=page-1;
        }
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().listPage(page,
                pageSize);
        List<ProcessDefDTO> dtoList = getProcessDefDTO(list);
        return dtoList;
    }

    /**
     * 根据部署id删除,级联删除 不管流程是否启动，都能可以删除
     *
     * @param deploymentId 部署id
     */

    public void deleteDeploymentCascade(String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
    }

    /**
     * 根据部署id删除,不带级联的删除 只能删除没有启动的流程，如果流程启动，就会抛出异常
     *
     * @param deploymentId 部署id
     */
    public void deleteByDeploymentId(String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, false);
    }

    /**
     * 删除流程定义key对应的流程
     * @param processDefKey 流程定义key
     */
    public void deleteAllByProcessDefKey(String processDefKey) {
        // 先使用流程定义的key查询流程定义，查询出所有的版本
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionKey
                (processDefKey)
                .list();
        // 遍历，获取每个流程定义的部署ID
        if (list != null && list.size() > 0) {
            for (ProcessDefinition pd : list) {
                // 获取部署ID
                String deploymentId = pd.getDeploymentId();
                deleteByDeploymentId(deploymentId);
            }
        }
    }

}
