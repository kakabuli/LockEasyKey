package com.philips.easykey.lock.mvp.view;

import com.philips.easykey.lock.mvp.mvpbase.IBleView;

public interface IFaceOtaView extends IBleView {

    void otaSuccess();

    void otaFailed(int state);

    /**
     * 结束OTA成功
     */
    void onFinishOtaSuccess();

    /**
     * 结束OTA失败
     * @param throwable
     */
    void onFinishOtaFailed(Throwable throwable);
}
