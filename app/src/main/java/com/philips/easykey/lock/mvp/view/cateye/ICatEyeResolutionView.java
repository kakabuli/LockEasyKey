package com.philips.easykey.lock.mvp.view.cateye;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;

public interface ICatEyeResolutionView extends IBaseView {

    //设置分辨率成功
    void setResolutionSuccess(String resolution);

    //设置分辨率失败
    void setResolutionFail();

    //设置分辨率异常
    void setResolutionThrowable(Throwable throwable);

}
