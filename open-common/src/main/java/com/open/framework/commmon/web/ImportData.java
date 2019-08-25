package com.open.framework.commmon.web;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: ImportData
 * @Description: 基本导入对象
 * @Author: hsj
 * @Date: 2019/8/24 17:37
 * @Version 1/0
 **/
@Data
public class ImportData {
    /**
     * 列信息和excel列对应,如果没有,默认为column1,column2
     */
    private List<String> columns;
    /**
     * 回调的service名称
     */
    private String handleService;
}
