package com.philips.easykey.lock.mvp.view;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;

public interface IBindBleSecondView extends IBaseView {
    void onDeviceStateChange(boolean isConnected);
}
