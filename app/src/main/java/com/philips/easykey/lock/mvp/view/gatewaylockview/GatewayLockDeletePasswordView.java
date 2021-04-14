package com.philips.easykey.lock.mvp.view.gatewaylockview;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;

public interface GatewayLockDeletePasswordView extends IBaseView {

    //删除成功
    void deleteLockPwdSuccess();

    //删除失败
    void deleteLockPwdFail();

    //删除异常
    void delteLockPwdThrowable(Throwable throwable);


}
