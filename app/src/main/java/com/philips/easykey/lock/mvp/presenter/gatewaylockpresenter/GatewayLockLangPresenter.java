package com.philips.easykey.lock.mvp.presenter.gatewaylockpresenter;

import com.google.gson.Gson;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.gatewaylockview.GatewayLockLangView;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.publiclibrary.mqtt.MqttCommandFactory;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.GetLockLang;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.SetLockLang;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttData;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class GatewayLockLangPresenter<T> extends BasePresenter<GatewayLockLangView> {

    private Disposable getLangDisposable;
    private Disposable setLangDisposable;

    //获取语言
    public void getLang(String gatewayId, String deviceId) {
        toDisposable(getLangDisposable);
        if (mqttService != null) {
            getLangDisposable = mqttService.mqttPublish(MqttConstant.getCallTopic(MyApplication.getInstance().getUid()), MqttCommandFactory.getLockLang(gatewayId, deviceId))
                    .timeout(10 * 1000, TimeUnit.MILLISECONDS)
                    .compose(RxjavaHelper.observeOnMainThread())
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            if (MqttConstant.GET_LANG.equals(mqttData.getFunc())) {
                                return true;
                            }
                            return false;
                        }
                    })
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            toDisposable(getLangDisposable);
                            GetLockLang getLockLang = new Gson().fromJson(mqttData.getPayload(), GetLockLang.class);
                            if ("200".equals(getLockLang.getReturnCode())) {
                                if (isSafe()) {
                                    mViewRef.get().getLockLangSuccess(getLockLang.getReturnData().getLang());
                                }
                            } else {
                                if (isSafe()) {
                                    mViewRef.get().getLockLangFail();
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().getLockLangThrowable(throwable);
                            }
                        }
                    });
            compositeDisposable.add(getLangDisposable);
        }
    }


    //设置锁的语言
    public void setLang(String gatewayId, String deviceId, String lang) {
        toDisposable(setLangDisposable);
        if (mqttService != null) {
            setLangDisposable = mqttService.mqttPublish(MqttConstant.getCallTopic(MyApplication.getInstance().getUid()), MqttCommandFactory.setLockLang(gatewayId, deviceId, lang))
                    .timeout(10 * 1000, TimeUnit.MILLISECONDS)
                    .compose(RxjavaHelper.observeOnMainThread())
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            if (MqttConstant.SET_LANG.equals(mqttData.getFunc())) {
                                return true;
                            }
                            return false;
                        }
                    })
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            toDisposable(setLangDisposable);
                            SetLockLang setLockLang = new Gson().fromJson(mqttData.getPayload(), SetLockLang.class);
                            if ("200".equals(setLockLang.getReturnCode())) {
                                if (isSafe()) {
                                    mViewRef.get().setLockLangSuccess(setLockLang.getParams().getLanguage());
                                }
                            } else {
                                if (isSafe()) {
                                    mViewRef.get().setLockLangFail();
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().setLockLangThrowable(throwable);
                            }
                        }
                    });
            compositeDisposable.add(setLangDisposable);
        }
    }
}
