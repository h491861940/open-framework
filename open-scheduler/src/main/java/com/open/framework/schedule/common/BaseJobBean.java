package com.open.framework.schedule.common;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.Serializable;

/**
 * @Author
 * @Description 任务调度基础类
 * @Date  2018-08-15 23:15:38
 **/
public abstract class BaseJobBean<R extends Serializable> extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		JobResult result = null;
		try {
			result = execute();
			if (result == null) {
				result = new JobResult();
			}
		} catch (Exception e) {
			result = new JobResult(JobResult.State.exception, e.getMessage());
			throw new JobExecutionException(e.getMessage());
		} finally {
			context.setResult(result);
		}
	}

	/**
	 * 任务执行体，由子类继承实现
	 *
	 * @return  result
	 * @throws JobExecutionException
	 */
	protected abstract JobResult<R> execute() throws JobExecutionException;
}
