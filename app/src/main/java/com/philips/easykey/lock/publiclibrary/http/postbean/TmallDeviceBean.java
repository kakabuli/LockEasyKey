package com.philips.easykey.lock.publiclibrary.http.postbean;

public class TmallDeviceBean {

    private Code code;
    private String uid;
    public void setCode(Code code) {
        this.code = code;
    }
    public Code getCode() {
        return code;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getUid() {
        return uid;
    }

    public class Code {

        private String aligenieDeviceName;
        private String aligenieSN;
        private String code;
        private String userOpenId;
        private String deviceOpenId;
        private String action;
        private String aligenieDeviceModel;
        private String aligenieMac;
        public void setAligenieDeviceName(String aligenieDeviceName) {
            this.aligenieDeviceName = aligenieDeviceName;
        }
        public String getAligenieDeviceName() {
            return aligenieDeviceName;
        }

        public void setAligenieSN(String aligenieSN) {
            this.aligenieSN = aligenieSN;
        }
        public String getAligenieSN() {
            return aligenieSN;
        }

        public void setCode(String code) {
            this.code = code;
        }
        public String getCode() {
            return code;
        }

        public void setUserOpenId(String userOpenId) {
            this.userOpenId = userOpenId;
        }
        public String getUserOpenId() {
            return userOpenId;
        }

        public void setDeviceOpenId(String deviceOpenId) {
            this.deviceOpenId = deviceOpenId;
        }
        public String getDeviceOpenId() {
            return deviceOpenId;
        }

        public void setAction(String action) {
            this.action = action;
        }
        public String getAction() {
            return action;
        }

        public void setAligenieDeviceModel(String aligenieDeviceModel) {
            this.aligenieDeviceModel = aligenieDeviceModel;
        }
        public String getAligenieDeviceModel() {
            return aligenieDeviceModel;
        }

        public void setAligenieMac(String aligenieMac) {
            this.aligenieMac = aligenieMac;
        }
        public String getAligenieMac() {
            return aligenieMac;
        }

    }
}
