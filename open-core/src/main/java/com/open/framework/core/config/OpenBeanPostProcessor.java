package com.open.framework.core.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
///BeanFactoryPostProcessor：BeanFactory级别的处理，是针对整个Bean的工厂进行处理
//BeanPostProcessor:bean级别的处理，针对某个具体的bean进行处理
//需要注意一点，我们定义一个类实现了BeanPostProcessor，默认是会对整个Spring容器中所有的bean进行处理。
// 既然是默认全部处理，那么我们怎么确认我们需要处理的某个具体的bean呢？
//可以看到方法中有两个参数。类型分别为Object和String，第一个参数是每个bean的实例，第二个参数是每个bean的name或者id属性的值。
// 所以我们可以第二个参数，来确认我们将要处理的具体的bean。
//Spring容器初始化bean大致过程      定义bean标签>将bean标签解析成BeanDefinition>调用构造方法实例化(IOC)>属性值得依赖注入(DI)
@Configuration
public class OpenBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("openEventListener")) {
            System.out.println("执行BeanPostProcessor的postProcessBeforeInitialization方法");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("openEventListener")) {
            System.out.println("执行BeanPostProcessor的postProcessAfterInitialization方法");
        }
        return bean;
    }
}