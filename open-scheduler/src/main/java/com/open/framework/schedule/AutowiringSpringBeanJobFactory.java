package com.open.framework.schedule;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

/**
 * 定制Job工厂类
 *
 * @date :2018-07-18
 */
@Component
public class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {

	/**
	 * The Autowire capable bean factory.
	 */
	private AutowireCapableBeanFactory autowireCapableBeanFactory;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
	}

	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		Object job = super.createJobInstance(bundle);
		autowireCapableBeanFactory.autowireBean(job);
		return job;
	}
}
