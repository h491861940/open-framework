package com.open.framework.file.service;

import com.open.framework.commmon.web.ImportData;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description: 获取导入数据,返回成功条数
 * @author: hsj
 * @date: 2019-08-24 23:35:35
 */
public interface ImportService {
      
    /** 
     * 读取excel中的数据,生成list 
     */
    int readExcelFile(MultipartFile file, ImportData importData);
  
}