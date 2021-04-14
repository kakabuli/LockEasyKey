package com.philips.easykey.lock.publiclibrary.http.temp.postbean;

public class GetLockNumberNicknameBean {
    private String uid;
    private String devname;

    public GetLockNumberNicknameBean(String uid, String devname) {
        this.uid = uid;
        this.devname = devname;
    }
}
