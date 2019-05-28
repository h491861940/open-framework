package com.open.framework.workflow.controller;

import com.open.framework.workflow.api.dto.HistoryTaskDTO;
import com.open.framework.workflow.service.ActHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Auther: hsj
 * @Date: 2019/5/28 16:13
 * @Description:
 */
public class ActHistoryController {
    @Autowired
    private ActHistoryService actHistoryService;


    @PostMapping("/getHisVariableAll")
    public Map getHisVariableAll(@RequestParam("processInsId") String processInsId) {
        return actHistoryService.getHisVariableAll(processInsId);
    }

    @PostMapping("/getTaskByProcessInsId")
    public List<HistoryTaskDTO> getTaskByProcessInsId(@RequestParam("processInsId") String processInsId) {
        return actHistoryService.getTaskByProcessInsId(processInsId);
    }

    @PostMapping("/getTaskByProcessInsIdParam")
    public List<HistoryTaskDTO> getTaskByProcessInsIdParam(@RequestParam("processInsId") String processInsId) {
        return actHistoryService.getTaskByProcessInsIdParam(processInsId);
    }

}
