package com.open.framework.supports.file.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.open.framework.dao.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author DuanWei mailto:<duan_w@neusoft.com>
 * @version 1.0
 * @Description: 文件基本信息实体
 * @date 2018/8/20 15:26
 */
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "file_info")
@ApiModel(value = "com.zjz.framework.support.file.entity.FileInfo", description = "文件实体")
public class FileInfo extends BaseEntity {
    /**
     * 名称
     */
    @Column(name="name")
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 大小
     */
    @Column(name="original_size")
    @ApiModelProperty(value = "大小")
    private Long originalSize;
    /**
     * 类型
     *
     * @see
     */
    @Column(name="type")
    @ApiModelProperty(value = "类型")
    private String type;
    /**
     * 状态
     *
     * @see
     */
    @Column(name="status")
    @ApiModelProperty(value = "状态")
    private Short status;
    /**
     * 路径
     */
    @Column(name="path")
    @ApiModelProperty(value = "路径")
    private String path;
    /**
     * md5值
     */
    @Column(name="md5")
    @ApiModelProperty(value = "md5值")
    private String md5;
    /**
     * 后缀
     */
    @Column(name="suffix")
    @ApiModelProperty(value = "后缀")
    private String suffix;

    /**
     * 文件唯一标识
     */
    @Transient
    private UUID fileId;

    /**
     * 文件流
     */
    @Transient
    @JsonIgnore
    private InputStream inputStream;


}
