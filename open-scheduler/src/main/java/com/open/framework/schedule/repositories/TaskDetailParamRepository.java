package com.open.framework.schedule.repositories;

import com.open.framework.dao.repository.base.BaseRepository;
import com.open.framework.schedule.entity.TaskDetailParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author hj
 * @Description 定时任务明细参数dao操作类
 * @Date  2018-07-23 11:26:47
 **/
public interface TaskDetailParamRepository extends JpaRepository<TaskDetailParam, String> {

}
