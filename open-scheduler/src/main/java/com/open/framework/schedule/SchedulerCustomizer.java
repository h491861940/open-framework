package com.open.framework.schedule;

import com.open.framework.schedule.listener.TaskLogJobListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSourceInitializer;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;

/**
 * 定制SchedulerFactoryBean
 *
 * @date :2018-07-17
 */
@Configuration
@EnableScheduling
public class SchedulerCustomizer implements SchedulerFactoryBeanCustomizer {

    @Autowired
    private AutowiringSpringBeanJobFactory autowiringSpringBeanJobFactory;


    @Autowired
    private TaskLogJobListener taskLogJobListener;

    @Override
    public void customize(SchedulerFactoryBean schedulerFactoryBean) {
        schedulerFactoryBean.setStartupDelay(60);
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setJobFactory(autowiringSpringBeanJobFactory);

        schedulerFactoryBean.setGlobalJobListeners(taskLogJobListener);
        //schedulerFactoryBean.setGlobalTriggerListeners(null);
        //schedulerFactoryBean.setSchedulerListeners(null);

        //schedulerFactoryBean.setDataSource(ds);
    }

    @Bean
    public QuartzDataSourceInitializer quartzDataSourceInitializer(
            DataSource dataSource, ResourceLoader resourceLoader,
            QuartzProperties properties) {
        return new QuartzDataSourceInitializerExt(dataSource, resourceLoader,
                properties);
    }
}