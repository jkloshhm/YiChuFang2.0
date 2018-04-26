package com.guojian.weekcook.bean;

import java.util.List;

/**
 * Created by guojian on 11/4/16.
 */
public class ParentClassBean {
    private String parentClassId;
    private String parentClassName;
    private String parentParentId;
    private List<ChildrenClassBean> childrenClassBeen;

    public ParentClassBean(List<ChildrenClassBean> childrenClassBeen, String parentClassId,
                           String parentClassName, String parentParentId) {
        this.childrenClassBeen = childrenClassBeen;
        this.parentClassId = parentClassId;
        this.parentClassName = parentClassName;
        this.parentParentId = parentParentId;
    }

    public List<ChildrenClassBean> getChildrenClassBeen() {
        return childrenClassBeen;
    }

    public void setChildrenClassBeen(List<ChildrenClassBean> childrenClassBeen) {
        this.childrenClassBeen = childrenClassBeen;
    }

    public String getParentClassId() {
        return parentClassId;
    }

    public void setParentClassId(String parentClassId) {
        this.parentClassId = parentClassId;
    }

    public String getParentClassName() {
        return parentClassName;
    }

    public void setParentClassName(String parentClassName) {
        this.parentClassName = parentClassName;
    }

    public String getParentParentId() {
        return parentParentId;
    }

    public void setParentParentId(String parentParentId) {
        this.parentParentId = parentParentId;
    }

    @Override
    public String toString() {
        return "ParentClassBean{" +
                "childrenClassBeen=" + childrenClassBeen +
                ", parentClassId='" + parentClassId + '\'' +
                ", parentClassName='" + parentClassName + '\'' +
                ", parentParentId='" + parentParentId + '\'' +
                '}';
    }
}
