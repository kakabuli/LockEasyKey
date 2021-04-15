package com.philips.easykey.lock.mvp.view.deviceaddview;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.BindGatewayResultBean;

import java.util.List;

public interface GatewayBindView extends IBaseView {


    //绑定正常网关成功
    void bindGatewaySuccess(String deviceSN);

    //绑定套装成功
    void bindGatewaySuitSuccess(String deviceSN, List<BindGatewayResultBean.DataBean.DeviceListBean> mDeviceList,boolean isbindMeMe);

    //绑定网关失败
    void bindGatewayFail(String code,String msg);

    //绑定套装失败
    void bindGatewaySuitFail(String code,String msg);


    //绑定网关异常
    void bindGatewayThrowable(Throwable throwable);

}
