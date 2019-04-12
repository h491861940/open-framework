package com.open.framework.schedule.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 默认的job监听器，对spring上下文感知
 *
 * @date :2018-08-02
 */
public abstract class DefaultJobListenerSupport extends JobListenerSupport implements ApplicationContextAware {

	private ApplicationContext ctx;

	private boolean initialized;

	protected void init() {

	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		if (!initialized) {
			init();
			initialized = true;
		}
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		if (!initialized) {
			init();
			initialized = true;
		}
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		if (!initialized) {
			init();
			initialized = true;
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
	}

	public <T> T getBean(Class<T> clazz) {
		if (clazz == null) {
			return null;
		}
		return ctx.getBean(clazz);
	}

	public Object getBean(String name) {
		if (name == null || name.length() < 1) {
			return null;
		}
		return ctx.getBean(name);
	}
}
