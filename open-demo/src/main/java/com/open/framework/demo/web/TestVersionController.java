package com.open.framework.demo.web;

import com.open.framework.core.web.version.ApiVersion;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/{version}/")
public class TestVersionController {

    @RequestMapping("hello/")
    @ApiVersion(1)
    @ResponseBody
    public String hello(HttpServletRequest request){
        System.out.println("haha1..........");

        return "hello";
    }

    @RequestMapping("hello/")
    @ApiVersion(2)
    @ResponseBody
    public String hello2(HttpServletRequest request){
        System.out.println("haha2.........");

        return "hello";
    }

    @RequestMapping("hello/")
    @ApiVersion(5)
    @ResponseBody
    public String hello5(HttpServletRequest request){
        System.out.println("haha5.........");

        return "hello";
    }

}
