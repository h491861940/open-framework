package com.open.framework.file.service.impl;/**
 * @Auther: hsj
 * @Date: 2019/8/24 21:38
 * @Description:
 */

import com.open.framework.file.service.ImportHandlService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *@ClassName: ImportHandlServiceImpl
 *@Description:
 *@Author: lenovo
 *@Date: 2019/8/24 21:38
 *@Version 1/0
 **/
@Service("defaultImportHandl")
public class ImportHandlServiceImpl implements ImportHandlService {
    @Override
    public void handlData(List<Map<String, Object>> listData) {
        System.out.println(listData.toString());
    }
}
