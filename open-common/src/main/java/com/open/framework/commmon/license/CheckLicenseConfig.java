package com.open.framework.commmon.license;

import com.open.framework.commmon.BaseConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnProperty(name = BaseConstant.LICENSE_CHECK,havingValue = BaseConstant.LICENSE_CHECK_YES)
public class CheckLicenseConfig {

    @Bean
    public CheckLicenseUtil checkLicence() {
       System.out.println("进入校验");
       return  new CheckLicenseUtil();
    }
}
