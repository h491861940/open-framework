package com.open.framework.core;

import com.open.framework.dao.dynamic.DynamicDataSourceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableAutoConfiguration
@ServletComponentScan
@ComponentScan("com.open.framework")
@Import(DynamicDataSourceRegister.class)
@EnableAspectJAutoProxy
public class Application {

    public static void main(String[] args) {
        SpringApplication springApplication =new SpringApplication(Application.class);
        springApplication.run(args);
    }
}