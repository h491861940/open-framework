package com.open.framework.supports.customform.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @Auther: hsj
 * @Date: 2019/3/26 15:14
 * @Description:
 */
@Data
public class CustomFormCommonDTO {
    private String formId;
    private String formCode;
    private JSONObject data;
}
