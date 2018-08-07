package com.open.framework.file.dto;

import java.util.List;

/**
 * @Auther: hsj
 * @Date: 2018/7/22 17:15
 * @Description:
 */
public class ExportData {
    private String fileName;//导出文件名
    private String[] columnNames;//列名称
    private String[] column;//列

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

    public String[] getColumn() {
        return column;
    }

    public void setColumn(String[] column) {
        this.column = column;
    }
}
