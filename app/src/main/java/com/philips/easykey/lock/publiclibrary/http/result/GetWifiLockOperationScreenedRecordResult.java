package com.philips.easykey.lock.publiclibrary.http.result;

import com.philips.easykey.lock.publiclibrary.bean.WifiLockOperationRecord;

import java.util.List;

public class GetWifiLockOperationScreenedRecordResult extends BaseResult {

    /**
     * code : 200
     * msg : 成功
     * nowTime : 1576655959
     * data : [{"_id":"5dde33754d27d6da12f51637","time":"1541468973342","type":1,"wifiSN":"WF132231004","bleSN":"BT01192910010","pwdType":4,"pwdNum":2,"createTime":1576058537426,"appId":1,"uid":"5c4fe492dc93897aa7d8600b","uname":"8618954359822","userNickname":"ahaha"}]
     */

    private int nowTime;

    private Data data;

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
    public void setData(Data data){
        this.data = data;
    }
    public Data getData(){
        return this.data;
    }

    public class Data
    {
        private List<WifiLockOperationRecord> operationList;

        public List<WifiLockOperationRecord> getOperationList() {
            return operationList;
        }

        public void setOperationList(List<WifiLockOperationRecord> operationList) {
            this.operationList = operationList;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "operationList=" + operationList +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GetWifiLockOperationScreenedRecordResult{" +
                "nowTime=" + nowTime +
                ", data=" + data +
                '}';
    }
}
