package com.open.framework.schedule.controller;

import com.open.framework.commmon.utils.BeanUtil;
import com.open.framework.commmon.utils.JsonResultUtil;
import com.open.framework.commmon.web.JsonResult;
import com.open.framework.commmon.web.PageBean;
import com.open.framework.commmon.web.QueryParam;
import com.open.framework.core.service.BaseService;
import com.open.framework.schedule.dto.TaskDetailDTO;
import com.open.framework.schedule.entity.TaskDetail;
import com.open.framework.schedule.services.TaskDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author hj
 * @Description 任务明细控制器
 * @Date 2018-07-19 11:10:47
 * @return
 **/
@RestController
@RequestMapping("/scheduler/detail")
public class TaskDetailController {

    @Autowired
    private TaskDetailService service;
    @Autowired
    private BaseService<TaskDetail, TaskDetailDTO> baseService;

    /**
     * @return
     * @Author hj
     * @Description 任务明细实体
     * @Date 2018-07-19 11:12:22
     * @Param entity: 任务明细实体
     **/
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult save(@RequestBody TaskDetail entity) {
        return JsonResultUtil.success(service.save(entity).getGid());
    }


    /**
     * @return
     * @Author hj
     * @Description 任务明细类型删除
     * @Date 2018-07-19 11:12:22
     * @Param ids: 任务明细主键集合
     **/
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public JsonResult delete(@RequestBody List<String> ids) {
        service.delete(ids);
        return JsonResultUtil.success();
    }

    /**
     * @return
     * @Author hj
     * @Description 任务明细类型修改
     * @Date 2018-07-19 11:12:22
     * @Param entity: 任务明细实体
     **/
    @PutMapping
    public JsonResult update(@RequestBody TaskDetail entity) {
        service.update(entity);
        return JsonResultUtil.success();
    }

    /**
     * @return 任务类型实体
     * @Author hj
     * @Description 任务明细详情查询
     * @Date 2018-07-19 11:12:22
     * @Param id: 任务类型主键
     **/
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public JsonResult findById(@RequestParam String id) {
        return JsonResultUtil.success(service.findById(id));
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public JsonResult query(@RequestBody QueryParam queryParam) {
        Object obj = baseService.query(TaskDetailDTO.class, queryParam);
        if (obj instanceof PageBean) {
            return JsonResultUtil.successPage((PageBean) obj);
        } else {
            return JsonResultUtil.success(obj);
        }
    }


    /**
     * 启动或关闭任务
     *
     * @param taskId 任务id
     * @return
     */
    @PostMapping("/startUp/{taskId}")
    public Boolean startUp(@PathVariable(name = "taskId") String taskId) {
        return service.startUp(taskId);
    }

    /**
     * @return
     * @Author hj
     * @Description
     * @Date 2018-07-20 16:55:44
     * @Param 暂停任务和恢复任务:
     **/
    @PostMapping("/state/{taskId}/{state}")
    public Boolean changeState(@PathVariable(name = "taskId") String taskId, @PathVariable(name = "state") String
            state) {
        return service.changeState(taskId, state);
    }

    /**
     * @param taskId
     * @return java.lang.Boolean
     * @Author hj
     * @Description 立即执行方法
     * @Date 2018-07-20 17:30:08
     **/
    @PostMapping("/atonce/{taskId}")
    public Boolean atonce(@PathVariable(name = "taskId") String taskId) {
        return service.atonce(taskId);
    }

    /**
     * 直接更新cron表达式
     *
     * @param taskId
     * @param cronExpression
     * @return
     */
    @PutMapping("/{taskId}/{cronExpression}")
    public Boolean updateCronExp(@PathVariable(name = "taskId") String taskId, @PathVariable(name = "cronExpression")
            String cronExpression) {
        return service.updateCronExp(taskId, cronExpression);
    }


    /* *//**
     * 查询任务详情定时参数配置信息
     *
     * @param id 任务详情id
     * @return 任务详情定时参数配置信息
     *//*
    @GetMapping("/vo/{id}")
    public TaskDetailVo findVoById(@PathVariable("id") String id) {
        TaskDetail detail = service.findById(id);
        TaskDetailVo vo = new TaskDetailVo();
        if (detail != null) {
            List<TaskDetailParam> detailParams = detail.getTaskDetailParams();
            if (!CollectionUtils.isEmpty(detailParams)) {
                Map<String, String> paramMap = detailParams.stream().collect(HashMap::new, (m, v) -> m.put(v.getParam
                ().getCode(), v.getParamValue()), HashMap::putAll);
                TaskDetailVo param = JSON.parseObject(JSON.toJSONString(paramMap), TaskDetailVo.class);
                //复制系统参
                BeanUtil.copyProperties(param, vo);
                //处理用户参数
                List<TaskDetailParam> usrParams = detailParams.stream().filter(p -> JobEnum.Level.user.getVal()
                .equals(p.getParam().getLvl())).collect(Collectors.toList());
                vo.setParams(usrParams);
            }
            BeanUtil.copyProperties(detail, vo);
        }
        return vo;
    }*/
}
