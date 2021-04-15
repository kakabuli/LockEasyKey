package com.philips.easykey.lock.mvp.view.deviceaddview;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.bean.ServerGatewayInfo;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.GetBindGatewayListResult;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.GwWiFiBaseInfo;

import java.util.List;

public interface DeviceGatewayBindListView extends IBaseView {
    //获取网关状态
    void getGatewayStateSuccess(String deviceId,String gatewayState);

}
