package com.guojian.weekcook.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guojian on 12/15/16.
 */
public class StepViewPagerBean implements Serializable{
    private String position;
    private List<ProcessBean> mProcessBeanList;

    public StepViewPagerBean(List<ProcessBean> mProcessBeanList, String position) {
        this.mProcessBeanList = mProcessBeanList;
        this.position = position;
    }

    public List<ProcessBean> getmProcessBeanList() {
        return mProcessBeanList;
    }

    public void setmProcessBeanList(List<ProcessBean> mProcessBeanList) {
        this.mProcessBeanList = mProcessBeanList;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
