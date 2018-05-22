package com.guojian.weekcook.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guojian on 12/15/16.
 */
public class StepViewPagerBean implements Serializable{
    private String position;
    private List<CookListBean.ResultBean.ListBean.ProcessBean> mProcessBeanList;

    public StepViewPagerBean(List<CookListBean.ResultBean.ListBean.ProcessBean> mProcessBeanList, String position) {
        this.mProcessBeanList = mProcessBeanList;
        this.position = position;
    }

    public List<CookListBean.ResultBean.ListBean.ProcessBean> getmProcessBeanList() {
        return mProcessBeanList;
    }

    public void setmProcessBeanList(List<CookListBean.ResultBean.ListBean.ProcessBean> mProcessBeanList) {
        this.mProcessBeanList = mProcessBeanList;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
