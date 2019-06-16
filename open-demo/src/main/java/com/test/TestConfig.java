package com.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Auther: hsj
 * @Date: 2019/4/15 10:50
 * @Description: spring.factories里面配置的启动
 */
//@Configuration
@Import(TestB.class)
public class TestConfig {
    @Autowired
    TestB testB;
    @Bean
    public TestA getTestA(){
        System.out.println("初始化A");
        testB.getB();
        return new TestA();
    }
}
