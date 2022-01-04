package com.philips.easykey.lock.publiclibrary.bean;

import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;

import java.io.Serializable;
import java.util.List;

public class TmallDeviceListBean implements Serializable {

    private String _id;

    private String wifiSN;

    private String lockNickname;

    private String productModel;

    private String userOpenId;

    private String deviceOpenId;

    private int isBind;

    private String aligenieSN;
    private String aligenieDeviceModel;
    private String aligenieMac;

    private boolean isSelece;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getWifiSN() {
        return wifiSN;
    }

    public void setWifiSN(String wifiSN) {
        this.wifiSN = wifiSN;
    }

    public String getLockNickname() {
        return lockNickname;
    }

    public void setLockNickname(String lockNickname) {
        this.lockNickname = lockNickname;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getUserOpenId() {
        return userOpenId;
    }

    public void setUserOpenId(String userOpenId) {
        this.userOpenId = userOpenId;
    }

    public String getDeviceOpenId() {
        return deviceOpenId;
    }

    public void setDeviceOpenId(String deviceOpenId) {
        this.deviceOpenId = deviceOpenId;
    }

    public int getIsBind() {
        return isBind;
    }

    public void setIsBind(int isBind) {
        this.isBind = isBind;
    }

    public boolean isSelece() {
        return isSelece;
    }

    public void setSelece(boolean selece) {
        isSelece = selece;
    }

    public String getAligenieSN() {
        return aligenieSN;
    }

    public void setAligenieSN(String aligenieSN) {
        this.aligenieSN = aligenieSN;
    }

    public String getAligenieDeviceModel() {
        return aligenieDeviceModel;
    }

    public void setAligenieDeviceModel(String aligenieDeviceModel) {
        this.aligenieDeviceModel = aligenieDeviceModel;
    }

    public String getAligenieMac() {
        return aligenieMac;
    }

    public void setAligenieMac(String aligenieMac) {
        this.aligenieMac = aligenieMac;
    }
}
