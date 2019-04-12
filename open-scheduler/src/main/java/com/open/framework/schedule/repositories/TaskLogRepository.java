package com.open.framework.schedule.repositories;

import com.open.framework.dao.repository.base.BaseRepository;
import com.open.framework.schedule.entity.TaskLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @Author hj
 * @Description 定时任务日志dao操作类
 * @Date 2018-07-23 11:26:47
 **/
public interface TaskLogRepository extends JpaRepository<TaskLog, String> {

    /**
     * 按执行开始时间删除保留记录数外日志数据
     * 物理删除
     *
     * @param size 保留记录数
     * @return 删除记录数
     */
    @Modifying
    @Query(value = "DELETE FROM QRTZ_TASK_LOG L" +
            " WHERE EXISTS (SELECT 1 FROM " +
            " (SELECT ID, ROW_NUMBER() OVER(PARTITION BY DETAIL_ID ORDER BY EXEC_START_TIME DESC) AS ROW_NO" +
            " FROM QRTZ_TASK_LOG) LL" +
            " WHERE L.ID = LL.ID" +
            " AND LL.ROW_NO > :size)", nativeQuery = true)
    int deleteOverRange(@Param("size") Integer size);


}
