package com.open.framework.schedule.listener;

import com.open.framework.commmon.enums.EnumBase;
import com.open.framework.commmon.utils.DateUtil;
import com.open.framework.commmon.utils.JsonUtil;
import com.open.framework.schedule.common.JobConstants;
import com.open.framework.schedule.common.JobResult;
import com.open.framework.schedule.entity.TaskDetail;
import com.open.framework.schedule.entity.TaskLog;
import com.open.framework.schedule.enums.JobEnum;
import com.open.framework.schedule.services.TaskDetailService;
import com.open.framework.schedule.services.TaskLogService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;

/**
 * @Description: 任务执行日志记录监听
 * @CreateDate: 2018/7/19 11:11
 * @Version: 1.0
 */
@Component
public class TaskLogJobListener extends DefaultJobListenerSupport {

    private static final Logger LOG = LoggerFactory.getLogger(TaskLogJobListener.class);

    private TaskLogService taskLogService;

    private TaskDetailService taskDetailService;

    private static final String LOG_ENABLE_KEY = "recordLog";

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    /**
     * Job执行完成后，记入任务日志执行记录
     *
     * @param context      Job上下文
     * @param jobException 异常信息
     */
    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        super.jobWasExecuted(context, jobException);
        //回写任务详情状态、上下次执行时间
        Object result = context.getResult();
        if (result instanceof JobResult) {
            String detailId = JobConstants.parseTaskId(context.getJobDetail().getKey().getName());
            TaskDetail detail = taskDetailService.findById(detailId);
            if (ObjectUtils.isEmpty(detail)) {
                LOG.error("回写任务详情失败:不存在ID为[{}]的任务", detailId);
                return;
            }
            detail.setExecLastTime(context.getFireTime());
            detail.setExecNextTime(context.getNextFireTime());
            JobResult.State state = ((JobResult) result).getState();
            switch (state) {
                case exception:
                    detail.setCurrentState(JobEnum.CurrentState.EXCEPTION.getCode());
                    break;
                case failed:
                    detail.setCurrentState(JobEnum.CurrentState.FAILED.getCode());
                    break;
                default:
                    detail.setCurrentState(JobEnum.CurrentState.WAITING.getCode());
            }
            taskDetailService.updateState(detail);
            //若启用日志记录，记录任务执行日志表
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            if (EnumBase.EnableDisable.enable.getVal().toString().equals(dataMap.getString(LOG_ENABLE_KEY))) {

                TaskLog log = new TaskLog();
                log.setResult(state.getCode());
                Serializable data = ((JobResult) result).getData();
                log.setMessage(ObjectUtils.isEmpty(data) ? null : JsonUtil.toJSONString(data));
                log.setDetailId(detailId);
                log.setExecStartTime(context.getFireTime());
                log.setExecEndTime(DateUtil.getNowDate());
                taskLogService.save(log);
            }


        }

        //没有else


    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        super.jobToBeExecuted(context);
        //调度启动，更新状态为执行中
        String detailId = JobConstants.parseTaskId(context.getJobDetail().getKey().getName());
        TaskDetail detail = taskDetailService.findById(detailId);
        if (ObjectUtils.isEmpty(detail)) {
            LOG.error("任务状态更新失败:不存在ID为[{}]的任务", detailId);
            return;
        }
        detail.setCurrentState(JobEnum.CurrentState.EXECUTING.getCode());
        taskDetailService.updateState(detail);
    }

    @Override
    protected void init() {
        if (taskLogService == null) {
            taskLogService = getBean(TaskLogService.class);
        }
        if (taskDetailService == null) {
            taskDetailService = getBean(TaskDetailService.class);
        }
    }
}
