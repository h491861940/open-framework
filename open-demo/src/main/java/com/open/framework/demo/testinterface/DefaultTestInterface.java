package com.open.framework.demo.testinterface;

/**
 * @Auther: hsj
 * @Date: 2019/4/20 14:13
 * @Description: 默认接口实现
 */
public class DefaultTestInterface implements TestInterface {
    @Override
    public void testFunction() {
        System.out.println("默认实现");
    }
}
