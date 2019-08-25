package com.open.framework.commmon.web;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Auther: hsj
 * @Date: 2018/7/22 17:15
 * @Description: 导出数据对象
 */
@Data
public class ExportData {
    private String fileName;//导出文件名
    private String[] columnNames;//列名称
    private String[] columns;//列
    private List datas;//数据

    private QueryParam queryParam;//条件

    public ExportData() {

    }

    public ExportData(String fileName, String[] columnNames, String[] columns) {
        this.fileName = fileName;
        this.columnNames = columnNames;
        this.columns = columns;
    }

}
