package com.open.framework.core.service;

import com.open.framework.core.runner.StartRun;
import com.open.framework.dao.dynamic.ChangeDs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
@StartRun(methodName = "test", seq = 4)
public class DemoService {
    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;
    @Autowired(required = false)
    private DemoService demoService;
    public void test() {
        System.out.println("我是第一个");
    }

    public void testDs() {
        demoService.testDs1();
        demoService.testDs2();
        demoService.testDs3();
    }

    public void testDs1() {
        String sql = "select * from sm_code_rule_reg";
        List list = jdbcTemplate.queryForList(sql);
        System.out.println("默认数据源" + list);
    }

    @ChangeDs(key = "ds1")
    public void testDs2() {
        String sql = "select * from demo";
        List list = jdbcTemplate.queryForList(sql);
        System.out.println("数据源1" + list);

    }

    @ChangeDs(key = "ds2")
    public void testDs3() {
        String sql = "select * from mts_button";
        List list = jdbcTemplate.queryForList(sql);
        System.out.println("数据源2" + list);
    }
}
