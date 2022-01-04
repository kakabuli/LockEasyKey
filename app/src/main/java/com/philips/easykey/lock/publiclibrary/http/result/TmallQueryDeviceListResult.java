package com.philips.easykey.lock.publiclibrary.http.result;

import java.io.Serializable;
import java.util.List;

public class TmallQueryDeviceListResult extends BaseResult{

    /**
     {
     "code": "200",
     "msg": "成功",
     "nowTime": 1638868692,
     "data": [
     {
     "_id": "60f161ad6fdd1d112347fb78",
     "wifiSN": "8VP0212810001",
     "lockNickname": "708vp",
     "productModel": "ALPHA-V",
     "isBind": 0,
     "deviceOpenId": "",
     "userOpenId": ""
     },
     {
     "_id": "61af0f7a1c731976295a1fe7",
     "wifiSN": "8VP0212810002",
     "lockNickname": "708vp-2",
     "productModel": "ALPHA-V",
     "userOpenId": "123",
     "deviceOpenId": "789",
     "isBind": 1
     }
     ]
     }
     */

    private int nowTime;

    private List<TmallQueryDeviceList> data;

    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
    public void setNowTime(int nowTime){
        this.nowTime = nowTime;
    }
    public int getNowTime(){
        return this.nowTime;
    }

    public List<TmallQueryDeviceList> getData() {
        return data;
    }

    public void setData(List<TmallQueryDeviceList> data) {
        this.data = data;
    }

    public static class TmallQueryDeviceList implements Serializable {
        public String _id;

        private String wifiSN;

        private String lockNickname;

        private String productModel;

        private String userOpenId;

        private String deviceOpenId;

        private String aligenieSN;

        private String aligenieDeviceModel;

        private String aligenieMac;

        public TmallQueryDeviceList(){}

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

        @Override
        public String toString() {
            return "TmallQueryDeviceList{" +
                    "_id='" + _id + '\'' +
                    ", wifiSN='" + wifiSN + '\'' +
                    ", lockNickname='" + lockNickname + '\'' +
                    ", productModel='" + productModel + '\'' +
                    ", userOpenId='" + userOpenId + '\'' +
                    ", deviceOpenId='" + deviceOpenId + '\'' +
                    ", aligenieSN='" + aligenieSN + '\'' +
                    ", aligenieDeviceModel='" + aligenieDeviceModel + '\'' +
                    ", aligenieMac='" + aligenieMac + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TmallQueryDeviceListResult{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", nowTime=" + nowTime +
                ", data=" + data +
                '}';
    }
}
