package com.open.framework.schedule.services;


import com.open.framework.schedule.entity.TaskLog;
import com.open.framework.schedule.entity.TaskReg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**s
 * @Author hj
 * @Description 定时任务日志接口
 * @Date 2018-07-23 11:26:47
 **/
public interface TaskLogService {


    TaskLog save(TaskLog entity);
    /**
     * 按执行开始时间删除保留记录数外日志数据
     * 物理删除
     *
     * @param size 保留记录数
     * @return 删除记录数
     */
    int deleteOverRange(Integer size);

}
