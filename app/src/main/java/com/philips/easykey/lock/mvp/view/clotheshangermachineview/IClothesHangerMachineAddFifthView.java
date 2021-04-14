package com.philips.easykey.lock.mvp.view.clotheshangermachineview;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;

public interface IClothesHangerMachineAddFifthView extends IBaseView {


    void onDeviceStateChange(boolean isConnected);

    void onMatchingSuccess();

    void onMatchingFailed();

    void onSendSuccess(int sendType);

    void onBindDeviceSuccess();

    void onBindDeviceFailed();

    void onBindDeviceFailed(Throwable throwable);
}
