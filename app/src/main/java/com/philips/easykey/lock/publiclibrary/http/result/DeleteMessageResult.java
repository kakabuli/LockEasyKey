package com.philips.easykey.lock.publiclibrary.http.result;

public class DeleteMessageResult extends BaseResult{


    /**
     * code : 200
     * msg : 成功
     * data : null
     */

    private String code;
    private String msg;
    private Object data;

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
