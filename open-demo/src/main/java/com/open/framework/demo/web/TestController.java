package com.open.framework.demo.web;

import com.open.framework.demo.service.Test2Service;
import com.open.framework.demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    public TestService testService;
    @Autowired
    public Test2Service test2Service;
    @RequestMapping("/save")
    public String save() {
        testService.save();// 保存数据.
        return "ok.save";
    }

}
