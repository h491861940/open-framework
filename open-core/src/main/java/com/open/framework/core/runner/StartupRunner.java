package com.open.framework.core.runner;

import com.open.framework.core.listener.InstantiationTracingBeanPostProcessor;
import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 服务启动执行,扫描StartRun注解，然后执行这类型注解的类里面写的方法，默认不带参数
 *
 */
@Component
public class StartupRunner implements CommandLineRunner {

    @Override
    public void run(String...args) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>服务启动执行，执行加载数据等操作<<<<<<<<<<<<<");

        for (Map.Entry<String,String> entry : InstantiationTracingBeanPostProcessor.runMap.entrySet()) {
            String key=entry.getKey();
            String[] keys=key.split("-");
            String methodName=entry.getValue();
            String serviceName=null;
            if(null!=key && keys.length>0){
                serviceName=keys[1];
            }
            /*if(StringUtil.isNotEmpty(serviceName)){
                Object obj=  SpringUtil.getBean(serviceName);
                Method method=obj.getClass().getMethod(methodName);
                method.invoke(obj);
            }*/
        }
    }

}