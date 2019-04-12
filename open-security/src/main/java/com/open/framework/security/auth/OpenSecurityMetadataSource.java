package com.open.framework.security.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

@Component
public class OpenSecurityMetadataSource implements FilterInvocationSecurityMetadataSource, InitializingBean {


    private static final String AUTH_NO_ROLE = " __AUTH_NO_ROLE__";

    private ResourceService resourceService;

    public OpenSecurityMetadataSource(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    private void loadResourceDefine() {
        if (resourceMap == null) {
            resourceMap = new ConcurrentHashMap<String, Collection<ConfigAttribute>>();
        } else {
            resourceMap.clear();
        }

        Map<String, String> resourceRoleMap = resourceService.getAllResourceRole();

        for (Map.Entry<String, String> entry : resourceRoleMap.entrySet()) {
            String url = entry.getKey();
            String values = entry.getValue();

            Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
            ConfigAttribute configAttribute = new SecurityConfig(StringUtils.defaultString(values, AUTH_NO_ROLE));
            configAttributes.add(configAttribute);
            resourceMap.put(url, configAttributes);
        }
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        TreeMap<String, Collection<ConfigAttribute>> attrMap = new TreeMap<String, Collection<ConfigAttribute>>
                (resourceMap);
        Iterator<String> ite = attrMap.keySet().iterator();
        RequestMatcher urlMatcher = null;
        Collection<ConfigAttribute> attrSet = new HashSet<ConfigAttribute>();
        while (ite.hasNext()) {
            String resURL = ite.next();
            urlMatcher = new AntPathRequestMatcher(resURL);
            if (urlMatcher.matches(request) || StringUtils.equals(request.getRequestURI(), resURL)) {
                attrSet.addAll(attrMap.get(resURL));
            }
        }
        if (!attrSet.isEmpty()) {
            return attrSet;
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        loadResourceDefine();
    }

}