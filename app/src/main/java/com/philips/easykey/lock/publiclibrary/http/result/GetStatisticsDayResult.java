package com.philips.easykey.lock.publiclibrary.http.result;

import java.util.List;

public class GetStatisticsDayResult extends BaseResult{

    /**
     {
     "code": "200",
     "msg": "成功",
     "nowTime": 1564473520,
     "data": {
     "pwdOpenLockCount": 10,
     "fingerprintOpenLockCount": 10,
     "cardOpenLockCount": 10,
     "faceOpenLockCount": 10,
     "doorbellCount": 3,
     "allCount" : 50
     }
     }
     */
    private long nowTime;

    private DataBean data;

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
    public void setNowTime(long nowTime){
        this.nowTime = nowTime;
    }
    public long getNowTime(){
        return this.nowTime;
    }
    public void setData(DataBean data){
        this.data = data;
    }
    public DataBean getData(){
        return this.data;
    }

    public static class DataBean {

        private int doorbellCount;

        private int pwdOpenLockCount;

        private int fingerprintOpenLockCount;

        private int faceOpenLockCount;

        private int cardOpenLockCount;

        private int allCount;

        public void setPwdOpenLockCount(int pwdOpenLockCount){
            this.pwdOpenLockCount = pwdOpenLockCount;
        }
        public int getPwdOpenLockCount(){
            return this.pwdOpenLockCount;
        }
        public void setFingerprintOpenLockCount(int fingerprintOpenLockCount){
            this.fingerprintOpenLockCount = fingerprintOpenLockCount;
        }
        public int getFingerprintOpenLockCount(){
            return this.fingerprintOpenLockCount;
        }
        public void setCardOpenLockCount(int cardOpenLockCount){
            this.cardOpenLockCount = cardOpenLockCount;
        }
        public int getCardOpenLockCount(){
            return this.cardOpenLockCount;
        }
        public void setFaceOpenLockCount(int faceOpenLockCount){
            this.faceOpenLockCount = faceOpenLockCount;
        }
        public int getFaceOpenLockCount(){
            return this.faceOpenLockCount;
        }
        public void setDoorbellCount(int doorbellCount){
            this.doorbellCount = doorbellCount;
        }
        public int getDoorbellCount(){
            return this.doorbellCount;
        }
        public void setAllCount(int allCount){
            this.allCount = allCount;
        }
        public int getAllCount(){
            return this.allCount;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "pwdOpenLockCount=" + pwdOpenLockCount +
                    ", fingerprintOpenLockCount=" + fingerprintOpenLockCount +
                    ", cardOpenLockCount=" + cardOpenLockCount +
                    ", faceOpenLockCount=" + faceOpenLockCount +
                    ", doorbellCount=" + doorbellCount +
                    ", allCount=" + allCount +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GetStatisticsDayResult{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", nowTime=" + nowTime +
                ", data=" + data +
                '}';
    }
}
