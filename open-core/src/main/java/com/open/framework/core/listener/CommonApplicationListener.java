package com.open.framework.core.listener;

import com.open.framework.core.startrun.StartRun;
import com.open.framework.core.startrun.dto.StartRunDTO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class CommonApplicationListener implements
        ApplicationListener<ContextRefreshedEvent> {
    public static List<StartRunDTO> runList=new ArrayList();
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            //只在初始化“根上下文”的时候执行
            final ApplicationContext app = event.getApplicationContext();
            if (null == app.getParent()) {
                Map<String,Object> beans = event.getApplicationContext().getBeansWithAnnotation(StartRun.class);
                    for (Map.Entry<String,Object> entry : beans.entrySet()) {
                        StartRun startRun = AnnotationUtils.findAnnotation(entry.getValue().getClass(), StartRun.class);
                        String[] methodNames=startRun.methodName();
                        for(String str:methodNames){
                            StartRunDTO startRunDTO=new StartRunDTO();
                            startRunDTO.setMethodName(str);
                            startRunDTO.setServiceName(entry.getKey());
                            startRunDTO.setSeq(startRun.seq());
                            if(startRun.seq()==0 || startRun.seq()>runList.size()){
                                runList.add(startRunDTO);
                            }else{
                                runList.add(startRun.seq(),startRunDTO);
                            }
                        }
                }
            }
        } catch (Exception e) {
        }
    }

}