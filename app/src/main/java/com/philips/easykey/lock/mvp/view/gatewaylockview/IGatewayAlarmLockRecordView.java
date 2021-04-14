package com.philips.easykey.lock.mvp.view.gatewaylockview;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.GetAlarmRecordResult;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.SelectOpenLockResultBean;

import java.util.List;

public interface IGatewayAlarmLockRecordView extends IBaseView {

    //获取开锁记录成功
    void getOpenLockRecordSuccess(    List<GetAlarmRecordResult.DataBean> data);

    //获取开锁记录失败
    void getOpenLockRecordFail();

    //获取开锁记录异常
    void getOpenLockRecordThrowable(Throwable throwable);

}
