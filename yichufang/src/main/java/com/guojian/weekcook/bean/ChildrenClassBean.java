package com.guojian.weekcook.bean;

/**
 * Created by guojian on 11/4/16.
 */
public class ChildrenClassBean {
    private String childrenClassId;
    private String childrenClassName;
    private String childrenParentId;

    public ChildrenClassBean(String childrenClassId, String childrenClassName,
                             String childrenParentId) {
        this.childrenClassId = childrenClassId;
        this.childrenClassName = childrenClassName;
        this.childrenParentId = childrenParentId;
    }

    public String getChildrenClassId() {
        return childrenClassId;
    }

    public void setChildrenClassId(String childrenClassId) {
        this.childrenClassId = childrenClassId;
    }

    public String getChildrenClassName() {
        return childrenClassName;
    }

    public void setChildrenClassName(String childrenClassName) {
        this.childrenClassName = childrenClassName;
    }

    public String getChildrenParentId() {
        return childrenParentId;
    }

    public void setChildrenParentId(String childrenParentId) {
        this.childrenParentId = childrenParentId;
    }

    @Override
    public String toString() {
        return "ChildrenClassBean{" +
                "childrenClassId='" + childrenClassId + '\'' +
                ", childrenClassName='" + childrenClassName + '\'' +
                ", childrenParentId='" + childrenParentId + '\'' +
                '}';
    }
}
