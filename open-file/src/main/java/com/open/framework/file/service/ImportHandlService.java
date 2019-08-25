package com.open.framework.file.service;


import java.util.List;
import java.util.Map;

/**
 * @description: 回调的service,用于处理数据
 * @author: hsj
 * @date: 2019-08-24 23:37:55
 */
public interface ImportHandlService {

    /**
     * 读取excel中的数据,生成list
     */
    void handlData(List<Map<String, Object>> listData);

}