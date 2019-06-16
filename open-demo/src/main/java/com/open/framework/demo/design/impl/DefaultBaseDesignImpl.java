package com.open.framework.demo.design.impl;

import com.open.framework.demo.design.BaseDesign;
import org.springframework.stereotype.Component;

/**
 * @Auther: hsj
 * @Date: 2019/6/4 22:40
 * @Description:
 */
@Component
public class DefaultBaseDesignImpl implements BaseDesign {
    @Override
    public String execute(String str) {
        return "我是老二"+str;
    }
}
