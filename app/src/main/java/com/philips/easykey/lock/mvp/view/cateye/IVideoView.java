package com.philips.easykey.lock.mvp.view.cateye;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.bean.CateEyeInfo;
import com.philips.easykey.lock.publiclibrary.bean.GwLockInfo;
import com.philips.easykey.lock.publiclibrary.mqtt.eventbean.DeviceOnLineBean;
import com.philips.easykey.lock.utils.StringUtil;

public interface IVideoView extends IBaseView {
    /**
     * 猫眼呼叫进来  且米米网登录成功
     */
    void onCatEyeCallIn();

    /**
     * 猫眼呼叫进来  且米米网登录成功
     */
    void loginMemeFailed();

    /**
     * sip 连接成功
     */
    void onCallConnected();

    /**
     * Sip  已经有数据流
     */
    void onStreaming();

    /**
     * 结束通话
     */
    void onCallFinish();

    /**
     * 截图成功
     */
    void screenShotSuccess();
    /**
     * 截图成功刷新图库
     */
    void screenShotSuccessPath(String filePath);

    /**
     * 截图失败
     */
    void screenShotFailed(Exception e);

    /**
     * 录屏时间少于5秒钟
     */
    void recordTooShort();

    void recordTooStart();

    void recordTooEnd();

    void recordExceptionTooShort();
    void recordFrequentlyTooShort();
    /**
     * 唤醒猫眼成功
     */
    void wakeupSuccess();

    /**
     * 唤醒猫眼失败
     */
    void wakeupFailed();

    /**
     * 唤醒猫眼失败
     */
    void wakeupFailedStateCode(String code);


    /**
     * 等待猫眼呼过来超时
     */
    void waitCallTimeout();



    /**
     * 通话时间
     */
    void callTimes(String time);

    /**
     * 猫眼当前为离线状态
     */
    void onCatEyeOffline( );


    /**
     * 输入密码
     */
    void inputPwd(GwLockInfo gwLockInfo);

    /**
     * 开锁成功
     */
    void openLockSuccess(String deviceId);
    /**
     * 开锁失败
     */
    void openLockFailed(String deviceId);

    /**
     * 开锁异常
     * @param throwable
     */
    void openLockThrowable(Throwable throwable,String devId);

    /**
     * 开始开锁
     */
    void startOpenLock(String deviceId);
    /**
     * 关锁成功
     */
    void lockCloseSuccess(String deviceId);
    /**
     * 关锁失败
     */
    void lockCloseFailed(String devId);

    /**
     * 有设备状态改变
     */

    void deviceStatusChange(DeviceOnLineBean deviceOnLineBean);


    /**
     * 手机网络改变
     */

    void netWorkChange(boolean isEnable);

    void callSuccess();

    //开锁上报
    void  getLockEvent(String gwId,String deviceId);

    //关锁上报
    void closeLockSuccess(String devId,String gwId);

    //关锁上报异常
    void closeLockThrowable();

    void  closeMain();
}
