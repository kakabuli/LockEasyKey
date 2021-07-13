package com.philips.easykey.lock.publiclibrary.http.postbean;

/**
 * author :
 * time   : 2021/6/10
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class CodeLoginBean {


    private String tel;
    private String tokens;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTokens() {
        return tokens;
    }

    public void setTokens(String tokens) {
        this.tokens = tokens;
    }
}
