package com.open.framework.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping
    public String getUsers() {       
        return "Hello Spring Security";
    }
    @GetMapping("/test")
    public String test() {
        return "这是test";
    }
    @GetMapping("/admin")
    public String admin() {
        return "这是admin";
    }
}