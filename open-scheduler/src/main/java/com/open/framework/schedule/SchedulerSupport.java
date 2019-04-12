package com.open.framework.schedule;

import com.open.framework.schedule.common.JobConstants;
import com.open.framework.schedule.entity.TaskDetail;
import com.open.framework.schedule.repositories.TaskDetailRepository;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @Author hsj
 * @Description 定时任务操作工具类
 * @Date  2018-08-18 18:02:03
 **/
@Service
public class SchedulerSupport implements JobConstants {

	private static final Logger LOG = LoggerFactory.getLogger(SchedulerSupport.class);

	/**
	 * The constant JOB_DESC_TPL.
	 */
	private static final String JOB_DESC_TPL = "[%s]%s";

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	@Autowired
	private TaskDetailRepository taskDetailRepository;

	/**
	 * Add job.
	 *
	 * @param task		required	the task
	 * @param jobClass	required	the job class
	 * @param jobParams optional	the job params
	 * @throws SchedulerException the scheduler exception
	 */
	public void addJob(TaskDetail task, Class jobClass, Map<String, String> jobParams) throws SchedulerException {
		Assert.notNull(task, "任务为空");
		Assert.notNull(task.getGid(), "任务ID为空");
		Assert.notNull(task.getName(), "任务名称为空");
		Assert.notNull(task.getGroupName(), "任务组为空");
		Assert.notNull(task.getCronExpr(), "任务Cron表达式为空");

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(
				String.format(KEY_NAME_TPL, task.getGid()),
				String.format(KEY_GROUP_TPL, task.getGroupName())
		);
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		if (null == trigger) {
			JobBuilder jobBuilder = JobBuilder.newJob(jobClass);
			if (jobParams != null && jobParams.size() > 0) {
				jobBuilder.setJobData(new JobDataMap(jobParams));
			}
			JobDetail jobDetail = jobBuilder.withDescription(String.format(JOB_DESC_TPL, task.getName(),
					Optional.ofNullable(task.getDescription()).orElse("")))
					.withIdentity(
							String.format(KEY_NAME_TPL, task.getGid()),
							String.format(KEY_GROUP_TPL, task.getGroupName())
					).build();

			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpr());

			trigger = TriggerBuilder
					.newTrigger()
					.withIdentity(String.format(KEY_NAME_TPL, task.getGid()), String.format(KEY_GROUP_TPL, task.getGroupName()))
					.withSchedule(scheduleBuilder).build();
			scheduler.scheduleJob(jobDetail, trigger);
		} else {
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpr());
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
			scheduler.rescheduleJob(triggerKey, trigger);
		}
	}

	/**
	 * Pause job.
	 *
	 * @param gid the gid
	 * @throws SchedulerException the scheduler exception
	 */
	public void pauseJob(String gid) throws SchedulerException {
		TaskDetail task = findTaskById(gid);
		Assert.notNull(task, "不存在ID为[" + gid + "]的任务");

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(String.format(KEY_NAME_TPL, task.getGid()), String.format(KEY_GROUP_TPL, task.getGroupName()));
		scheduler.pauseJob(jobKey);
	}

	/**
	 * Pause job group.
	 *
	 * @param group the group
	 * @throws SchedulerException the scheduler exception
	 */
	public void pauseJobGroup(String group) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group)).forEach(jobKey -> {
			try {
				scheduler.pauseJob(jobKey);
			} catch (SchedulerException e) {
				LOG.error("暂停定时任务出错，分组[{}]", group);
			}
		});
	}

	/**
	 * Resume job.
	 *
	 * @param gid the gid
	 * @throws SchedulerException the scheduler exception
	 */
	public void resumeJob(String gid) throws SchedulerException {
		TaskDetail task = findTaskById(gid);
		Assert.notNull(task, "不存在ID为["+gid+"]的任务");

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(String.format(KEY_NAME_TPL, task.getGid()), String.format(KEY_GROUP_TPL, task.getGroupName()));
		scheduler.resumeJob(jobKey);
	}

	/**
	 * Resume job group.
	 *
	 * @param group the group
	 * @throws SchedulerException the scheduler exception
	 */
	public void resumeJobGroup(String group) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group)).forEach(jobKey -> {
			try {
				scheduler.resumeJob(jobKey);
			} catch (SchedulerException e) {
				LOG.error("恢复定时任务出错，分组[{}]", group);
			}
		});
	}

	/**
	 * Delete job.
	 *
	 * @param gid the gid
	 * @throws SchedulerException the scheduler exception
	 */
	public void deleteJob(String gid) throws SchedulerException {
		TaskDetail task = findTaskById(gid);
		Assert.notNull(task, "不存在ID为["+gid+"]的任务");

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(String.format(KEY_NAME_TPL, task.getGid()), String.format(KEY_GROUP_TPL, task.getGroupName()));
		scheduler.deleteJob(jobKey);
	}


	/**
	 * Delete job group.
	 *
	 * @param group the group
	 * @throws SchedulerException the scheduler exception
	 */
	public void deleteJobGroup(String group) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group)).forEach(jobKey -> {
			try {
				scheduler.deleteJob(jobKey);
			} catch (SchedulerException e) {
				LOG.error("删除定时任务出错，分组[{}]", group);
			}
		});
	}

	/**
	 * Runnow job.
	 *
	 * @param gid the gid
	 * @throws SchedulerException the scheduler exception
	 */
	public void runnowJob(String gid) throws SchedulerException {
		TaskDetail task = findTaskById(gid);
		Assert.notNull(task, "不存在ID为["+gid+"]的任务");

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(String.format(KEY_NAME_TPL, task.getGid()), String.format(KEY_GROUP_TPL, task.getGroupName()));
		scheduler.triggerJob(jobKey);
	}

	/**
	 * Runnow job group.
	 *
	 * @param group the group
	 * @throws SchedulerException the scheduler exception
	 */
	public void runnowJobGroup(String group) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group)).forEach(jobKey -> {
			try {
				scheduler.triggerJob(jobKey);
			} catch (SchedulerException e) {
				LOG.error("触发定时任务出错，分组[{}]", group);
			}
		});
	}

	/**
	 * Update job.
	 *
	 * @param gid      the gid
	 * @param cronExpr the cron expr
	 * @throws SchedulerException the scheduler exception
	 */
	public void updateJob(String gid, String cronExpr) throws SchedulerException {
		TaskDetail task = findTaskById(gid);
		Assert.notNull(task, "不存在ID为["+gid+"]的任务");

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(String.format(KEY_NAME_TPL, task.getGid()), String.format(KEY_GROUP_TPL, task.getGroupName()));
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpr);
		trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
		scheduler.rescheduleJob(triggerKey, trigger);
	}

	/**
	 * Update job with Param and Cron
	 * @param gid
	 * @param param
	 * @throws SchedulerException
	 */
	public void updateJob(String gid, String cronExpr, Map param) throws SchedulerException {
		TaskDetail task = findTaskById(gid);
		Assert.notNull(task, "不存在ID为["+gid+"]的任务");

		Scheduler scheduler = schedulerFactoryBean.getScheduler();

		TriggerKey triggerKey = TriggerKey.triggerKey(String.format(KEY_NAME_TPL, task.getGid()), String.format(KEY_GROUP_TPL, task.getGroupName()));
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpr);
		trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

		JobKey jobKey = JobKey.jobKey(String.format(KEY_NAME_TPL, task.getGid()), String.format(KEY_GROUP_TPL, task.getGroupName()));
		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		jobDetail.getJobDataMap().putAll(param);

		Set<Trigger> set = new HashSet<>();
		set.add(trigger);

		scheduler.scheduleJob(jobDetail,set,true);
	}

	/**
	 * Update job group.
	 *
	 * @param group    the group
	 * @param cronExpr the cron expr
	 * @throws SchedulerException the scheduler exception
	 */
	public void updateJobGroup(String group, String cronExpr) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(group)).forEach(triggerKey -> {
			try {
				CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpr);
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
				scheduler.rescheduleJob(triggerKey, trigger);
			} catch (SchedulerException e) {
				LOG.error("更新定时任务出错，分组[{}]", group);
			}
		});
	}

	/**
	 * Find task by id task detail.
	 *
	 * @param gid the gid
	 * @return the task detail
	 */
	private TaskDetail findTaskById(String gid) {
		return taskDetailRepository.findById(gid).orElse(null);
	}
}
