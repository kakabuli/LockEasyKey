package com.philips.easykey.lock.publiclibrary.http.postbean;

public class TmallDelDeviceBean {


    private String uid;
    private String wifiSN;
    private String userOpenId;
    private String deviceOpenId;

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
}
