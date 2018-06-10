package com.open.framework.dao.dynamic;

import com.alibaba.druid.pool.DruidDataSource;
import com.open.framework.commmon.BaseConstant;
import com.open.framework.commmon.utils.BeanUtil;
import com.open.framework.commmon.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源注册<br/>
 * 启动动态数据源请在启动类中（如SpringBootSampleApplication）添加 @Import(DynamicDataSourceRegister.class)<br/>
 * <p/>
 *
 * <b>配置示例：</b>
 * <pre>
 *     spring:
 *       datasource:
 *         type: com.alibaba.druid.pool.DruidDataSource
 *         driver-class-name: oracle.jdbc.driver.OracleDriver
 *         url: jdbc:oracle:thin:@host:1521:orcl
 *         username: ime
 *         password: ime
 *     open:
 *       datasource:
 *         multi:
 *           names: ds1,ds2
 *           ds1:
 *             type:com.alibaba.druid.pool.DruidDataSource
 *             driver-class-name: com.mysql.jdbc.Driver
 *             url: mysql://host:3306/schema?characterEncoding=utf8
 *             username: root
 *             password: root
 *           ds2:
 *             type: com.alibaba.druid.pool.DruidDataSource
 *             driver-class-name: oracle.jdbc.driver.OracleDriver
 *             url: jdbc:oracle:thin:@host:1521:orcl
 *             username: system
 *             password: system
 * </pre>
 *
 */
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    /**
     * The constant logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceRegister.class);

    /**
     * 默认数据源
     */
    private DataSource defaultDataSource;

    /**
     * 定制数据源集合
     */
    private Map<String, DataSource> customDataSources  = new HashMap<>();

    @Override
    public void setEnvironment(Environment environment) {
        //初始化主数据源
        initDefaultDataSource(environment);

        //初始化定制数据源
        initCustomDataSources(environment);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        // 将主数据源添加到更多数据源中
        targetDataSources.put("default", defaultDataSource);
        DynamicDataSourceContextHolder.dsKeys.add("default");

        // 添加更多数据源
        targetDataSources.putAll(customDataSources);
        for (String key : customDataSources.keySet()) {
            DynamicDataSourceContextHolder.dsKeys.add(key);
        }

        // 定义DynamicDataSource类型的BeanDefinition，用于创建指定spring bean对象
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);

        // 设置BeanDefinition属性，defaultTargetDataSource指向默认数据源，targetDataSources指向数据源集合
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        // 注册id为dataSource的数据源Bean对象到spring容器
        registry.registerBeanDefinition("dataSource", beanDefinition);
        logger.debug("动态数据源注册完毕");
    }

    /**
     * 初始化主数据源
     *
     * @param environment the environment
     */
    private void initDefaultDataSource(Environment environment) {
        defaultDataSource = bindDataSource(BaseConstant.PRIMARY_DS_PREFIX, environment);
    }

    /**
     * 初始化定制数据源
     *
     * @param environment the environment
     */
    private void initCustomDataSources(Environment environment) {
        String names = environment.getProperty(BaseConstant.MULTI_DS_PREFIX + ".names");
        if (names == null || names.length() < 1) {
            return;
        }
        for (String name : names.split(",")) {
            customDataSources.put(name.trim(), bindDataSource(BaseConstant.MULTI_DS_PREFIX + "." + name.trim(), environment));
        }
    }

    /**
     * 初始化数据源
     *
     * @param environment the env
     */
    private DataSource bindDataSource(String prefix, Environment environment) {
        if (StringUtil.isEmpty(prefix)) {
            return null;
        }
        DataSource result = BeanUtil.instantiateClass(DruidDataSource.class);
        //Spring Boot 2.0中增加了新的绑定API
        Binder binder = Binder.get(environment);
        return binder.bind(prefix, Bindable.ofInstance(result)).get();
    }

}
