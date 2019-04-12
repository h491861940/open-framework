package com.open.framework.schedule;

import com.open.framework.schedule.common.BaseJobBean;
import com.open.framework.schedule.common.JobResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Auther: hsj
 * @Date: 2018/7/23 16:16
 * @Description:
 */
public class TestJob extends BaseJobBean<String> {


    @Override
    protected JobResult<String> execute()  {
        System.out.println("开始调度了");
        return new JobResult("成功");
    }
}
