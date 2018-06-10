package com.open.framework.commmon.licence;

import com.open.framework.commmon.BaseConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnProperty(name = BaseConstant.LICENCE_CHECK,havingValue = BaseConstant.LICENCE_CHECK_YES)
public class CheckLicenceConfig{

    @Bean
    public CheckLicenceUtil checkLicence() {
       System.out.println("进入校验");
       return  new CheckLicenceUtil();
    }
}
