package com.philips.easykey.lock.publiclibrary.mqtt.publishbean;

import java.io.Serializable;

public class SetNetBasicBean implements Serializable {


    /**
     * msgtype : respone
     * userId :
     * msgId : 1
     * gwId :
     * deviceId : devuuid
     * func :  setNetBasic
     * params : {"lanIp":"192.168.1.1","lanNetmask":"255.255.255.0"}
     * returnCode : 200
     * returnData : {}
     * timestamp : 13433333333
     */

    private String msgtype;
    private String userId;
    private int msgId;
    private String gwId;
    private String deviceId;
    private String func;
    private ParamsBean params;
    private String returnCode;
    private ReturnDataBean returnData;
    private String timestamp;

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getGwId() {
        return gwId;
    }

    public void setGwId(String gwId) {
        this.gwId = gwId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public ReturnDataBean getReturnData() {
        return returnData;
    }

    public void setReturnData(ReturnDataBean returnData) {
        this.returnData = returnData;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static class ParamsBean {
        /**
         * lanIp : 192.168.1.1
         * lanNetmask : 255.255.255.0
         */

        private String lanIp;
        private String lanNetmask;

        public String getLanIp() {
            return lanIp;
        }

        public void setLanIp(String lanIp) {
            this.lanIp = lanIp;
        }

        public String getLanNetmask() {
            return lanNetmask;
        }

        public void setLanNetmask(String lanNetmask) {
            this.lanNetmask = lanNetmask;
        }
    }

    public static class ReturnDataBean {
    }

    public SetNetBasicBean(String msgtype, String userId, int msgId, String gwId, String deviceId, String func, ParamsBean params, String returnCode, ReturnDataBean returnData, String timestamp) {
        this.msgtype = msgtype;
        this.userId = userId;
        this.msgId = msgId;
        this.gwId = gwId;
        this.deviceId = deviceId;
        this.func = func;
        this.params = params;
        this.returnCode = returnCode;
        this.returnData = returnData;
        this.timestamp = timestamp;
    }

    public SetNetBasicBean() {
    }
}
