package com.open.framework.supports.file.enums;

/**
 * @author DuanWei mailto:<duan_w@neusoft.com>
 * @version 1.0
 * @Description: 文件状态枚举类
 * @date 2018/8/20 15:39
 */
public enum FileStatus {

    /**
     * 未完成
     */
    UNDONE((short) 0, "未完成"),
    /**
     * 已完成
     */
    DONE((short) 1, "已完成");

    private Short code;
    private String name;

    FileStatus(Short code, String name) {
        this.code = code;
        this.name = name;
    }

    public Short getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
