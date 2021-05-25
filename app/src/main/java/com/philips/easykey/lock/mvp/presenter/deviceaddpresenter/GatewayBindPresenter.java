package com.philips.easykey.lock.mvp.presenter.deviceaddpresenter;

import com.google.gson.Gson;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.publiclibrary.mqtt.MqttCommandFactory;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.BindGatewayResultBean;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttData;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.mvp.view.deviceaddview.GatewayBindView;


import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;


public class GatewayBindPresenter<T> extends BasePresenter<GatewayBindView> {
    private Disposable bindGatewayDisposable;
    private Disposable bingMimiDisposable;

    //绑定网关
    public void bindGateway(String deviceSN) {
        toDisposable(bindGatewayDisposable);
        MqttMessage mqttMessage = MqttCommandFactory.bindGateway(MyApplication.getInstance().getUid(), deviceSN);
        if (mqttService != null) {
            bindGatewayDisposable = mqttService.mqttPublish(MqttConstant.MQTT_REQUEST_APP, mqttMessage)
                    .compose(RxjavaHelper.observeOnMainThread())
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            //TODO  以后改成根据  msgId 区分是不是当前消息的回调
                            if (MqttConstant.BIND_GATEWAY.equals(mqttData.getFunc())) {
                                return true;
                            }
                            return false;
                        }
                    })
                    .timeout(10 * 1000, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            LogUtils.d("绑定网关回调" + mqttData.getPayload());
                            BindGatewayResultBean bindGatewayResult = new Gson().fromJson(mqttData.getPayload(), BindGatewayResultBean.class);
                            LogUtils.d(bindGatewayResult.getFunc());
                            if ("200".equals(bindGatewayResult.getCode()) && bindGatewayResult.getData().getDeviceList() != null && bindGatewayResult.getData().getDeviceList().size() > 0) {
                                if (isSafe()) {
                                    if (bindGatewayResult.getData().getMeBindState() == 1) {
                                        mViewRef.get().bindGatewaySuitSuccess(deviceSN, bindGatewayResult.getData().getDeviceList(), true);
                                    } else {
                                        mViewRef.get().bindGatewaySuitSuccess(deviceSN, bindGatewayResult.getData().getDeviceList(), false);
                                    }
                                    MyApplication.getInstance().getAllDevicesByMqtt(true);
                                }
                            } else if ("200".equals(bindGatewayResult.getCode())) {
                                if (isSafe()) {
                                    mViewRef.get().bindGatewaySuccess(deviceSN);
                                    //bindMimi(deviceSN,deviceSN);
                                    MyApplication.getInstance().getAllDevicesByMqtt(true);
                                }
                            } else {
                                if (isSafe()) {
                                    if (bindGatewayResult.getData().getDeviceList() != null && bindGatewayResult.getData().getDeviceList().size() > 0) {
                                        mViewRef.get().bindGatewaySuitFail(bindGatewayResult.getCode(), bindGatewayResult.getMsg());
                                    } else {
                                        mViewRef.get().bindGatewayFail(bindGatewayResult.getCode(), bindGatewayResult.getMsg());
                                    }

                                }
                            }
                            toDisposable(bindGatewayDisposable);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().bindGatewayThrowable(throwable);
                            }
                        }
                    });
            compositeDisposable.add(bindGatewayDisposable);
        }

    }

}
