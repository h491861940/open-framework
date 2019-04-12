package com.open.framework.schedule.controller;

import com.open.framework.core.web.BaseController;
import com.open.framework.schedule.dto.TaskLogDTO;
import com.open.framework.schedule.entity.TaskLog;
import com.open.framework.schedule.services.TaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 任务日志应用服务
 * @CreateDate: 2018/7/20 15:03
 * @Version: 1.0
 */
@RestController
@RequestMapping("/scheduler/log")
public class TaskLogController extends BaseController<TaskLog,TaskLogDTO> {

}
