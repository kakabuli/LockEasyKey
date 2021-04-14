package com.philips.easykey.lock.publiclibrary.bean;

import java.io.Serializable;

public class ServerGwDevice implements Serializable {
    /**
     * SW : orangecat-1.3.7
     * deviceId : 0C9A42B78CF5
     * device_type : kdscateye
     * event_str : offline
     * ipaddr : 192.168.168.235
     * macaddr : 0C:9A:42:B7:8C:F5
     * nickName : 0C9A42B78CF5
     * time : 2019-04-25 11:24:43.548
     */

    private String SW;
    private String deviceId;
    private String device_type;
    private String event_str;
    private String ipaddr;
    private String macaddr;
    private String nickName;
    private String time;
    private String model;   // 6030 小网关、 6010大网关
    /**
     * delectTime : 2019-11-28 14:29:33.961
     * lockversion : 8100Z;00;V1.02.040;V0.00.000
     * moduletype : KSZG1703U
     * nwaddr : 34049
     * offlineTime : 2019-11-28 17:29:13.809
     * onlineTime : 2019-11-28 17:29:24.923
     * shareFlag : 0
     */

    private String delectTime;
    private String lockversion;
    private String moduletype;
    private int nwaddr;
    private String offlineTime;
    private String onlineTime;
    private int shareFlag;
    private int pushSwitch;


    public int getPushSwitch() {
        return pushSwitch;
    }

    public void setPushSwitch(int pushSwitch) {
        this.pushSwitch = pushSwitch;
    }

    public String getSW() {
        return SW;
    }

    public void setSW(String SW) {
        this.SW = SW;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getEvent_str() {
        return event_str;
    }

    public void setEvent_str(String event_str) {
        this.event_str = event_str;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public String getMacaddr() {
        return macaddr;
    }

    public void setMacaddr(String macaddr) {
        this.macaddr = macaddr;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public ServerGwDevice(String SW, String deviceId, String device_type, String event_str, String ipaddr, String macaddr, String nickName, String time, String model, String delectTime, String lockversion, String moduletype, int nwaddr, String offlineTime, String onlineTime, int shareFlag, int pushSwitch) {
        this.SW = SW;
        this.deviceId = deviceId;
        this.device_type = device_type;
        this.event_str = event_str;
        this.ipaddr = ipaddr;
        this.macaddr = macaddr;
        this.nickName = nickName;
        this.time = time;
        this.model = model;
        this.delectTime = delectTime;
        this.lockversion = lockversion;
        this.moduletype = moduletype;
        this.nwaddr = nwaddr;
        this.offlineTime = offlineTime;
        this.onlineTime = onlineTime;
        this.shareFlag = shareFlag;
        this.pushSwitch = pushSwitch;
    }

    public ServerGwDevice(String SW, String deviceId, String device_type, String event_str, String ipaddr, String macaddr, String nickName, String time, String model) {
        this.SW = SW;
        this.deviceId = deviceId;
        this.device_type = device_type;
        this.event_str = event_str;
        this.ipaddr = ipaddr;
        this.macaddr = macaddr;
        this.nickName = nickName;
        this.time = time;
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public ServerGwDevice() {
    }

    @Override
    public String toString() {
        return "ServerGwDevice{" +
                "SW='" + SW + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", device_type='" + device_type + '\'' +
                ", event_str='" + event_str + '\'' +
                ", ipaddr='" + ipaddr + '\'' +
                ", macaddr='" + macaddr + '\'' +
                ", nickName='" + nickName + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public String getDelectTime() {
        return delectTime;
    }

    public void setDelectTime(String delectTime) {
        this.delectTime = delectTime;
    }

    public String getLockversion() {
        return lockversion;
    }

    public void setLockversion(String lockversion) {
        this.lockversion = lockversion;
    }

    public String getModuletype() {
        return moduletype;
    }

    public void setModuletype(String moduletype) {
        this.moduletype = moduletype;
    }

    public int getNwaddr() {
        return nwaddr;
    }

    public void setNwaddr(int nwaddr) {
        this.nwaddr = nwaddr;
    }

    public String getOfflineTime() {
        return offlineTime;
    }

    public void setOfflineTime(String offlineTime) {
        this.offlineTime = offlineTime;
    }

    public String getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(String onlineTime) {
        this.onlineTime = onlineTime;
    }

    public int getShareFlag() {
        return shareFlag;
    }

    public void setShareFlag(int shareFlag) {
        this.shareFlag = shareFlag;
    }
}
