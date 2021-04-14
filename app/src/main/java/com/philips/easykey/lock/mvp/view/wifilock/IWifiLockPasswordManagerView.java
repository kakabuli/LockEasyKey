package com.philips.easykey.lock.mvp.view.wifilock;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.bean.WiFiLockPassword;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;

public interface IWifiLockPasswordManagerView extends IBaseView {

    /**
     * 获取密码成功
     * @param wiFiLockPassword
     */
    void onGetPasswordSuccess(WiFiLockPassword wiFiLockPassword);

    /**
     * @param baseResult
     */
    void onGetPasswordFailedServer(BaseResult baseResult);

    /**
     *  获取密码失败
     * @param throwable
     */
    void onGetPasswordFailed(Throwable throwable);



}
