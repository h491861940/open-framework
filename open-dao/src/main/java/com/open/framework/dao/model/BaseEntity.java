package com.open.framework.dao.model;

import lombok.Data;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.TemporalType;
import java.util.Date;
import javax.persistence.*;
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Where(clause = " del_state = 0 ")
@Data
public abstract class BaseEntity extends IdEntity {
    /**
     * 创建人
     */
    @CreatedBy
    @Column(updatable=false)
    protected String createId;

    /**
     * 创建时间
     */
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    protected Date createDate;

    /**
     * 修改人
     */
    @LastModifiedBy
    protected String modifyId;

    /**
     * 修改时间,记录修改的最后时间.
     */
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date modifyDate;

    protected String delState;

    protected String actState;

}
