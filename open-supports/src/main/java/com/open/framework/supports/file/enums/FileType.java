package com.open.framework.supports.file.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author DuanWei mailto:<duan_w@neusoft.com>
 * @version 1.0
 * @Description: 文件类型枚举类
 * @date 2018/8/20 15:39
 */
public enum FileType {
    /**
     * PDF
     */
    PDF("PDF", Collections.singletonList(".pdf")),
    /**
     * WORD
     */
    WORD("WORD", Arrays.asList(".doc", ".docx")),
    /**
     * EXCEL
     */
    EXCEL("EXCEL", Arrays.asList(".xls", ".xlsx")),
    /**
     * BMP、JPG、JPEG、PNG、GIF
     */
    IMG("图片", Arrays.asList(".jpg", ".bmp", ".jpeg", ".png")),
    /**
     * ZIP
     */
    ZIP("压缩包", Arrays.asList(".zip", ".tar")),
    /**
     * 其他
     */
    OTHER("其他", Collections.emptyList());

    private String name;

    private List<String> suffixs;

    FileType(String name, List<String> suffixs) {
        this.name = name;
        this.suffixs = suffixs;
    }

    public List<String> getSuffixs() {
        return suffixs;
    }

    public String getName() {
        return name;
    }

    public static String getNameBySuffix(String suffix) {
        for (FileType type : FileType.values()) {
            if (type.getSuffixs().contains(suffix)) {
                return type.getName();
            }
        }
        return OTHER.getName();
    }
}
