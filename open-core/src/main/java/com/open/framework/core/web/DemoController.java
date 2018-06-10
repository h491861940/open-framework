package com.open.framework.core.web;
import com.open.framework.core.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    public DemoService demoService;
    @RequestMapping("/getBodyParam")
    public @ResponseBody Map<String, Object> getBodyParam(@RequestBodyParam("code") String code1,
                                                          @RequestBodyParam("name") String name2){
        Map map=new HashMap();
        map.put("one",code1);
        map.put("two",name2);
        return map;
    }

    @RequestMapping("/getBodyParam1")
    public @ResponseBody Map<String, Object> getBodyParam1(@RequestBody String json){
        Map map=new HashMap();
        map.put("one",json);
        return map;
    }
    @RequestMapping("/testDs")
    public void testDs(){
        demoService.testDs();
    }
}
