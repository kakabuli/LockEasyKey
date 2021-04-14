package com.philips.easykey.lock.mvp.view.wifilock;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;

public interface IWifiLockVideoSixthView extends IBaseView {

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


    void onCheckError(byte[] data);


    void onSetNameSuccess(); //设置名称成功
    void onSetNameFailedNet(Throwable throwable); //设置名称网络异常
    void onSetNameFailedServer(BaseResult baseResult);  //设置名称  服务器返回错误

}
