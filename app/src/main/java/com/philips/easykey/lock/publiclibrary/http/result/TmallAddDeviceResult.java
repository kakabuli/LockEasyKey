package com.philips.easykey.lock.publiclibrary.http.result;

/**
 * Create By lxj  on 2019/3/3
 * Describe
 */
public class TmallAddDeviceResult extends BaseResult {

    /**
     {
     "code": "200",
     "msg": "成功",
     "nowTime": 1638846331,
     "data": {}
     }
     */
    private int nowTime;

    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setNowTime(int nowTime){
        this.nowTime = nowTime;
    }
    public int getNowTime(){
        return this.nowTime;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
    }
}
