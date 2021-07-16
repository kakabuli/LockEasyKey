package com.philips.easykey.lock.publiclibrary.http.postbean;

public class WifiLockRecordScreenedBean {


    public WifiLockRecordScreenedBean(String wifiSN, int page,long startTime,long endTime) {
        this.wifiSN = wifiSN;
        this.page = page;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     "wifiSN": "WF01202010001",
     "page": 1,
     "startTime": 1619539200,
     "endTime": 1619625600
     */



    private String wifiSN;
    private int page;
    private long startTime;
    private long endTime;

    public String getWifiSN() {
        return wifiSN;
    }

    public void setWifiSN(String wifiSN) {
        this.wifiSN = wifiSN;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
