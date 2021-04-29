package com.philips.easykey.lock.mvp.view.wifilock.videolock;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.bean.WiFiLockPassword;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.WifiLockShareResult;

import java.util.List;

public interface IPhilipsWifiVideoLockDetailView extends IBaseView {
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



    /**
     * 查询普通用户列表成功
     */
    void querySuccess(List<WifiLockShareResult.WifiLockShareUser> users);

    /**
     * 查询普通用户列表失败
     */
    void queryFailedServer(BaseResult result);

    /**
     * 查询普通用户列表异常
     */
    void queryFailed(Throwable throwable);


    void onDeleteDeviceSuccess();

    void onDeleteDeviceFailed(Throwable throwable);

    void onDeleteDeviceFailedServer(BaseResult result);

}
