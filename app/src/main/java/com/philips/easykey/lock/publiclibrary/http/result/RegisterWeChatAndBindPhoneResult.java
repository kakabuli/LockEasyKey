package com.philips.easykey.lock.publiclibrary.http.result;

/**
 * author :
 * time   : 2021/6/10
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class RegisterWeChatAndBindPhoneResult {


    private String code;
    private String msg;
    private Integer nowTime;
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

    public Integer getNowTime() {
        return nowTime;
    }

    public void setNowTime(Integer nowTime) {
        this.nowTime = nowTime;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String uid;
        private String token;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "uid='" + uid + '\'' +
                    ", token='" + token + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "RegisterWeChatAndBindPhoneResult{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", nowTime=" + nowTime +
                ", data=" + data +
                '}';
    }
}
