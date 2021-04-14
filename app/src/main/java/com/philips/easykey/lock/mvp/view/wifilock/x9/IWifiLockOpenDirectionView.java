package com.philips.easykey.lock.mvp.view.wifilock.x9;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;

public interface IWifiLockOpenDirectionView extends IBaseView  {

    void settingThrowable(Throwable throwable);

    void settingFailed();

    void settingSuccess(int openDirection);


}
