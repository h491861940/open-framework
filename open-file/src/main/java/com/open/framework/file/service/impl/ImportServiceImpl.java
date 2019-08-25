package com.open.framework.file.service.impl;

import com.open.framework.commmon.utils.ExcelUtil;
import com.open.framework.commmon.web.ImportData;
import com.open.framework.file.service.ImportHandlService;
import com.open.framework.file.service.ImportService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
/**
 * @description: 导入处理serice
 * @author: hsj
 * @date: 2019-08-25 16:58:25
 */
@Service
public class ImportServiceImpl implements ImportService {
    @Autowired(required = false)
    Map<String,ImportHandlService> handlMap;
    @Override
    public int readExcelFile(MultipartFile file, ImportData importData) {
        //解析excel，获取上传的事件单
        List<Map<String, Object>> resultList = ExcelUtil.getExcelInfo(file,importData);
        if(StringUtils.isNotEmpty(importData.getHandleService())){
            //如果有回调,判断是否有实现
            if(!handlMap.isEmpty()){
                //获取传入的实现
                ImportHandlService importHandlService= handlMap.get(importData.getHandleService());
                //如果实现不为空则调用
                if(null!=importHandlService){
                    importHandlService.handlData(resultList);
                }
            }
        }
        return resultList.size();
    }

}