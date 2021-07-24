package com.philips.easykey.lock.mvp.view.wifilock.x9;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;

public interface IWifiLockOpenForceView extends IBaseView  {

    //设置的值回调
    void onSettingCallBack(boolean flag);

}
