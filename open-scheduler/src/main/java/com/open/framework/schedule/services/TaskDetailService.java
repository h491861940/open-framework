package com.open.framework.schedule.services;

import com.open.framework.schedule.entity.TaskDetail;

import java.util.List;

/**
 * @Author hj
 * @Description 定时任务明细接口
 * @Date  2018-07-23 11:26:47
 **/
public interface TaskDetailService {

    /**
     * @Author  hj
     * @Description 新增任务详情
     * @Date  2018-07-19 10:44:48
     * @Param TaskDetail:
     * @return
     **/
    TaskDetail save(TaskDetail entity);


    /**
     * @Author hj
     * @Description 根据gid批量删除
     * @Date  2018-07-19 10:46:05
     * @Param ids:
     * @return
     **/
    void delete(List<String> ids);

    /**
     * 注意，taskDetail的 startup 属性只在新增和启动操作时设置及变更，update操作时此属性不予修改
     * @param entity
     */
    void update(TaskDetail entity);
    /**
     * @Author hj
     * @Description  根据gid查找任务详情
     * @Date  2018-07-19 10:46:46
     * @Param gid:
     * @return
     **/
    TaskDetail findById(String gid);

    /**
     * 启动任务
     * @param taskId 任务id
     * @return
     */
    Boolean startUp(String taskId);
    /**
     * @Author hj
     * @Description 定时任务的暂停和恢复
     * @Date  2018-07-20 16:45:31
     * @Param taskId: 任务id
     * @Param state: 状态
     * @return
     **/
    Boolean changeState(String taskId, String state);
    /**
     * @Author hj
     * @Description  立即执行一次
     * @Date  2018-07-20 17:20:29
     * @param taskId
     * @return
     **/
    Boolean atonce(String taskId);
    /**
     * @Author hj
     * @Description 提供没有业务逻辑的save
     * @Date  2018-07-20 17:21:56
     * @param entity
     * @return
     **/
    void updateState(TaskDetail entity);

    /**
     *直接更新任务的cron表达式
     * @param taskId
     * @param cronExpression
     * @return
     */
    Boolean updateCronExp(String taskId, String cronExpression);
}
