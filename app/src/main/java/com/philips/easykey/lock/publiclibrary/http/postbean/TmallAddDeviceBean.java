package com.philips.easykey.lock.publiclibrary.http.postbean;

public class TmallAddDeviceBean {


    private String uid;
    private String wifiSN;
    private String userOpenId;
    private String deviceOpenId;
    private String nickName;
    private String aligenieSN;
    private String aligenieDeviceModel;
    private String aligenieMac;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWifiSN() {
        return wifiSN;
    }

    public void setWifiSN(String wifiSN) {
        this.wifiSN = wifiSN;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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
