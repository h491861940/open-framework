package com.open.framework.dao.dynamic;

import java.util.Properties;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * 事物配置的加载在service方法上面增加事物
 */
@Configuration
public class TxConfigBean {
  @Autowired
  private PlatformTransactionManager transactionManager;
  // 创建事务通知
  @Bean(name = "txAdvice")
  public TransactionInterceptor getAdvisor() throws Exception {
    Properties properties = new Properties();
  /*  properties.setProperty("get*", "PROPAGATION_REQUIRED,-Exception,readOnly");
    properties.setProperty("query*", "PROPAGATION_REQUIRED,-Exception,readOnly");
    properties.setProperty("select*", "PROPAGATION_REQUIRED,-Exception,readOnly");
    properties.setProperty("find*", "PROPAGATION_REQUIRED,-Exception,readOnly");
  */
    properties.setProperty("save*", "PROPAGATION_REQUIRED,-Exception");
    properties.setProperty("add*", "PROPAGATION_REQUIRED,-Exception");
    properties.setProperty("insert*", "PROPAGATION_REQUIRED,-Exception");
    properties.setProperty("modify*", "PROPAGATION_REQUIRED,-Exception");
    properties.setProperty("update*", "PROPAGATION_REQUIRED,-Exception");
    properties.setProperty("delete*", "PROPAGATION_REQUIRED,-Exception");
    properties.setProperty("remove*", "PROPAGATION_REQUIRED,-Exception");
    properties.setProperty("*", "readOnly");
    TransactionInterceptor tsi = new TransactionInterceptor(transactionManager,properties);
    return tsi;
  }
  @Bean
  public BeanNameAutoProxyCreator txProxy() {
    BeanNameAutoProxyCreator creator = new BeanNameAutoProxyCreator();
    creator.setInterceptorNames("txAdvice");
    creator.setBeanNames("*Service", "*ServiceImpl");
    creator.setProxyTargetClass(true);
    creator.setOrder(2);
    return creator;
  }
}