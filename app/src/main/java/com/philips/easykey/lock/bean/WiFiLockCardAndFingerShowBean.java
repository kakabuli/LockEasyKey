package com.philips.easykey.lock.bean;

import java.io.Serializable;

public class WiFiLockCardAndFingerShowBean implements Serializable {
    private int num;
    private long createTime;
    private String nickName;
    private int tpye;


    public WiFiLockCardAndFingerShowBean(int num, long createTime, String nickName,int type) {
        this.num = num;
        this.createTime = createTime;
        this.nickName = nickName;
        this.tpye = type;
    }

    public int getTpye() {
        return tpye;
    }

    public void setTpye(int tpye) {
        this.tpye = tpye;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
