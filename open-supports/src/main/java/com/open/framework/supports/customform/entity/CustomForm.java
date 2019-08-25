package com.open.framework.supports.customform.entity;

import com.open.framework.dao.model.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author hsj
 * @Description 自定义表单主体对象
 * @Date  2019-03-25 09:22:12
 **/
@Data
@Accessors(chain = true)
@Entity
public class CustomForm extends BaseEntity {
    private String code;
    private String name;
    /**
     * 0未发布,1已经发布
     */
    private Integer publishState;
    /**
     * 提交数据地址,前台提交时候用
     */
    private String postUrl;
    /**
     * 前端页面结构数据
     */
    private String jsonData;
    /**
     * 应用gid
     */
    private String applicationGid;
    /**
     * 菜单数据
     */
    private String menuUrl;
    /**
     * 排序字段,直接写结果 code desc,name asc
     */
    private String orderBy;
    /**
     * 默认code的下划线,MrlCode-->mrl_code
     */
    private String tableName;
    /**
     * 主表属性
     */
    @Transient
    private List<CustomFormDetail>  fields;
    /**
     * 子表属性
     */
    @Transient
    private List<CustomFormChild>  childs;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     *修改时间
     */
    private Date modifyTime;
}

