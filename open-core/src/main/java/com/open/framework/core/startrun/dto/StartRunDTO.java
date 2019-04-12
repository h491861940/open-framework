package com.open.framework.core.startrun.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: hsj
 * @Date: 2019/4/12 09:24
 * @Description:
 */
@Data
public class StartRunDTO implements Serializable {
    private String methodName;
    private String serviceName;
    private Integer seq;
}
