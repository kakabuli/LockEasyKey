package com.philips.easykey.lock.mvp.view;


import com.philips.easykey.lock.mvp.mvpbase.ICheckOtaView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;

/**
 * Created by David on 2019/3/14
 */
public interface IOldDeviceInfoView extends ICheckOtaView {
    /**
     * 软件版本
     */
    void SoftwareRevDataSuccess(String data);

    void SoftwareRevDataError(Throwable throwable);

    /**
     * 硬件版本
     */
    void HardwareRevDataSuccess(String data);

    void HardwareRevDataError(Throwable throwable);

    /**
     * 锁型号
     */
    void FirmwareRevDataSuccess(String data);

    void FirmwareRevDataError(Throwable throwable);

    /**
     * 序列号 sn
     */
    void SerialNumberDataSuccess(String data);

    void SerialNumberDataError(Throwable throwable);

    /**
     * 模块代号
     */
    void ModelNumberDataSuccess(String data);

    void ModelNumberDataError(Throwable throwable);


    /**
     * 修改设备昵称成功
     */
    void modifyDeviceNicknameSuccess();

    /**
     * 修改昵称异常
     */
    void modifyDeviceNicknameError(Throwable throwable);

    /**
     * 修改昵称失败
     */
    void modifyDeviceNicknameFail(BaseResult baseResult);

    /**
     *上传设备信息服务器失败
     */
    void onUpdateSoftFailedServer(BaseResult baseResult);
    /**
     *上传设备信息网络问题
     */
    void onUpdateSoftFailed(Throwable throwable);
}
