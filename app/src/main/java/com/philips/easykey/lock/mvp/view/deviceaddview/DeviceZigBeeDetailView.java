package com.philips.easykey.lock.mvp.view.deviceaddview;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.bean.ServerGatewayInfo;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.AllBindDevices;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.GetBindGatewayListResult;

import java.util.List;

public interface DeviceZigBeeDetailView extends IBaseView {
    //页面初始化的时候更新
    void onDeviceRefresh(AllBindDevices allBindDevices);


}
