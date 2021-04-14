package com.philips.easykey.lock.mvp.view.wifilock;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;

public interface IWifiLockAPWifiSetUpView   extends IBaseView {


    /**
     * 绑定成功
     */

    void onBindSuccess(String wifiSn);

    /**
     * 绑定失败
     */
    void onBindFailed(BaseResult baseResult);

    /**
     * 绑定异常
     */
    void onBindThrowable(Throwable throwable);

    /**
     * 绑定成功
     */

    void onUpdateSuccess(String wifiSn);

    /**
     * 绑定失败
     */
    void onUpdateFailed(BaseResult baseResult);

    /**
     * 绑定异常
     */
    void onUpdateThrowable(Throwable throwable);



    void onSendSuccess();

    void onSendFailed();

    void onReceiverFailed();

    void onReceiverSuccess();


}