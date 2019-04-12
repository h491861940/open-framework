package com.open.framework.supports.customform.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author hsj
 * @Description 自定义表单属性字段
 * @Date  2019-03-25 09:23:21
 **/
@Data
@Accessors(chain = true)
@Entity
public class CustomFormDetail implements Serializable {
    @Id
    private String gid;
    private String code;
    private String name;
    /**
     * 字段类型
     */
    private String fieldType;
    /**
     * 页面元素类型
     */
    private String htmlType;
    /**
     * 序号
     */
    private Integer seq;
    /**
     * 1,主表,2子表
     */
    private Integer parentType;
    /**
     * 主表gid,包括customForm,customFormChild
     */
    private String parentGid;
    /**
     * 新增1,修改2,删除3
     */
    private int cudState;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     *修改时间
     */
    private Date modifyTime;

}
