package com.open.framework.commmon.web;

import java.util.List;
import java.util.Map;

/**
 * @Auther: hsj
 * @Date: 2018/7/22 17:15
 * @Description:
 */
public class ExportData {
    private String fileName;//导出文件名
    private String[] columnNames;//列名称
    private String[] columns;//列
    private List datas;
    private QueryParam queryParam;
    public ExportData() {

    }

    public ExportData(String fileName, String[] columnNames, String[] columns) {
        this.fileName = fileName;
        this.columnNames = columnNames;
        this.columns = columns;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public List getDatas() {
        return datas;
    }

    public void setDatas(List datas) {
        this.datas = datas;
    }

    public QueryParam getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(QueryParam queryParam) {
        this.queryParam = queryParam;
    }
}
