package com.open.framework.file.web;

import com.open.framework.commmon.utils.ExcelUtil;
import com.open.framework.commmon.web.ExportData;
import com.open.framework.commmon.web.ImportData;
import com.open.framework.commmon.web.JsonResult;
import com.open.framework.file.service.ExportService;
import com.open.framework.file.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

/**
 * 基本导入导出
 */
@RestController
@RequestMapping("/excel")
public  class ExportController {

    @Autowired
    ExportService exportService;

    @Autowired
    ImportService importService;

    /**
     * 基本导出
     * @param exportData
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public void export(@RequestBody ExportData exportData){
        exportService.export(exportData);
    }

    /**
     * 根据excel解析文件,然后转换为和列对应的list<map>对象,然后如果有回调,调用回调,把导入的值传给实现,然后去处理数据,返回成功条数
     * @param file 导入的excel文件
     * @param columns  导入需要转换的列,顺序和excel一样
     * @param handleService 回调的service
     * @return
     */
    @RequestMapping(value = "/import", method=RequestMethod.POST)
    public JsonResult importExcel(@RequestParam(value="file") MultipartFile file, @RequestParam(value="columns",required = false) String[] columns, @RequestParam(value="handleService",required = false) String handleService){
        if(file.isEmpty()){
            return  JsonResult.error("文件不能为空");
        }
        if(ExcelUtil.isExcel2003(file.getOriginalFilename()) && ExcelUtil.isExcel2007(file.getOriginalFilename())){
            return  JsonResult.error("请导入excel文件");
        }
        ImportData importData=new ImportData();
        if(null!=columns && columns.length>0){
            //转换列
            importData.setColumns(Arrays.asList(columns));
        }
        //回调service
        importData.setHandleService(handleService);
        int result = importService.readExcelFile(file,importData);
        return  JsonResult.success("导入成功:"+result+"条");
    }

}
