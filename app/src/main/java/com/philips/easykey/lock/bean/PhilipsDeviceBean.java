package com.philips.easykey.lock.bean;

/**
 * author :
 * time   : 2021/4/22
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class PhilipsDeviceBean {

    private String deviceName;
    private int power;
    private long lastRecordTime;
    private String lastRecordDetail;
    private int deviceType;              // 模拟 1：视频锁  2：晾衣机


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

    public long getLastRecordTime() {
        return lastRecordTime;
    }

    public void setLastRecordTime(long lastRecordTime) {
        this.lastRecordTime = lastRecordTime;
    }

    public String getLastRecordDetail() {
        return lastRecordDetail;
    }

    public void setLastRecordDetail(String lastRecordDetail) {
        this.lastRecordDetail = lastRecordDetail;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public String toString() {
        return "PhilipsDeviceBean{" +
                "deviceName='" + deviceName + '\'' +
                ", power=" + power +
                ", lastRecordTime=" + lastRecordTime +
                ", lastRecordDetail='" + lastRecordDetail + '\'' +
                ", deviceType=" + deviceType +
                '}';
    }
}
