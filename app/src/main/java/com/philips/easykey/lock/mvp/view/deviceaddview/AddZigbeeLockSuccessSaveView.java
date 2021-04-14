package com.philips.easykey.lock.mvp.view.deviceaddview;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;

public interface AddZigbeeLockSuccessSaveView extends IBaseView {

    //网关下设备名称修改成功
    void updateDevNickNameSuccess();

    //网关下设备名称修改失败
    void updateDevNickNameFail();

    //网关下设备名称异常
    void updateDevNickNameThrowable(Throwable throwable);

}
