package com.philips.easykey.lock.mvp.view;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.bean.VersionBean;


public interface ISplashView extends IBaseView {

    void getVersionSuccess(VersionBean versionBean);

    void getVersionFail();

    //蓝牙服务启动成功和mqtt服务启动成功
    void serviceConnectSuccess();

    //蓝牙服务启动失败获取mqtt服务器启动失败
    void serviceConnectThrowable();

}
