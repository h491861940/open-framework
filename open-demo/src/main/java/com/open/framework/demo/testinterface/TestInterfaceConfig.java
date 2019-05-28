package com.open.framework.demo.testinterface;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: hsj
 * @Date: 2019/4/20 14:14
 * @Description:测试接口没有实现的时候走默认实现
 */
@Configuration
public class TestInterfaceConfig {
    @Bean
    @ConditionalOnMissingBean
    public TestInterface getTestInterface(){
        TestInterface testInterface=new DefaultTestInterface();
        testInterface.testFunction();
        return testInterface;
    }
}
