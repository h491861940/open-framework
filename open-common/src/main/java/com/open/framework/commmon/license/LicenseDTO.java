package com.open.framework.commmon.license;

import lombok.Data;


@Data
public class LicenseDTO {
    private String cpu;
    private String board;
    private String disk;
    private String mac;
    //是否临时license,yes是,no不是
    private String tempTry;
    //过期日期
    private String expireDate;
    //使用个数
    private Integer userCount=10;
}
