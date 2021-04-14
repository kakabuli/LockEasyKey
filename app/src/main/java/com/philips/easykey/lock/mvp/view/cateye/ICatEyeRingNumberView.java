package com.philips.easykey.lock.mvp.view.cateye;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;

public interface ICatEyeRingNumberView extends IBaseView {

    //设置响铃次数成功
    void setRingNumberSuccess(int number);

    //设置响铃次数失败
    void setRingNumberFail();

    //设置响铃次数异常
    void setRingNumberThrowable(Throwable throwable);


}
