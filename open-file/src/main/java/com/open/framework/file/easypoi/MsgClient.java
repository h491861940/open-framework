package com.open.framework.file.easypoi;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 通讯录
 * @author JueYue
 *   2014-09-22 16:03:32
 * @version V1.0
 * 
 */
@SuppressWarnings("serial")
@Data
public class MsgClient implements Serializable {
    /** id */
    private String id;
    @Excel(name = "电话号码")
    private String           clientPhone = null;
    @Excel(name = "姓名")
    private String           clientName  = null;
    @Excel(name = "备注")
    private String           remark      = null;
    @Excel(name = "出生日期", format = "yyyy-MM-dd", width = 20)
    private Date             birthday    = null;
    private String           createBy    = null;


}
