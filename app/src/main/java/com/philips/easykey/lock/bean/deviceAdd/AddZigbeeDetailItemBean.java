package com.philips.easykey.lock.bean.deviceAdd;

import java.io.Serializable;

public class AddZigbeeDetailItemBean implements Serializable {
    private int imageId;

    private String text;

    //1代表网关，2代表猫眼，3代表锁
    private int type;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
