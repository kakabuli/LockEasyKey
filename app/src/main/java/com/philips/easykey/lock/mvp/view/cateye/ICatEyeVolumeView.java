package com.philips.easykey.lock.mvp.view.cateye;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;

public interface ICatEyeVolumeView extends IBaseView {

    //设置音量成功
    void setVolumeSuccess(int number);

    //设置音量失败
    void setVolumeFail();

    //设置音量异常
    void setVolumeThrowable(Throwable throwable);


}
