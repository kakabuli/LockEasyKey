package com.philips.easykey.lock.mvp.view.singlefireswitchview;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.AddSingleFireSwitchBean;

public interface SingleFireSwitchView extends IBaseView {

    //设置锁外围（开关）成功
    void settingDeviceSuccess();

    //设置锁外围（开关）失败
    void settingDeviceFail();

    //设置锁外围（开关）异常
    void settingDeviceThrowable();

    //查询锁外围（开关）成功
    void gettingDeviceSuccess();

    //查询锁外围（开关）失败
    void gettingDeviceFail();

    //查询锁外围（开关）异常
    void gettingDeviceThrowable();

    //添加锁外围（开关）成功
    void addDeviceSuccess(AddSingleFireSwitchBean addSingleFireSwitchBean);

    //添加锁外围（开关）失败
    void addDeviceFail();

    //添加锁外围（开关）异常
    void addDeviceThrowable();

    //绑定锁外围（开关）成功
    void bindingAndModifyDeviceSuccess();

    //绑定锁外围（开关）失败
    void bindingAndModifyDeviceFail();

    //绑定锁外围（开关）异常
    void bindingAndModifyDeviceThrowable();


}
