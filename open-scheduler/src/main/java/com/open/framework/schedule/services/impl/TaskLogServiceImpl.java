package com.open.framework.schedule.services.impl;

import com.open.framework.schedule.entity.TaskLog;
import com.open.framework.schedule.repositories.TaskLogRepository;
import com.open.framework.schedule.services.TaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author hj
 * @Description 定时任务日志接口实现
 * @Date 2018-07-23 11:26:47
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskLogServiceImpl implements TaskLogService {

    @Autowired
    private TaskLogRepository repository;

    @Override
    public TaskLog save(TaskLog entity) {
        return repository.save(entity);
    }
    @Override
    public int deleteOverRange(Integer size) {
        List<TaskLog> taskLogs = repository.findAll(Sort.by(Sort.Direction.DESC, "execStartTime"));
        int removeSize = 0;
        if (!CollectionUtils.isEmpty(taskLogs)) {
            Map<String, List<TaskLog>> taskLogMap = taskLogs.stream().collect(Collectors.groupingBy(TaskLog::getDetailId));
            for (List<TaskLog> v : taskLogMap.values()) {
                if (v.size() > size) {
                    removeSize += (v.size() - size);
                    repository.deleteAll(v.subList(size, v.size()));
                }
            }

        }
        return removeSize;
    }


}
