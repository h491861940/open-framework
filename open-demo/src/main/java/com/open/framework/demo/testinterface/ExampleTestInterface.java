package com.open.framework.demo.testinterface;

import org.springframework.stereotype.Component;

/**
 * @Auther: hsj
 * @Date: 2019/4/20 14:13
 * @Description: 自定义接口实现
 */
//@Component
public class ExampleTestInterface implements TestInterface {
    @Override
    public void testFunction() {
        System.out.println("自定义实现");
    }
}
