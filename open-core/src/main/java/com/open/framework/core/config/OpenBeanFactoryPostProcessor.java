package com.open.framework.core.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;

import java.util.Iterator;
@Configuration
public class OpenBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    /**
     * 主要是用来自定义修改持有的bean
     * ConfigurableListableBeanFactory 其实就是DefaultListableBeanDefinition对象
     * @param beanFactory
     * @throws BeansException
     */

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("调用MergedBeanDefinitionPostProcessor了自定义的BeanFactoryPostProcessor 开始 " + beanFactory);
        System.out.println("所有bean的名次"+beanFactory.getBeanDefinitionNames());
      /*  String[] names = beanFactory.getBeanDefinitionNames();
        // 获取了所有的bean名称列表
        for(int i=0; i<names.length; i++){
            String name = names[i];
            BeanDefinition bd = beanFactory.getBeanDefinition(name);
            System.out.println(name + " bean properties: " + bd.getPropertyValues().toString());
        }*/
        System.out.println("调用了自定义的BeanFactoryPostProcessor 结束" );

    }
}