package com.philips.easykey.lock.mvp.view.deviceaddview;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.mqtt.eventbean.DeviceOnLineBean;

public interface IAddCatEyeView extends IBaseView {


    /**
     * 添加超时
     */
    void joinTimeout();

    /**
     * 添加猫眼成功
     */
    void cateEyeJoinSuccess(DeviceOnLineBean deviceOnLineBean);

    /**
     * 添加猫眼失败
     */
    void catEysJoinFailed(Throwable throwable);



}
