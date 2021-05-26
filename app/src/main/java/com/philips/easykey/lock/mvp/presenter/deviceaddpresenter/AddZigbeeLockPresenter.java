package com.philips.easykey.lock.mvp.presenter.deviceaddpresenter;

import com.google.gson.Gson;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.deviceaddview.IAddZigbeeLockView;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.publiclibrary.mqtt.MqttCommandFactory;
import com.philips.easykey.lock.publiclibrary.mqtt.eventbean.DeviceOnLineBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.SetJoinAllowBean;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttData;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.SPUtils;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class AddZigbeeLockPresenter<T> extends BasePresenter<IAddZigbeeLockView> {
    private Disposable addZigbeeDisposable;
    private Disposable addZigbeeEvent;

    //网关开启入网模式
    public void openJoinAllow(String gatewayId) {
        toDisposable(addZigbeeDisposable);
        if (mqttService != null && mqttService.getMqttClient() != null && mqttService.getMqttClient().isConnected()) {
            MqttMessage mqttMessage = MqttCommandFactory.setJoinAllow(MyApplication.getInstance().getUid(), gatewayId, gatewayId);
            addZigbeeDisposable = mqttService.mqttPublish(MqttConstant.getCallTopic(MyApplication.getInstance().getUid()), mqttMessage)
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            // TODO: 2019/4/19    以后要改成通过MSG  Id来判断
                            if (MqttConstant.SET_JOIN_ALLOW.equals(mqttData.getFunc())) {
                                return true;
                            }
                            return false;
                        }
                    })
                    .timeout(30 * 1000, TimeUnit.MILLISECONDS)
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            //网关允许入网成功
                            toDisposable(addZigbeeDisposable);
                            SetJoinAllowBean setJoinAllowBean = new Gson().fromJson(mqttData.getPayload(), SetJoinAllowBean.class);
                            if (setJoinAllowBean != null) {
                                if ("200".equals(setJoinAllowBean.getReturnCode())) {
                                    if (isSafe()) {
                                        mViewRef.get().netInSuccess();
                                    }
                                } else {
                                    if (isSafe()) {
                                        mViewRef.get().netInFail();

                                    }
                                }

                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().netInThrowable();
                            }
                        }
                    });
            compositeDisposable.add(addZigbeeDisposable);
        }
    }


    //设备上线通知
    public void deviceZigbeeIsOnLine(String gwId) {
        toDisposable(addZigbeeEvent);
        if (mqttService != null) {
            addZigbeeEvent = mqttService.listenerDataBack()
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            return mqttData.getFunc().equals(MqttConstant.GW_EVENT);
                        }
                    })
                    .timeout(120 * 1000, TimeUnit.MILLISECONDS)
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            DeviceOnLineBean deviceOnLineBean = new Gson().fromJson(mqttData.getPayload(), DeviceOnLineBean.class);
                            //todo 假如此时其他的zigbee上线了，如何处理
                            if (gwId.equals(deviceOnLineBean.getGwId()) && MqttConstant.ON_LINE.equals(deviceOnLineBean.getEventparams().getEvent_str()) && deviceOnLineBean.getEventparams().getDevice_type().equals("zigbee")) {
                                //设备信息匹配成功  且是上线上报
                                if (isSafe()) {
                                    LogUtils.d("添加网关成功");
                                    mViewRef.get().addZigbeeSuccess(deviceOnLineBean);
                                    MyApplication.getInstance().getAllDevicesByMqtt(true);
                                    SPUtils.remove(KeyConstants.SAVA_LOCK_PWD + deviceOnLineBean.getDeviceId());
                                    SPUtils.remove(KeyConstants.FIRST_IN_GATEWAY_LOCK + MyApplication.getInstance().getUid() + deviceOnLineBean.getDeviceId());
                                    toDisposable(addZigbeeEvent);
                                }

                            }


                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().addZigbeeThrowable();

                            }
                        }
                    });
            compositeDisposable.add(addZigbeeEvent);
        }


    }


}
