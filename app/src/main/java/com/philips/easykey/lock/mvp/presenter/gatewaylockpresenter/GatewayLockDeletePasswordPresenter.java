package com.philips.easykey.lock.mvp.presenter.gatewaylockpresenter;

import com.google.gson.Gson;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.gatewaylockview.GatewayLockDeletePasswordView;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.publiclibrary.mqtt.MqttCommandFactory;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.LockPwdFuncBean;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttData;
import com.philips.easykey.lock.utils.LogUtils;
import com.philips.easykey.lock.utils.greenDao.bean.GatewayLockPwd;
import com.philips.easykey.lock.utils.greenDao.db.GatewayLockPwdDao;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class GatewayLockDeletePasswordPresenter<T> extends BasePresenter<GatewayLockDeletePasswordView> {
    private Disposable deleteLockPwdDdisposable;

    //删除用户密码
    public void gatewayLockDeletePwd(String gatewayId, String deviceId, String pwdNum) {
        toDisposable(deleteLockPwdDdisposable);
        if (mqttService != null) {
            deleteLockPwdDdisposable = mqttService.mqttPublish(MqttConstant.getCallTopic(MyApplication.getInstance().getUid()), MqttCommandFactory.lockPwdFunc(gatewayId, deviceId, "clear", "pin", pwdNum, ""))
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            if (MqttConstant.SET_PWD.equals(mqttData.getFunc())) {
                                return true;
                            }
                            return false;
                        }
                    })
                    .timeout(20 * 1000, TimeUnit.MILLISECONDS)
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            LogUtils.d("删除" + mqttData.getFunc());
                            toDisposable(deleteLockPwdDdisposable);
                            LockPwdFuncBean lockPwdFuncBean = new Gson().fromJson(mqttData.getPayload(), LockPwdFuncBean.class);
                            if ("200".equals(mqttData.getReturnCode()) &&
                                    lockPwdFuncBean.getReturnData().getStatus() == 0) {
                                //删除成功
                                if (isSafe()) {
                                    mViewRef.get().deleteLockPwdSuccess();
                                    deleteOnePwd(gatewayId, deviceId, MyApplication.getInstance().getUid(), pwdNum);
                                }
                            } else {
                                if (isSafe()) {
                                    mViewRef.get().deleteLockPwdFail();
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().delteLockPwdThrowable(throwable);
                            }
                        }
                    });
            compositeDisposable.add(deleteLockPwdDdisposable);
        }


    }

    //删除某个数据
    private void deleteOnePwd(String gatewayId, String deviceId, String uid, String num) {
        GatewayLockPwdDao gatewayLockPwdDao = MyApplication.getInstance().getDaoWriteSession().getGatewayLockPwdDao();
        GatewayLockPwd gatewayLockPwd = gatewayLockPwdDao.queryBuilder().where(GatewayLockPwdDao.Properties.GatewayId.eq(gatewayId), GatewayLockPwdDao.Properties.DeviceId.eq(deviceId), GatewayLockPwdDao.Properties.Uid.eq(uid), GatewayLockPwdDao.Properties.Num.eq(num)).unique();
        if (gatewayLockPwd != null) {
            gatewayLockPwdDao.delete(gatewayLockPwd);
        }
    }


}
