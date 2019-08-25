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
 * @Description 自定义表单子表对象
 * @Date  2019-03-25 09:23:00
 **/
@Data
@Accessors(chain = true)
@Entity
public class CustomFormChild extends BaseEntity {
   private String code;
   private String name;
   /**
    * 排序字段,直接写结果 code desc,name asc
    */
   private String orderBy;
   /**
    * 默认code的下划线,MrlCode-->mrl_code
    */
   private String tableName;
   /**
    * 主表gid
    */
   private String customFormGid;
   @Transient
   private List<CustomFormDetail> fields;
    /**
     * 新增1,修改2,删除3
     */
   private Integer cudState;
   /**
    * 创建时间
    */
   private Date createTime;

   /**
    *修改时间
    */
   private Date modifyTime;
}
