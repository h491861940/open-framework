package com.open.framework.gateway.config.oauth;

import com.open.framework.gateway.config.oauth.auth.BaseUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring-Security 配置<br>
 * 具体参考: https://github.com/lexburner/oauth2-demo
 * http://blog.didispace.com/spring-security-oauth2-xjf-1/ 
 * https://www.cnblogs.com/cjsblog/p/9152455.html
 * https://segmentfault.com/a/1190000014371789 (多种认证方式)
 * @author wunaozai
 * @date 2018-05-28
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //启用方法级的权限认证
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    //通过自定义userDetailsService 来实现查询数据库，手机，二维码等多种验证方式
    @Bean
    @Override
    protected UserDetailsService userDetailsService(){
        //采用一个自定义的实现UserDetailsService接口的类
        return new BaseUserDetailService();
        /*
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String finalPassword = "{bcrypt}"+bCryptPasswordEncoder.encode("123456");
        manager.createUser(User.withUsername("user_1").password(finalPassword).authorities("USER").build());
        finalPassword = "{noop}123456";
        manager.createUser(User.withUsername("user_2").password(finalPassword).authorities("USER").build());
        return manager;
        */
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//            .antMatHandlerMethodArgumentResolverchers("/", "/index.html", "/oauth/**").permitAll() //允许访问
//            .anyRequest().authenticated() //其他地址的访问需要验证权限
//            .and()
//            .formLogin()
//            .loginPage("/login.html") //登录页
//            .failureUrl("/login-error.html").permitAll()
//            .and()
//            .logout()
//            .logoutSuccessUrl("/index.html");
        http.authorizeRequests().anyRequest().fullyAuthenticated();
        http.formLogin().loginPage("/login").failureUrl("/login?code=").permitAll();
        http.logout().permitAll();
        http.authorizeRequests().antMatchers("/oauth/authorize").permitAll();
    }
    
    /**
     * 用户验证
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }
    
    /**
     * Spring Boot 2 配置，这里要bean 注入
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager manager = super.authenticationManagerBean();
        return manager;
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}