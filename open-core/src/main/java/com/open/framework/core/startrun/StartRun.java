package com.open.framework.core.startrun;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @StartRun(methodName = {"test","test1"}, seq = 4)
 * 类上面加的注解,用于在类上面,系统启动以后,调用想启动的方法,需要配置方法名seq是执行的顺序
 * 加方法上面,还需要扫类,所以直接扫类配置,目前只支持无参
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StartRun {
    String name() default "";
    String[] methodName();
    int seq() default 0;
}
