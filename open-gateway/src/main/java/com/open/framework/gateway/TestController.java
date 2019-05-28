package com.open.framework.gateway;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/")
public class TestController {

    @RequestMapping(value="order/demo")
    public YYModel getDemo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth);
        YYModel yy = new YYModel();
        yy.setYy("中文");
        yy.setZz(3);
        return yy;
    }
    
    @GetMapping("/test")
    public String getTest() {
        YYModel yy = new YYModel();
        yy.setYy("中文");
        yy.setZz(3);
        return yy.toString();
    }
    @GetMapping("/login")
    public String login() {
        YYModel yy = new YYModel();
        yy.setYy("中文");
        yy.setZz(3);
        return yy.toString();
    }
}