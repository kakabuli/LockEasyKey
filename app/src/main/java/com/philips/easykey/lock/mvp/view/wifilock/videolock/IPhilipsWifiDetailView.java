package com.philips.easykey.lock.mvp.view.wifilock.videolock;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;

public interface IPhilipsWifiDetailView extends IBaseView {

    void getSignalSuccess(String rssi,String strength);

    void getSignalFailed(int errorCode);
}
