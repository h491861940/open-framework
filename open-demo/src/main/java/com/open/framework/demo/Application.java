package com.open.framework.demo;

import com.open.framework.dao.dynamic.DynamicDataSourceRegister;
import com.open.framework.dao.repository.base.BaseRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ServletComponentScan
@ComponentScan({"com.open.framework"})
@Import(DynamicDataSourceRegister.class)//动态数据源注册
@EnableAspectJAutoProxy//切面配置
@EnableJpaRepositories(basePackages = {"com.open.framework"},
        repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class//指定自己的工厂类
)
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")//实体审计,写入新增人和修改人
@ImportResource(locations = { "classpath:druid-bean.xml" })
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}