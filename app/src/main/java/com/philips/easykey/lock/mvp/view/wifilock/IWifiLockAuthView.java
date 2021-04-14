package com.philips.easykey.lock.mvp.view.wifilock;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;

public interface IWifiLockAuthView extends IBaseView {


    void onDeleteDeviceSuccess();

    void onDeleteDeviceFailed(Throwable throwable);

    void onDeleteDeviceFailedServer(BaseResult result);

}
