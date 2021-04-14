package com.philips.easykey.lock.publiclibrary.http.postbean;

public class GetOpenCountBean {


    /**
     * wifiSN : WF132231004
     */

    private String wifiSN;

    public GetOpenCountBean(String wifiSN) {
        this.wifiSN = wifiSN;
    }

    public String getWifiSN() {
        return wifiSN;
    }

    public void setWifiSN(String wifiSN) {
        this.wifiSN = wifiSN;
    }
}
