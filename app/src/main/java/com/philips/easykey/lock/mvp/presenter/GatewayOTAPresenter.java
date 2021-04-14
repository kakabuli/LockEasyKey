package com.philips.easykey.lock.mvp.presenter;

import com.google.gson.Gson;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.GatewayOTAView;
import com.philips.easykey.lock.mvp.view.IAddCardEndView;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.publiclibrary.mqtt.MqttCommandFactory;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.GatewayComfirmOtaResultBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.GatewayOtaNotifyBean;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttData;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class GatewayOTAPresenter<T> extends BasePresenter<GatewayOTAView> {
    private Disposable comfirmGatewayOtaDisposable;

    //网关ota确认升级
    public void confirmGatewayOtaUpgrade(GatewayOtaNotifyBean otaNotifyBean, String uid) {
        if (mqttService != null) {
            toDisposable(comfirmGatewayOtaDisposable);
            MqttMessage message = MqttCommandFactory.gatewayOtaUpgrade(otaNotifyBean, uid);
            comfirmGatewayOtaDisposable = mqttService.mqttPublish(MqttConstant.PUBLISH_TO_SERVER, message)
                    .timeout(10 * 1000, TimeUnit.MILLISECONDS)
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            if (mqttData != null) {
                                if (mqttData.getFunc().equals(MqttConstant.CONFIRM_GATEWAY_OTA)) {
                                    toDisposable(comfirmGatewayOtaDisposable);
                                    GatewayComfirmOtaResultBean gatewayComfirmOtaResultBean = new Gson().fromJson(mqttData.getPayload(), GatewayComfirmOtaResultBean.class);
                                    if ("200".equals(gatewayComfirmOtaResultBean.getCode())) {
                                        if (isSafe()) {
                                            mViewRef.get().gatewayUpgradeingNow(gatewayComfirmOtaResultBean.getDeviceId());
                                        }
                                    } else {
                                        if (isSafe()) {
                                            mViewRef.get().gatewayUpgradeFail(gatewayComfirmOtaResultBean.getDeviceId());
                                        }
                                    }

                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().gatewayUpgradeFail(otaNotifyBean.getDeviceId());
                            }
                        }
                    });
            compositeDisposable.add(comfirmGatewayOtaDisposable);
        }


    }


}
