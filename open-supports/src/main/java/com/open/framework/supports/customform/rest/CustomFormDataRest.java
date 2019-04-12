package com.open.framework.supports.customform.rest;

import com.open.framework.commmon.utils.JsonResultUtil;
import com.open.framework.commmon.utils.JsonUtil;
import com.open.framework.commmon.web.JsonResult;
import com.open.framework.commmon.web.QueryParam;
import com.open.framework.supports.customform.entity.CustomForm;
import com.open.framework.supports.customform.service.CustomFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * customForm元数据对外提供的接口
 */
@RestController
@RequestMapping("/customFormData")
public class CustomFormDataRest {
    @Autowired
    private CustomFormService customFormService;

    @PostMapping("/save")
    public JsonResult save(@RequestBody CustomForm customForm) {
        return JsonResultUtil.success(customFormService.save(customForm));
    }

    @PostMapping("/modify")
    public JsonResult modify(@RequestBody CustomForm customForm) {
         customFormService.modify(customForm);
         return JsonResultUtil.success("操作成功");
    }

    @GetMapping("/findById")
    public JsonResult findById(@RequestParam String gid) {
       return JsonResultUtil.success(customFormService.findById(gid));
    }

    @PostMapping("/findAll")
    public JsonResult findAll() {
       return JsonResultUtil.success(customFormService.findAll());
    }
    @GetMapping("/generateMenu")
    public JsonResult generateMenu(@RequestParam String gid) {
        return JsonResultUtil.success(customFormService.generateMenu(gid));
    }
    @GetMapping("/publishForm")
    public JsonResult publishForm(@RequestParam String gid) throws Exception {
        customFormService.publishForm(gid);
        return JsonResultUtil.success("发布成功");
    }

    @GetMapping("/getFieldType")
    public JsonResult getFieldType()  {
        return JsonResultUtil.success(customFormService.getFieldType());
    }
    @PostMapping("/query")
    public JsonResult query(@RequestBody QueryParam queryParam) {

        CustomForm condition = new CustomForm();
        Page<CustomForm> infos =null;// customFormService.findAll(condition, pageRequest);
        return JsonResultUtil.success(infos);
    }
}
