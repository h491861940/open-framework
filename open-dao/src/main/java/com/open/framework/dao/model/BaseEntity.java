package com.open.framework.dao.model;

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
//@Where(clause = "del_state = false")
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

    protected boolean delState;

    protected boolean actState;

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
    public boolean getDelState() {
        return delState;
    }

    public void setDelState(boolean delState) {
        this.delState = delState;
    }

    public boolean getActState() {
        return actState;
    }

    public void setActState(boolean actState) {
        this.actState = actState;
    }
}
