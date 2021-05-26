package com.philips.easykey.lock.mvp.view;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;

public interface IDoorLockMessageView extends IBaseView {

    /**
     * 锁状态更新
     */
    void onWifiLockActionUpdate();
}
