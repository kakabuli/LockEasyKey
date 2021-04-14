package com.philips.easykey.lock.mvp.view.wifilock;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.WifiLockVideoBindBean;

public interface IWifiLockVideoFifthView extends IBaseView {

    void onDeviceBinding(WifiLockVideoBindBean mWifiLockVideoBindBean);

}
