package com.philips.easykey.lock.mvp.presenter.wifilock.x9;

import com.google.gson.Gson;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockMoreView;
import com.philips.easykey.lock.mvp.view.wifilock.x9.IWifiLockOpenForceView;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.publiclibrary.mqtt.MqttCommandFactory;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.SettingOpenForce;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.SettingOpenForceResult;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttData;
import com.philips.easykey.lock.utils.LogUtils;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class WifiLockOpenForcePresenter<T> extends BasePresenter<IWifiLockOpenForceView> {
    private Disposable setOpenForceDisposable;

    public void setOpenForce(String wifiSN,int openForce) {
        if (mqttService != null && mqttService.getMqttClient() != null && mqttService.getMqttClient().isConnected()) {
            MqttMessage mqttMessage = MqttCommandFactory.settingOpenForce(wifiSN,openForce);
            toDisposable(setOpenForceDisposable);
            setOpenForceDisposable = mqttService.mqttPublish(MqttConstant.getCallTopic(MyApplication.getInstance().getUid()),mqttMessage)
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            if(MqttConstant.SET_OPEN_FORCE.equals(mqttData.getFunc())){
                                return true;
                            }
                            return false;
                        }
                    })
                    .timeout(10 * 1000, TimeUnit.MILLISECONDS)
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            SettingOpenForceResult settingOpenForce = new Gson().fromJson(mqttData.getPayload(), SettingOpenForceResult.class);
                            LogUtils.e("shulan settingOpenForce-->" + settingOpenForce.toString());
                            if(settingOpenForce != null && isSafe()){
                                if("200".equals(settingOpenForce.getCode() + "")){
                                    mViewRef.get().settingSuccess(settingOpenForce.getParams().getOpenForce());
                                }else if("201".equals(settingOpenForce.getCode() + "")){
                                    mViewRef.get().settingFailed();
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if(isSafe()){
                                mViewRef.get().settingThrowable(throwable);
                            }
                        }
                    });
            compositeDisposable.add(setOpenForceDisposable);
        }
    }
}
