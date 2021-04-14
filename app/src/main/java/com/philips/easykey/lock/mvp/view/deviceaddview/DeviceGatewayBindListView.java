package com.philips.easykey.lock.mvp.view.deviceaddview;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.bean.ServerGatewayInfo;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.GetBindGatewayListResult;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.GwWiFiBaseInfo;

import java.util.List;

public interface DeviceGatewayBindListView extends IBaseView {



    //获取网关状态
    void getGatewayStateSuccess(String deviceId,String gatewayState);

    //获取网关状态失败
    void getGatewayStateFail();

    //获取网关的WiFi信息
    void onGetWifiInfoSuccess(GwWiFiBaseInfo gwWiFiBaseInfo);

    //获取网关WiFi信息失败
    void onGetWifiInfoFailed(Throwable throwable);




}
