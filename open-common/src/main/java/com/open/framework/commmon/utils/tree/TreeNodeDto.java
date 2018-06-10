package com.open.framework.commmon.utils.tree;

import java.io.Serializable;
import java.util.List;

/**
 * 类TreeNodeDTO.java的实现描述：用于传递树形数据模型，可以自由扩展属性，但不要删除已有属性
 * 
 * @author Mike 2017年6月15日 下午2:28:22
 */
public class TreeNodeDto implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long           serialVersionUID = 4214344288219280437L;

    private String                      id;

    private String                      pid;

    private String                      code;

    private String                      name;

    private String                      parentCode;

    private String                      parentName;

    private String                      text;

    private String                      objId;

    private String                      methodName;

    private String                      url;

    private String                      icon;

    private boolean                     disabled;

    private boolean                     active;

    private boolean                     virtual;

    private List<? extends TreeNodeDto> childs;
    
    private List<? extends TreeNodeDto> children;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<? extends TreeNodeDto> getChilds() {
        return childs;
    }

    public void setChilds(List<? extends TreeNodeDto> childs) {
        this.childs = childs;
    }

    /**
     * @return the disabled
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }
    
    public List<? extends TreeNodeDto> getChildren() {
        return children;
    }

    public void setChildren(List<? extends TreeNodeDto> children) {
        this.children = children;
    }
}
