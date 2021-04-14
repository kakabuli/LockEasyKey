package com.philips.easykey.lock.mvp.view.cateye;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.GetPirSlientBean;

public interface ICatEyeDefaultView extends IBaseView {
    //获取静默参数成功
    void getPirSlientSuccess(GetPirSlientBean.ReturnDataBean dataBean);

    //获取静默参数失败
    void getPirSlientFail();

    //获取静默参数异常
    void getPirSlientThrowable(Throwable throwable);

    //设置静默参数成功
    void setPirSlientSuccess();

    //设置静默参数失败
    void setPirSlientFail();

    //设置静默参数异常
    void setPirSlientThrowable(Throwable throwable);


}
