package com.open.framework.security.config;

import com.open.framework.security.auth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private OpenAccessDecisionManager accessDecisionManager;

    @Autowired
    private OpenSecurityMetadataSource securityMetadataSource;
    @Autowired
    private OpenAccessDeniedHandler openAccessDeniedHandler;


    /*@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password("123456").roles("USER");
    }*/
    // 自定义JpaFilterSecurityInterceptor过滤器
    public OpenSecurityFilter filter() throws Exception {
        OpenSecurityFilter filter = new OpenSecurityFilter();
        // 认证管理器，实现用户认证的入口
        //filter.setAuthenticationManager(getAuthenticationManager());
        // 访问决策器，决定某个用户具有的角色，是否有足够的权限去访问某个资源
        filter.setAccessDecisionManager(accessDecisionManager);
        // 资源源数据定义，即定义某一资源可以被哪些角色访问
        filter.setSecurityMetadataSource(securityMetadataSource);
        return filter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //加密方式
        //BCryptPasswordEncoder().encode("1111")生成新密码
        //passwordEncoder.matches("传入明文密码", "数据库加密密码")
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //设置登录,注销，表单登录不用拦截，其他请求要拦截
        http.addFilterBefore(filter(), FilterSecurityInterceptor.class).exceptionHandling().
                accessDeniedHandler(openAccessDeniedHandler).and().authorizeRequests().antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout().permitAll()
                .and()
                .formLogin().successHandler(new OpenAuthenctiationSuccessHandler()).failureHandler(new
                OpenAuthenctiationFailureHandler()).and()
                .csrf().disable();//关闭默认的csrf认证
        //session失效后跳转
        http.sessionManagement().invalidSessionUrl("/login");
        //只允许一个用户登录,如果同一个账户两次登录,那么第一个账户将被踢下线,跳转到登录页面
        http.sessionManagement().maximumSessions(1).expiredUrl("/login");

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //设置静态资源不要拦截
        web.ignoring().antMatchers("/js/**", "/cs/**", "/images/**");
    }
}