package com.philips.easykey.lock.mvp.presenter.gatewaylockpresenter;

import com.google.gson.Gson;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.IHomeView;
import com.philips.easykey.lock.mvp.view.gatewaylockview.GatewayLockPasswrodView;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.publiclibrary.mqtt.MqttCommandFactory;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.LockPwdFuncBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.LockPwdInfoBean;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttData;
import com.philips.easykey.lock.utils.greenDao.bean.GatewayLockPwd;
import com.philips.easykey.lock.utils.greenDao.db.GatewayLockPwdDao;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class GatewayLockPasswordForeverPresenter<T> extends BasePresenter<GatewayLockPasswrodView> {
    private Disposable addLockPwddDisposable;

    //添加密码
    public void addLockPwd(String gatewayiId, String deviceId, String pwdId, String pwdValue) {
        toDisposable(addLockPwddDisposable);
        if (mqttService != null) {
            addLockPwddDisposable = mqttService.mqttPublish(MqttConstant.getCallTopic(MyApplication.getInstance().getUid()),
                    MqttCommandFactory.lockPwdFunc(gatewayiId, deviceId, "set", "pin", pwdId, pwdValue))
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            if (mqttData.getFunc().equals(MqttConstant.SET_PWD)) {
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
                            toDisposable(addLockPwddDisposable);
                            LockPwdFuncBean pwdFuncBean = new Gson().fromJson(mqttData.getPayload(), LockPwdFuncBean.class);
                            if ("200".equals(pwdFuncBean.getReturnCode()) && pwdFuncBean.getReturnData().getStatus() == 0) {
                                if (isSafe()) {
                                    deleteOnePwd(gatewayiId, deviceId, MyApplication.getInstance().getUid(), pwdId);
                                    mViewRef.get().addLockPwdSuccess(pwdId, pwdValue);
                                    AddOnePwd(gatewayiId, deviceId, MyApplication.getInstance().getUid(), pwdId);
                                }
                            } else {
                                if (isSafe()) {
                                    mViewRef.get().addLockPwdFail(pwdFuncBean.getReturnData().getStatus());
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().addLockPwdThrowable(throwable);
                            }
                        }
                    });
            compositeDisposable.add(addLockPwddDisposable);
        }

    }

    //添加某个
    private void AddOnePwd(String gatewayId, String deviceId, String uid, String num) {
        GatewayLockPwdDao gatewayLockPwdDao = MyApplication.getInstance().getDaoWriteSession().getGatewayLockPwdDao();
        GatewayLockPwd gatewayLockPwd = new GatewayLockPwd();
        gatewayLockPwd.setUid(uid);
        gatewayLockPwd.setNum(num);
        gatewayLockPwd.setStatus(1);
        gatewayLockPwd.setName("");
        gatewayLockPwd.setGatewayId(gatewayId);
        gatewayLockPwd.setDeviceId(deviceId);
        Integer keyInt = Integer.parseInt(num);
        //用于zigbee锁是根据编号识别是永久密码，临时密码，还是胁迫密码
        if (keyInt <= 4) {
            gatewayLockPwd.setTime(1);
        } else if (keyInt <= 8 && keyInt > 4) {
            gatewayLockPwd.setTime(2);
        } else if (keyInt == 9) {
            gatewayLockPwd.setTime(3);
        }
        if (gatewayLockPwdDao != null) {
            gatewayLockPwdDao.insert(gatewayLockPwd);
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
