package com.philips.easykey.lock.mvp.view.gatewaylockview;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.SelectOpenLockResultBean;

import java.util.List;

public interface IGatewayLockRecordView extends IBaseView {

    //获取开锁记录成功
    void getOpenLockRecordSuccess(List<SelectOpenLockResultBean.DataBean> mOpenLockRecordList);

    //获取开锁记录失败
    void getOpenLockRecordFail();

    //获取开锁记录异常
    void getOpenLockRecordThrowable(Throwable throwable);

}
