package com.philips.easykey.lock.activity.device.gatewaylock.password.test;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;

public interface ITestGwTestView extends IBaseView {
    void getLockInfoSuccess(int maxPwd);

    void getLockInfoFail();

    void getLockInfoThrowable(Throwable throwable);
}
