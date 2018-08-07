package com.open.framework.file.web;

import com.open.framework.file.dto.ExportData;
import com.open.framework.file.service.ExportService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Auther: hsj
 * @Date: 2018/7/22 17:13
 * @Description:
 */
@RestController
@RequestMapping("/base")
public abstract class ExportController {
    @Autowired
    ExportService exportService;
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public void export(@RequestBody ExportData exportData){
        exportService.export(exportData);
    }

}
