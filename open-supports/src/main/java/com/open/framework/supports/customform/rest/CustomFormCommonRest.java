package com.open.framework.supports.customform.rest;

import com.open.framework.commmon.web.JsonResult;
import com.open.framework.commmon.web.QueryParam;
import com.open.framework.supports.customform.dto.CustomFormCommonDTO;
import com.open.framework.supports.customform.service.CustomFormCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * customForm业务表单对外接口
 */
@RestController
@RequestMapping("/customForm")
public class CustomFormCommonRest {
    @Autowired
    private CustomFormCommonService customFormCommonService;

    @PostMapping("/save")
    public JsonResult save(@RequestBody CustomFormCommonDTO customFormCommonDTO) {
        return JsonResult.success(customFormCommonService.save(customFormCommonDTO));
    }

    @PostMapping("/modify")
    public JsonResult modify(@RequestBody CustomFormCommonDTO customFormCommonDTO) {
        customFormCommonService.modify(customFormCommonDTO);
        return JsonResult.success("操作成功");
    }
    @PostMapping("/deleteByIds")
    public JsonResult deleteByIds(@RequestBody List<String> gids) {
        customFormCommonService.deleteByIds(gids);
        return JsonResult.success("操作成功");
    }
    @GetMapping("/findById")
    public JsonResult findById(@RequestParam String gid) {
        return JsonResult.success(customFormCommonService.findById(gid));
    }

    @PostMapping("/query")
    public JsonResult query(@RequestBody QueryParam queryParam) {

        return JsonResult.success(null);
    }

}
