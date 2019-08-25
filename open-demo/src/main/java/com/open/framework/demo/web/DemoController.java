package com.open.framework.demo.web;

import com.open.framework.commmon.web.JsonResult;
import com.open.framework.core.web.param.RequestBodyParam;
import com.open.framework.demo.design.BaseDesignExecute;
import com.open.framework.demo.model.User;
import com.open.framework.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/demo")
public class DemoController {
    @Autowired(required = false)
    public DemoService demoService;
    @Autowired
    BaseDesignExecute demoOther;
    @RequestMapping("/getBodyParam")
    public  Map<String, Object> getBodyParam(@RequestBodyParam("code") String code1,
                                                          @RequestBodyParam("name") String name2){
        Map map=new HashMap();
        map.put("one",code1);
        map.put("two",name2);
        return map;
    }

    @RequestMapping(value = "/getBodyParam1")
    public  Map<String, Object> getBodyParam1(@RequestBody String json){
        Map map=new HashMap();
        map.put("one",json);
        return map;
    }
    @RequestMapping("/testEx1")
    public void testEx1(){
        demoService.testEx1();
    }
    @RequestMapping("/testEx2")
    public void testEx2(){
        demoService.testEx2();
    }
    @RequestMapping("/testEx3")
    public void testEx3(){
        demoService.testEx3();
    }
    @RequestMapping("/save")
    public String save() {
        User u = new User();
        u.setName("hsj");
        u.setPassword("123456");
        u.setLoginName("hsj");
        u.setLoginDate(new Date());
        demoService.save(u);// 保存数据.
        return "ok.save";
    }
    @RequestMapping("/query")
    public String query() {
        demoService.query();
        return "ok.query";
    }
    @RequestMapping("/testDao")
    public String testDao() {
        /*demoService.testListClassroomDto();
        demoService.testSort();
        demoService.testPage();
        demoService.testListClassroomStuNum();
        demoService.testListGoodStu();
        demoService.testListStu();
        demoService.testAddClassroom();

        demoService.listBySQL();*/
        //demoService.testDeleStu2();
        demoService.testPage();
        return "ok.query";
    }
    @RequestMapping("/testQuery")

    public JsonResult testQuery() {
        Page page=demoService.testQuery();
        return JsonResult.successPage(page);
    }
    @RequestMapping("/testJson")

    public JsonResult testJson() {
        User u = new User();
        u.setName("hsj");
        u.setPassword("123456");
        u.setLoginName("hsj");
        u.setLoginDate(new Date());
        return JsonResult.success(u);
    }
    @RequestMapping(value = "/testDesign")
    public  JsonResult testDesign(@RequestParam String className,@RequestParam String str){
        demoOther.execute(className,str);
        return JsonResult.success();
    }
    @RequestMapping(value = "/testAsync")
    public  JsonResult testAsync(){
        demoService.executeAsync();
        return JsonResult.success();
    }
    @RequestMapping(value = "/publishEvent")
    public  JsonResult publishEvent(){
        demoService.publishEvent();
        return JsonResult.success();
    }

}
