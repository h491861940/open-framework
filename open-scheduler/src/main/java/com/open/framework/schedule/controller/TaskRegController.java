package com.open.framework.schedule.controller;

import com.open.framework.core.web.BaseController;
import com.open.framework.schedule.dto.TaskRegDTO;
import com.open.framework.schedule.entity.TaskReg;
import org.springframework.web.bind.annotation.*;

/**
 * @Author hj
 * @Description 任务注册控制器
 * @Date 2018-07-19 11:10:47
 * @return
 **/
@RestController
@RequestMapping("/scheduler/reg")
public class TaskRegController extends BaseController<TaskReg,TaskRegDTO> {

}
