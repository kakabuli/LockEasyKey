package com.philips.easykey.lock.bean;

import com.philips.easykey.lock.publiclibrary.bean.WifiLockOperationRecord;

import org.jetbrains.annotations.NotNull;

/**
 * author :
 * time   : 2021/4/22
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class PhilipsDeviceBean {

    private String deviceName;
    private int power;
    private WifiLockOperationRecord lastRecordDetail;
    private int deviceType;              // 模拟 6：视频锁  7：晾衣机
    private String wifiSn;
    private int powerSave;
    private int purview; ///权限范围

    public int getPowerSave() {
        return powerSave;
    }

    public void setPowerSave(int powerSave) {
        this.powerSave = powerSave;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public WifiLockOperationRecord getLastRecordDetail() {
        return lastRecordDetail;
    }

    public void setLastRecordDetail(WifiLockOperationRecord lastRecordDetail) {
        this.lastRecordDetail = lastRecordDetail;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getWifiSn() {
        return wifiSn;
    }

    public void setWifiSn(String wifiSn) {
        this.wifiSn = wifiSn;
    }

    public int getPurview() {
        return purview;
    }

    public void setPurview(int purview) {
        this.purview = purview;
    }

    @NotNull
    @Override
    public String toString() {
        return "PhilipsDeviceBean{" +
                "deviceName='" + deviceName + '\'' +
                ", power=" + power +
                ", lastRecordDetail='" + lastRecordDetail.toString() + '\'' +
                ", deviceType=" + deviceType +
                ", wifiSn='" + wifiSn + '\'' +
                ", powerSave=" + powerSave +
                ", purview=" + purview +
                '}';
    }
}
