package com.philips.easykey.lock.mvp.view;


import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.BluetoothSharedDeviceBean;


public interface IGatewaySharedDeviceManagementView extends IBaseView {
    /**
     * 查询普通用户列表成功
     */
    void queryCommonUserListSuccess(BluetoothSharedDeviceBean bluetoothSharedDeviceBean);

    /**
     * 查询普通用户列表失败
     */
    void queryCommonUserListFail(BluetoothSharedDeviceBean bluetoothSharedDeviceBean);

    /**
     * 查询普通用户列表异常
     */
    void queryCommonUserListError(Throwable throwable);

    void addCommonUserSuccess(BaseResult baseResult);

    void addCommonUserFail(BaseResult baseResult);

    void addCommonUserError(Throwable throwable);
}
