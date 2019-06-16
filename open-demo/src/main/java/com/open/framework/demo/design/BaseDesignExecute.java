package com.open.framework.demo.design;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: hsj
 * @Date: 2019/6/4 22:39
 * @Description:
 */
@Component
public class BaseDesignExecute {

    @Autowired
    Map<String, BaseDesign> map = new HashMap<>();

    public void execute(String className, String str) {
        System.out.println(map.get(className).execute(str));
    }
}
