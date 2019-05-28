package com.open.framework.workflow.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.open.framework.commmon.utils.StringUtil;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

/**
 * @Auther: hsj
 * @Date: 2019/5/14 11:38
 * @Description: 和模版流程图有关的操作
 */
@Service
public class ActModelService {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;

    /**
     * 根据模版id部署
     * @param modelId
     * @return
     */
    public String deployByModelId(String modelId) {
        try {
            Model modelData = repositoryService.getModel(modelId);
            JsonNode modelNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            // 文件的名字
            String processName = modelData.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment().name(modelData.getName()).addBpmnModel
                    (processName, model).deploy();
            return deployment.getId();
        } catch (Exception e) {
            throw new RuntimeException("根据模型部署流程模版失败：" + modelId);
        }
    }

    /**
     * 根据流和文件名部署
     * @param input
     * @param fileName
     * @return
     */
    public String deployByFile(InputStream input,String fileName) {
        String key = fileName.substring(0, fileName.indexOf("."));
        Deployment deployment = repositoryService.createDeployment().name(key).key(key).addInputStream(fileName, input).deploy();
        return deployment.getId();
    }

    /**
     * 根据路径部署文件
     * @param path
     * @return
     */
    public String deployByFilePath(String path) {
        File file = new File(path);
        InputStream input = null;
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("文件不存在"+file.getName());
        }
        return deployByFile(input,file.getName());
    }

    /**
     * 获取流程图的图片
     * @param deploymentId
     * @return
     */
    public BufferedImage showPng(String deploymentId)  {
        // 获取图片资源名称
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery().deploymentId(deploymentId)
                .singleResult();
        if(null==processDefinition){
            throw new RuntimeException("部署id:"+deploymentId+"不存在");
        }
        BpmnModel bpmnModel = repositoryService
                .getBpmnModel(processDefinition.getId());
        BufferedImage bufferedImage = new DefaultProcessDiagramGenerator().generateImage(bpmnModel, "png", Collections
                .emptyList(), Collections.emptyList(), "宋体", "宋体", "宋体", null, 1.0, true);
        return bufferedImage;
    }

    /**
     * 读取带跟踪的图片
     *
     * @param processInsId 流程实例ID
     */
    public BufferedImage showFlow(String processInsId) {
        List<String> activeActivityIds = new ArrayList<String>();
        ProcessInstance processInstance = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(processInsId).singleResult();
        String processDefinitionId = null;
        if (processInstance == null) {
            HistoricProcessInstance historicProcessInstance = historyService
                    .createHistoricProcessInstanceQuery()
                    .processInstanceId(processInsId).singleResult();
            if(null==historicProcessInstance){
                throw new RuntimeException("实例id:"+processInsId+"不存在");
            }
            processDefinitionId = historicProcessInstance
                    .getProcessDefinitionId();
        } else {
            processDefinitionId = processInstance.getProcessDefinitionId();
        }
        if(StringUtil.isEmpty(processDefinitionId)){
            throw new RuntimeException("实例id:"+processInsId+"不存在");
        }
        BpmnModel bpmnModel = repositoryService
                .getBpmnModel(processDefinitionId);

        List<Execution> list = runtimeService.createExecutionQuery()
                .processInstanceId(processInsId).list();
        for (Execution execution : list) {
            List<String> temList = runtimeService
                    .getActiveActivityIds(execution.getId());
            activeActivityIds.addAll(temList);
        }

        BufferedImage bufferedImage = new DefaultProcessDiagramGenerator().generateImage(bpmnModel, "png",
                activeActivityIds, Collections.emptyList(), "宋体", "宋体", "宋体", null, 1.0, true);
        return bufferedImage;
    }
}
