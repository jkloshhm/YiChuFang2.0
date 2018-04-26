package com.guojian.weekcook.bean;

import java.io.Serializable;

/**
 * Created by guojian on 11/15/16.
 */
public class MaterialBean implements Serializable{
    private String mname;
    private String type;
    private String amount;

    public MaterialBean(String amount, String mname, String type) {
        this.amount = amount;
        this.mname = mname;
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
