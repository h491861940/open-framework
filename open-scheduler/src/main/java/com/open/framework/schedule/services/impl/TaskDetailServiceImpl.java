package com.open.framework.schedule.services.impl;

import com.open.framework.commmon.enums.EnumBase;
import com.open.framework.commmon.exceptions.PlatformException;
import com.open.framework.schedule.SchedulerSupport;
import com.open.framework.schedule.entity.TaskDetail;
import com.open.framework.schedule.entity.TaskDetailParam;
import com.open.framework.schedule.entity.TaskReg;
import com.open.framework.schedule.enums.JobEnum;
import com.open.framework.schedule.repositories.TaskDetailRepository;
import com.open.framework.schedule.repositories.TaskRegRepository;
import com.open.framework.schedule.services.TaskDetailService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author hj
 * @Description 定时任务明细接口实现
 * @Date 2018-07-23 11:26:47
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskDetailServiceImpl implements TaskDetailService {

    @Autowired
    private TaskDetailRepository repository;

    @Autowired
    private TaskRegRepository taskRegRepository;


    @Autowired
    private SchedulerSupport support;


    @Override
    public TaskDetail save(TaskDetail entity) {
        //1，保存任务
        repository.save(entity);
        //2，如果任务启动
        if (EnumBase.CommonYN.yes.getVal().equals(entity.getStartup())) {
            addJob(entity);
        } else {
            //3,任务不启动

        }
        return entity;
    }


    @Override
    public void delete(List<String> ids) {
        repository.deleteInBatch(ids.stream().map(id -> {
            try {
                support.deleteJob(id);
            } catch (SchedulerException e) {
                throw new PlatformException(JobEnum.TaskCode.deleteJobException);
            }
            return new TaskDetail(id);
        }).collect(Collectors.toList()));
    }

    @Override
    public void update(TaskDetail entity) {
        //1,更新taskDetail
        repository.save(entity);
        try {
            //未启动，则不更新qurtz中的job,未启动task,没有job
            if (!EnumBase.CommonYN.yes.getVal().equals(entity.getStartup())) {
                return;
            }
            //2,更新job
            Map<String, String> map = parseParam(entity);
            support.updateJob(entity.getGid(), entity.getCronExpr(), map);
        } catch (SchedulerException e) {
            throw new PlatformException(JobEnum.TaskCode.modifyJobException, new String[]{"任务更新失败！"});
        }
    }

    @Override
    public TaskDetail findById(String gid) {
        return repository.findById(gid).orElse(null);
    }


    @Override
    public Boolean startUp(String taskId) {
        TaskDetail task = repository.findById(taskId).orElse(null);
        Assert.notNull(task, "不存在ID为[" + taskId + "]的任务");
        if (!EnumBase.CommonYN.yes.getVal().equals(task.getStartup())) {
            //当前taskDetail 还未启动，则启动
            addJob(task);
            //更新taskDetail 的startUp状态
            task.setStartup(EnumBase.CommonYN.yes.getVal());
            repository.save(task);
        } else {
            //已启动
        }
        return true;
    }

    /**
     * 解析TaskDetail中的参数，添加quartz的job
     *
     * @param entity
     */
    private void addJob(TaskDetail entity) {
        Class<?> jobClass;
        try {
            TaskReg reg = taskRegRepository.findById(entity.getRegId()).orElse(null);
            Assert.notNull(reg, String.format("当前任务还未注册"));
            entity.setReg(reg);
            Assert.notNull(reg.getFullClassName(), String.format("当前任务的注册全类名为空"));
            jobClass = Class.forName(entity.getReg().getFullClassName());
            //获取参数
            Map<String, String> param = parseParam(entity);
            //调用quartz，启动任务
            support.addJob(entity, jobClass, param);
        } catch (ClassNotFoundException e) {
            throw new PlatformException(JobEnum.TaskCode.addJobException, new String[]{String.format("找不到任务注册的类(%s)",
                    entity.getReg().getFullClassName())});
        } catch (SchedulerException e) {
            throw new PlatformException(JobEnum.TaskCode.addJobException);
        }
    }

    /**
     * 解析参数
     *
     * @param task
     * @return
     */
    Map<String, String> parseParam(TaskDetail task) {
        //用户级参数：Map<参数名，参数值>
        Map<String, String> jobParams = new HashMap<>();
        //1，当前任务是否记录日志，放入参数中
        jobParams.put("recordLog", task.getRecordLog() == null ? "0" : task.getRecordLog().toString());


        task.setCronExpr(task.getCronExpr());

        //2,用户级参数,放入JobDateMap中
        List<TaskDetailParam> taskDetailParams = task.getTaskDetailParams();
        for (TaskDetailParam taskDetailParam : taskDetailParams) {
            jobParams.put(taskDetailParam.getParamCode() != null ? taskDetailParam.getParamCode() : taskDetailParam
                    .getParamCode(), taskDetailParam.getParamValue());
        }
        return jobParams;
    }

    @Override
    public Boolean changeState(String taskId, String state) {
        TaskDetail task = repository.findById(taskId).orElse(null);
        Assert.notNull(task, "不存在ID为[" + taskId + "]的任务");
        if (JobEnum.CurrentState.PAUSED.getCode().equals(state)) {
            try {
                support.pauseJob(taskId);
            } catch (SchedulerException e) {
                throw new PlatformException(JobEnum.TaskCode.pauseJobException.getVal(), e.getMessage());
            }
        } else if (JobEnum.CurrentState.WAITING.getCode().equals(state)) {
            try {
                support.resumeJob(taskId);
            } catch (SchedulerException e) {
                throw new PlatformException(JobEnum.TaskCode.resumeJobException.getVal(), e.getMessage());
            }
        } else {
            throw new PlatformException(JobEnum.TaskCode.changeStateException);
        }
        task.setCurrentState(state);
        repository.save(task);
        return true;
    }

    @Override
    public Boolean atonce(String taskId) {
        try {
            support.runnowJob(taskId);
        } catch (SchedulerException e) {
            throw new PlatformException(JobEnum.TaskCode.atonceException);
        }
        return true;
    }

    @Override
    public void updateState(TaskDetail entity) {
        repository.save(entity);
    }

    @Override
    public Boolean updateCronExp(String taskId, String cronExpression) {
        try {
            support.updateJob(taskId, cronExpression);
        } catch (SchedulerException e) {
            throw new PlatformException(JobEnum.TaskCode.updateCronException);
        }
        return null;
    }
}
