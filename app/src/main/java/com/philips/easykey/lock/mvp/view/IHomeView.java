package com.philips.easykey.lock.mvp.view;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.AllBindDevices;

public interface IHomeView extends IBaseView {

    void onDeviceRefresh(AllBindDevices allBindDevices);

}
