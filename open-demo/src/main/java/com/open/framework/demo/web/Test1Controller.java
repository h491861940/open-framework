package com.open.framework.demo.web;

import com.open.framework.demo.service.Test2Service;
import com.open.framework.demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test1")
public class Test1Controller {
    @Autowired
    public Test2Service test2Service;
    @RequestMapping("/modify")
    public String modify() {
        test2Service.modify2();// 保存数据.
        return "ok.modify";
    }

}
