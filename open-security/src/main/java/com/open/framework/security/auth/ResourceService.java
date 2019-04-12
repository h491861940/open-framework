package com.open.framework.security.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;


@Component
public class ResourceService {


    /**
     * 获取各个资源（url）对应的角色
     *
     * @return
     */
    public Map<String, String> getAllResourceRole() {
        System.out.println("------开始加载菜单和角色数据-------");
        Map<String, String> resultMap = new HashMap();
        resultMap.put("/user/test", "test");
        resultMap.put("/user/admin", "admin");
        System.out.println("------结束加载菜单和角色数据-------");
        return resultMap;
    }

}