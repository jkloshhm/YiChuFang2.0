package com.guojian.weekcook.bean;

/**
 * @author jkloshhm at 2018-05-18 根据菜谱id请求返回的bean
 */

public class CookDetailBean {
    private String status;
    private String msg;
    private CookListBean.ResultBean.ListBean result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CookListBean.ResultBean.ListBean getResult() {
        return result;
    }

    public void setResult(CookListBean.ResultBean.ListBean result) {
        this.result = result;
    }

}
