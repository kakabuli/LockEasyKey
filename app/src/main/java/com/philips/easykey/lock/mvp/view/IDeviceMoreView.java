package com.philips.easykey.lock.mvp.view;


import com.philips.easykey.lock.mvp.mvpbase.ICheckOtaView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;

/**
 * Create By lxj  on 2019/3/4
 * Describe
 */
public interface IDeviceMoreView extends ICheckOtaView {

    void onDeleteDeviceSuccess();

    void onDeleteDeviceFailed(Throwable throwable);

    void onDeleteDeviceFailedServer(BaseResult result);

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
     * 获取是否开启声音
     */

    void getVoice(int voice);


    void setVoiceSuccess(int voice);

    void setVoiceFailed(Throwable throwable, int voice);

    /**
     * 获取是否开启自动关门
     */

    void getAutoLock(boolean isOpen);


    void setAutoLockSuccess(boolean isOpen);

    void setAutoLockFailed( int b);

    void setAutoLockError(Throwable throwable);

    /**
     * 读取SN成功
     */
    void readSnSuccess(String sn);

    /**
     * 读取蓝牙模块信息失败
     */
    void readInfoFailed(Throwable throwable);
    /**
     * 读取版本成功
     */
    void readVersionSuccess(String version);

    /**
     * @param type
     */
    void onStateUpdate(int type);

    /**
     * 更新软件版本失败，错误码
     */
    void onUpdateSoftFailed(Throwable throwable);

    /**
     * 更新软件版本失败，服务器返回
     */
    void onUpdateSoftFailedServer(BaseResult baseResult);


}
