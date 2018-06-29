package com.open.framework.demo;

import com.open.framework.dao.dynamic.DynamicDataSourceRegister;
import com.open.framework.dao.repository.base.BaseRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAutoConfiguration
@ServletComponentScan
@ComponentScan("com.open.framework")
@Import(DynamicDataSourceRegister.class)//动态数据源注册
@EnableAspectJAutoProxy//切面配置
@EnableJpaRepositories(basePackages = {"com.open.framework"},
        repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class//指定自己的工厂类
)
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")//实体审计,写入新增人和修改人
public class Application {

    public static void main(String[] args) {
        SpringApplication springApplication =new SpringApplication(Application.class);
        springApplication.run(args);
    }
}