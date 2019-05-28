package com.open.framework.workflow.listener;

import java.util.Map;

import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;


public class GlobalTaskListener implements TaskListener
{
    private Expression mestarService;
    private Expression mestarMethod;

    @Override
    public void notify(DelegateTask delegateTask)
    {
        String serverName = mestarService.getValue(delegateTask).toString();// 得到server名称
        String serverMethod = mestarMethod.getValue(delegateTask).toString();// 得到service的方法名称
        Map map = delegateTask.getVariables();// 得到任务提交以后的变量
        System.out.println("触发了全局监听器, pid={}, tid={}, event={}");

    }



}
