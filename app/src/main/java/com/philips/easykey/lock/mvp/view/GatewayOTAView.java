package com.philips.easykey.lock.mvp.view;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;

public interface GatewayOTAView extends IBaseView {

    //网关正在升级
    void gatewayUpgradeingNow(String deviceId);

    //网关正在升级失败
    void gatewayUpgradeFail(String deviceId);


}
