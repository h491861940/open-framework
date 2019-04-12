package com.open.framework.core.startrun;

import com.open.framework.commmon.utils.SpringUtil;
import com.open.framework.commmon.utils.StringUtil;
import com.open.framework.core.listener.CommonApplicationListener;
import com.open.framework.core.startrun.dto.StartRunDTO;
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

        for (StartRunDTO startRunDTO : CommonApplicationListener.runList) {
            String methodName=startRunDTO.getMethodName();
            String serviceName=startRunDTO.getServiceName();
            if(StringUtil.isNotEmpty(serviceName)){
                Object obj=  SpringUtil.getBean(serviceName);
                Method method=obj.getClass().getMethod(methodName);
                method.invoke(obj);
            }
        }
    }

}