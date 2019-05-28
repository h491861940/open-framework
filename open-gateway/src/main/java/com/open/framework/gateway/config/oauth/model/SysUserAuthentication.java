package com.open.framework.gateway.config.oauth.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 认证用户主体模型
 * @author wunaozai
 * @date 2018-06-19
 */
public class SysUserAuthentication  implements UserDetails {

    private static final long serialVersionUID = 2678080792987564753L;

    /**
     * ID号
     */
    private String uuid;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 账户生效
     */
    private boolean accountNonExpired;
    /**
     * 账户锁定
     */
    private boolean accountNonLocked;
    /**
     * 凭证生效
     */
    private boolean credentialsNonExpired;
    /**
     * 激活状态
     */
    private boolean enabled;
    /**
     * 权限列表
     */
    private Collection<GrantedAuthority> authorities;
    /**
     * ID号
     * @return uuid 
     */
    public String getUuid() {
        return uuid;
    }
    
    /**
     * ID号
     * @param uuid ID号
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    /**
     * 用户名
     * @return username 
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * 用户名
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * 密码
     * @return password 
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * 密码
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * 账户生效
     * @return accountNonExpired 
     */
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }
    
    /**
     * 账户生效
     * @param accountNonExpired 账户生效
     */
    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }
    
    /**
     * 账户锁定
     * @return accountNonLocked 
     */
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }
    
    /**
     * 账户锁定
     * @param accountNonLocked 账户锁定
     */
    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }
    
    /**
     * 凭证生效
     * @return credentialsNonExpired 
     */
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }
    
    /**
     * 凭证生效
     * @param credentialsNonExpired 凭证生效
     */
    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }
    
    /**
     * 激活状态
     * @return enabled 
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * 激活状态
     * @param enabled 激活状态
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    /**
     * 权限列表
     * @return authorities 
     */
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    /**
     * 权限列表
     * @param authorities 权限列表
     */
    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
    
}