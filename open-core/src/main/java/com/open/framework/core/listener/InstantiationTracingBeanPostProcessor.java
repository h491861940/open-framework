package com.open.framework.core.listener;

import com.open.framework.core.runner.StartRun;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;

@Component
public class InstantiationTracingBeanPostProcessor implements
        ApplicationListener<ContextRefreshedEvent> {
    public static Map<String,String> runMap=new TreeMap<String,String>();
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            //只在初始化“根上下文”的时候执行
            final ApplicationContext app = event.getApplicationContext();
            if (null == app.getParent()) {
                Map<String,Object> beans = event.getApplicationContext().getBeansWithAnnotation(StartRun.class);
                    for (Map.Entry<String,Object> entry : beans.entrySet()) {
                        StartRun startRun=  entry.getValue().getClass().getAnnotation(StartRun.class);
                        runMap.put(startRun.seq()+"-"+entry.getKey(),startRun.methodName());
                }
            }
        } catch (Exception e) {
        }
    }

}