package com.philips.easykey.lock.publiclibrary.http.result;

import com.philips.easykey.lock.publiclibrary.bean.WifiVideoLockAlarmRecord;

import java.util.List;

public class GetWifiVideoLockDoorbellScreenedRecordResult extends BaseResult {

    /**
     * {
     *     "code": "200",
     *     "msg": "成功",
     *     "nowTime": 1576656504,
     *     "data": [
     *         {
     *             "_id": "5df0abf54d27d6da12fb4c71",
     *             "time": "1541468973342",
     *             "type": 4,
     *             "wifiSN": "WF132231004",
     *             "createTime": "1576054908866",
     *             "productSN" : "KV51203710173",
     *             "eventId" : "KV512037101731484f83217d941ae9e354b3f3e68a342",
     *             "thumbUrl" : "https://test.juziwulian.com:8050/kx/api/upload/KV512037101731484f83217d941ae9e354b3f3e68a342.jpg",
     *             "fileDate" : "20200924",
     *             "fileName" : "152606",
     *             "height" : 1920,
     *             "startTime" : 1600917264,
     *             "thumbState" : true,
     *             "updateTime" : 1600933259,
     *             "vedioTime" : 10,
     *             "width" : 1080
     *         }
     *     ]
     * }
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
        private List<WifiVideoLockAlarmRecord> doorbellList;

        public List<WifiVideoLockAlarmRecord> getDoorbellList() {
            return doorbellList;
        }

        public void setDoorbellList(List<WifiVideoLockAlarmRecord> doorbellList) {
            this.doorbellList = doorbellList;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "doorbellList=" + doorbellList +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GetWifiVideoLockAlarmRecordResult{" +
                "nowTime=" + nowTime +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
